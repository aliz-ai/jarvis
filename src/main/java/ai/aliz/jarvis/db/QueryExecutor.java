package ai.aliz.jarvis.db;

import ai.aliz.jarvis.context.TestContext;

public interface QueryExecutor {
    
    void executeStatement(String query, TestContext context);
    
    String executeQuery(String query, TestContext context);
    
    void executeScript(String query, TestContext context);
}
