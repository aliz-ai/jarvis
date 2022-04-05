package ai.aliz.jarvis.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

public interface JarvisConstants {
    //Shared
    String SOURCE_PATH = "sourcePath";
    String SOURCE_FORMAT = "sourceFormat";
    String JARVIS_INIT = "jarvis_init";
    String SFTP = "sftp";
    
    //Extensions
    String JSON_FORMAT = "json";
    String BQL_FORMAT = "bql";
    String SQL_FORMAT = "sql";
    
    //Application parameters
    /* absolute path to the json file describing the contexts */
    String CONTEXT = "context";
    
    /* TODO doc */
    String CONFIG = "config";
    
    /* Parameters excluded in this lists will be handled as "additional parameters" and will be inserted/override in the environment parameter map*/
    Set<String> BASE_PARAMETERS = Stream.of(CONTEXT, CONFIG)
                                        .collect(Collectors.toSet());
    
    /* If a jarvis test is triggered by each commit we need to use a tablePostfix to resolve conflicting BQ tables.
    If the application is added to another repo as a jar and we want to use for example git commit information in the postfix, the easiest was is to pass it as a parameter
    */
    
    String TABLE_POSTFIX = "tablePostfix";
    
    //Context
    String ID = "id";
    String CONTEXT_TYPE = "contextType";
    String PARAMETERS = "parameters";
    
    //Folder
    String PRE = "pre";
    
    //MxSQL/SFTP
    String HOST = "host";
    String USER = "user";
    String PASSWORD = "password";
    String PORT = "port";
    String DATABASE = "database";
    String REMOTE_BASE_PATH = "remoteBasePath";
    
    //BQ
    String PROJECT = "project";
    String TABLE = "table";
    String DATASET = "dataset";
    String DATASET_NAME_PREFIX = "datasetNamePrefix";
    String NO_METADAT_ADDITION = "noMetadatAddition";
    String ASSERT_KEY_COLUMNS = "assertKeyColumns";
    String EXCLUDE_PREVIOUSLY_INSERTED_ROWS = "excludePreviouslyInsertedRows";
    
    //Local
    String REPOSITORY_ROOT = "repositoryRoot";
    
    //Talend
    String API_URL = "apiUrl";
    String API_KEY = "apiKey";
    String ENVIRONMENT = "environment";
    String WORKSPACE = "workspace";
}
