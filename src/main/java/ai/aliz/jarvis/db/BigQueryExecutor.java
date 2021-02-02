package ai.aliz.jarvis.db;

import lombok.AllArgsConstructor;
import lombok.Lombok;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.api.client.util.Lists;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.FieldList;
import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.LegacySQLTypeName;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.bigquery.TableResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ai.aliz.jarvis.service.shared.platform.BigQueryService;
import ai.aliz.jarvis.service.shared.ExecutorServiceWrapper;
import ai.aliz.jarvis.context.Context;
import ai.aliz.jarvis.util.JarvisUtil;

import static ai.aliz.jarvis.util.JarvisConstants.TEST_INIT;

@Component
@AllArgsConstructor
@Slf4j
public class BigQueryExecutor implements QueryExecutor {
    
    private ExecutorServiceWrapper executorService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private BigQueryService bigQueryService;
    
    @Override
    public void executeStatement(String query, Context context) {
        executeQueryAndGetResult(query, context);
    }
    
    @Override
    public String executeQuery(String query, Context context) {
        TableResult queryResult = executeQueryAndGetResult(query, context);
        ArrayNode result = bigQueryResultToJsonArrayNode(queryResult);
        return result.toString();
    }
    
    @Override
    public void executeScript(String query, Context context) {
        List<String> deletes = Lists.newArrayList();
        List<String> inserts = Lists.newArrayList();
        splitScriptIntoStatements(query)
                .forEach(e -> {
                    if (e.toUpperCase().contains("DELETE FROM")) {
                        deletes.add(e);
                    } else {
                        inserts.add(e);
                    }
                });
        
        List<Runnable> deleteRunnables = statementsToRunnables(context, deletes);
        List<Runnable> insertRunnables = statementsToRunnables(context, inserts);
        
        executorService.executeRunnablesInParallel(deleteRunnables, 60, TimeUnit.SECONDS);
        executorService.executeRunnablesInParallel(insertRunnables, 60, TimeUnit.SECONDS);
    }
    
    public int insertedRowCount(String tableId, String tableName, Context context) {
        TableResult tableResult = executeQueryAndGetResult("SELECT COUNT(*) FROM `" + tableId + "`WHERE " + tableName + "_INSERTED_BY != '" + TEST_INIT + "'", context);
        long count = tableResult.getValues().iterator().next().get(0).getLongValue();
        return (int) count;
    }
    
    public Long getTableLastModifiedAt(Context context, String project, String dataset, String table) {
        BigQuery bigQuery = getBigQueryClient(context);
        log.debug("Getting last modified at for table: {}.{}.{}", project, dataset, table);
        Table bqTable = bigQuery.getTable(TableId.of(project, dataset, table));
        
        return bqTable.getLastModifiedTime();
    }
    
    private BigQuery getBigQueryClient(Context context) {
        return bigQueryService.createBigQueryClient(context);
    }
    
    private List<Runnable> statementsToRunnables(Context context, List<String> statements) {
        return statements.stream()
                         .map(statement -> (Runnable) () -> executeStatement(statement, context))
                         .collect(Collectors.toList());
    }
    
    private TableResult executeQueryAndGetResult(String query, Context context) {
        String completedQuery = JarvisUtil.resolvePlaceholders(query, context.getParameters());
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(completedQuery).build();
        
        BigQuery bigQuery = getBigQueryClient(context);
        try {
            log.info("Executing query: '{}' \n completed as: '{}'", query, completedQuery);
            return bigQuery.query(queryConfig);
        } catch (Exception e) {
            log.error("Failed to execute: " + completedQuery, e);
            throw Lombok.sneakyThrow(e);
        }
    }
    
    private ArrayNode bigQueryResultToJsonArrayNode(TableResult queryResult) {
        FieldList schema = queryResult.getSchema().getFields();
        Iterator<FieldValueList> fieldValueListIterator = queryResult.iterateAll().iterator();
        ArrayNode result = objectMapper.createArrayNode();
        
        while (fieldValueListIterator.hasNext()) {
            FieldValueList row = fieldValueListIterator.next();
            ObjectNode recordNode = bigqueryRowToJsonNode(schema, row);
            result.add(recordNode);
        }
        return result;
    }
    
