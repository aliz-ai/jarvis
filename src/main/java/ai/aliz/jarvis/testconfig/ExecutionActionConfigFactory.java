package ai.aliz.jarvis.testconfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.aliz.jarvis.context.TestContextLoader;

import static ai.aliz.talendtestrunner.helper.Helper.SOURCE_PATH;

@Service
public class ExecutionActionConfigFactory {
    
    public static final String EXECUTIONS_KEY = "executions";
    
    @Autowired
    private TestContextLoader contextLoader;
    
    public List<ExecutionActionConfig> getExecutionActionConfig(Map testSuiteMap) {
        Object executeActionJson = testSuiteMap.get(EXECUTIONS_KEY);
        if (executeActionJson != null) {
            List<Map<String, String>> executionActions = (List<Map<String, String>>) testSuiteMap.getOrDefault(EXECUTIONS_KEY, Collections.singletonMap("type", "noOps"));
            List<ExecutionActionConfig> executionActionConfigs = getExecutionActionConfigs(executionActions);
            return executionActionConfigs;
        }
        
        return new ArrayList<>();
    }
    
    private List<ExecutionActionConfig> getExecutionActionConfigs(List<Map<String, String>> executions) {

        String repositoryRoot = moderateFilePathSlashes(contextLoader.getContext("local").getParameter("repositoryRoot"));
        return executions.stream().map(e -> {
                    List<ExecutionActionConfig> executionActionConfigs;
                    ExecutionActionConfig executionActionConfig = new ExecutionActionConfig();
                    ExecutionType executionType = ExecutionType.valueOf(checkExecutionType(e.get("executionType")));
                    switch (executionType){
                        case BqQuery:
                            executionActionConfigs =  Lists.newArrayList();
                            String newQueryPathName = "." + e.get("queryPath");
                            File queryPath = new File(newQueryPathName);
                            String executionContext = e.get("executionContext");
                            if (contextLoader == null){
                                Objects.requireNonNull(e, "Context must not be null.");
                            }
                            else if (!queryPath.isFile()){
                                throw new NullPointerException("This query path doesn't exist.");
                            }
                            else if (executionContext.isEmpty()){
                                Objects.requireNonNull(e, "Execution context must not be null.");
                            }
                            else if (!existsAValidExecutionContext(queryPath, executionContext)){
                                throw new IllegalArgumentException("Invalid execution type or illegal folder syntax.");
                            }else{
                                executionActionConfig.setType(ExecutionType.valueOf(checkExecutionType(e.get("executionType"))));
                                executionActionConfig.getProperties().put(SOURCE_PATH, repositoryRoot + e.get("queryPath"));
                                executionActionConfig.setExecutionContext(Objects.requireNonNull(e.get("executionContext"), "executionContext property must be specified on BqQuery executions"));
                                executionActionConfigs.add(executionActionConfig);
                                break;
                            }
                        case Talend:
                        case Airflow:
                        default:
                               throw new UnsupportedOperationException("This execution type isn't supported.");
                             }
                             return executionActionConfig;
                         })
                         .collect(Collectors.toList());
    }

    private boolean existsAValidExecutionContext(File queryPath, String executionContext){
        File parentFile = queryPath.getParentFile();
        boolean exists = false;
        File[] listOfFiles = parentFile.listFiles();
        for (File file : listOfFiles) {
            if (file.getName().contains("case")) {
                File[] listOfFilesInCaseFolder = file.listFiles();
                for (File executionContextFile : listOfFilesInCaseFolder){
                    File[] listOfFolders = executionContextFile.listFiles();
                    for (File folder : listOfFolders){
                        if (folder.getName().contains(executionContext)){
                            exists = true;
                            break;
                        }
                    }
                }
                break;
            }
        }
        return exists;
    }
    
    private String moderateFilePathSlashes(String path) {
        path.replace('\\', File.separatorChar);
        path.replace('/', File.separatorChar);
        
        return path;
    }
    
    private String checkExecutionType(String executionType) {
        try {
            ExecutionType.valueOf(executionType);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Execution type %s dose not exists", executionType));
        }
        return executionType;
    }
    
    
    
    
}