    private ObjectNode bigqueryRowToJsonNode(FieldList schema, FieldValueList row) {
        ObjectNode recordNode = objectMapper.createObjectNode();
        Iterator<Field> schemaIterator = schema.iterator();
        for (FieldValue value : row) {
            Field fieldSchema = schemaIterator.next();
            createAndAddJsonNodeToParent(fieldSchema, value, recordNode);
        }
        return recordNode;
    }
    
    private void createAndAddJsonNodeToParent(Field fieldSchema, FieldValue fieldValue, ObjectNode parent) {
        BaseJsonNode baseJsonNode;
        LegacySQLTypeName fieldType = fieldSchema.getType();
        try {
            
            if (fieldValue.isNull()) {
                baseJsonNode = NullNode.getInstance();
            } else if (fieldType.equals(LegacySQLTypeName.BOOLEAN)) {
                baseJsonNode = BooleanNode.valueOf(fieldValue.getBooleanValue());
            } else if (fieldType.equals(LegacySQLTypeName.TIMESTAMP)) {
                baseJsonNode = timestampFieldToJsonNode(fieldValue);
            } else if (fieldType.equals(LegacySQLTypeName.RECORD)) {
                if (fieldSchema.getMode() == Field.Mode.REPEATED) {
                    ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
                    baseJsonNode = arrayNode;
                    
                    for (FieldValue arrayElementFieldValue : fieldValue.getRepeatedValue()) {
                        FieldList subFields = fieldSchema.getSubFields();
                        BaseJsonNode arrayElementNode;
                        ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance);
                        FieldValueList recordValue = arrayElementFieldValue.getRecordValue();
                        int i = 0;
                        for (Field arrayElementField : subFields) {
                            createAndAddJsonNodeToParent(arrayElementField, recordValue.get(i), objectNode);
                            i++;
                        }
                        arrayElementNode = objectNode;
                        
                        arrayNode.add(arrayElementNode);
                    }
                } else {
                    ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance);
                    baseJsonNode = objectNode;
                    FieldValueList recordValue = fieldValue.getRecordValue();
                    FieldList subFields = fieldSchema.getSubFields();
                    int i = 0;
                    for (Field arrayElementField : subFields) {
                        createAndAddJsonNodeToParent(arrayElementField, recordValue.get(i), objectNode);
                        i++;
                    }
                }
            } else if (fieldSchema.getMode() == Field.Mode.REPEATED) {
                
                ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
                baseJsonNode = arrayNode;
                
                for (FieldValue arrayElementFieldValue : fieldValue.getRepeatedValue()) {
                    LegacySQLTypeName type = fieldSchema.getType();
                    BaseJsonNode arrayElementNode;
                    
                    if (type.equals(LegacySQLTypeName.STRING)) {
                        String stringValue = arrayElementFieldValue.getStringValue();
                        arrayElementNode = TextNode.valueOf(stringValue);
                    } else {
                        throw new UnsupportedOperationException("Not supported primitive type in array. " + type);
                    }
                    arrayNode.add(arrayElementNode);
                }
            } else {
                String stringValue = fieldValue.getStringValue();
                baseJsonNode = TextNode.valueOf(stringValue);
            }
        } catch (Exception e) {
            log.error(String.format("%s %s Failed to parse field \n%s \nwith schema \n%s", fieldType, fieldType.equals(LegacySQLTypeName.RECORD), fieldValue, fieldSchema));
            throw Lombok.sneakyThrow(e);
        }
        parent.set(fieldSchema.getName(), baseJsonNode);
    }
    
    private ValueNode timestampFieldToJsonNode(FieldValue fieldValue) {
        if (fieldValue.isNull()) {
            return null;
        } else {
            long timestamp = fieldValue.getTimestampValue();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(getInstantFromMicros(timestamp), ZoneOffset.UTC);
            String formattedTime = dateTimeFormatter.format(zonedDateTime);
            return TextNode.valueOf(formattedTime);
        }
    }
    
    static Instant getInstantFromMicros(Long microsSinceEpoch) {
        return Instant.ofEpochSecond(TimeUnit.MICROSECONDS.toSeconds(microsSinceEpoch),
                                     TimeUnit.MICROSECONDS.toNanos(Math.floorMod(microsSinceEpoch, TimeUnit.SECONDS.toMicros(1))));
    }
    
    /**
     * Splits a SQL script into its statements. Take this example: <br>
     * <pre>
     *     INSERT INTO a (ID, VALUE) VALUES (1, 'A');
     *     SELECT * FROM a;
     * </pre>
     * The method would return two statements: <code>INSERT INTO a (ID, VALUE) VALUES (1, 'A')</code> and <code>SELECT * FROM a</code>.
     * <p>
     * This method also strips away all comments from the script, leaving only the actual SQL behind.
     *
     * @param script The SQL script as a string.
     * @return a list of SQL statements.
     */
    private List<String> splitScriptIntoStatements(@NonNull String script) {
        List<String> statements = new ArrayList<>();
        if (script.length() == 0) {
            return statements;
        }
        
        StringBuilder sb = new StringBuilder();
        
        // whether we're in the middle of a ', " or `
        boolean inConstant = false;
        // the character bordering the constant
        char constantStart = 0;
        
        // if we have started a -- comment in code
        boolean dashDashStarted = false;
        // if we activated single line comment
        boolean inSingleLineComment = false;
        
        // whether we just started /* or */
        boolean slashStarStarted = false;
        
        // if we activated multi line comment
        boolean inMultiLineComment = false;
        
        for (char token : script.toCharArray()) {
            if (inConstant) {
                // if we're in a constant, output no matter what; at the end, clear flag
                sb.append(token);
                if (token == constantStart) {
                    inConstant = false;
                    constantStart = 0;
                }
            } else if (inSingleLineComment) {
                // if we are at the end of the line, clear flag; otherwise don't output anything
                if (token == '\n') {
                    sb.append(token);
                    inSingleLineComment = false;
                }
            } else if (inMultiLineComment) {
                if (slashStarStarted) {
                    // clear this flag no matter what
                    slashStarStarted = false;
                    if (token == '/') {
                        // detected '*/' -> stop comment mode
                        inMultiLineComment = false;
                        sb.append("\n");
                    }
                }
                if (token == '*') {
                    // might be a start of '*/'
                    slashStarStarted = true;
                }
            } else if (dashDashStarted) {
                // clear flag no matter what
                dashDashStarted = false;
                if (token == '-') {
                    // '--' detected -> comment
                    inSingleLineComment = true;
                } else {
                    // not '--' -> have to output the previous '-' as well as this token
                    sb.append('-').append(token);
                }
            } else if (slashStarStarted) {
                // clear flag no matter what
                slashStarStarted = false;
                if (token == '*') {
                    // '/*' detected; multi line comment
                    inMultiLineComment = true;
                } else {
                    // not '/*' -> have to output '/' and current token
                    sb.append('/').append(token);
                }
            } else {
                // at this point we can assume all flags are false; any flag should be handled prior to this branch
                switch (token) {
                    case '-':
                        dashDashStarted = true;
                        break;
                    case '#':
                        inSingleLineComment = true;
                        break;
                    case '/':
                        slashStarStarted = true;
                        break;
                    case '\'':
                    case '"':
                    case '`':
                        inConstant = true;
                        constantStart = token;
                        sb.append(token);
                        break;
                    case ';':
                        // save this statement, and start a new one
                        String statement = sb.toString().trim();
                        if (statement.length() > 0) {
                            statements.add(statement);
                        }
                        sb.setLength(0);
                        break;
                    default:
                        sb.append(token);
                        break;
                }
            }
        }
        
        String statement = sb.toString().trim();
        if (statement.length() > 0) {
            statements.add(statement);
        }
        
        return statements;
    }
}
