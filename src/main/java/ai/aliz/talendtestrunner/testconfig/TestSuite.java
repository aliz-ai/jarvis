package ai.aliz.talendtestrunner.testconfig;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import ai.aliz.talendtestrunner.context.ContextLoader;
import ai.aliz.talendtestrunner.service.AssertActionConfigCreator;
import ai.aliz.talendtestrunner.service.ExecutionActionConfigCreator;
import ai.aliz.talendtestrunner.service.InitActionConfigCreator;

@Data
@ToString(exclude = "parentSuite")
public class TestSuite {
    
    public static final String TEST_SUITE_FILE_NAME = "testSuite.json";
    public static final String EXECUTIONS_KEY = "executions";
    public static final String DEFAULT_PROPERTIES_KEY = "defaultProperties";
    public static final String PROPERTIES_KEY = "properties";
    public static final String CASE_AUTO_DETECT_KEY = "caseAutoDetect";
    public static final String TEST_CASES_KEY = "testCases";
    public static final String SYSTEM = "system";
    public static final String TYPE = "type";

    private static AssertActionConfigCreator assertActionConfigCreator = new AssertActionConfigCreator();

    private static InitActionConfigCreator initActionConfigCreator = new InitActionConfigCreator();

    private static ExecutionActionConfigCreator executionActionConfigCreator = new ExecutionActionConfigCreator();

    private Boolean caseAutoDetect;

    private final List<TestCase> testCases = new ArrayList<>();
    
    private String configPath;
    
    private String rootFolder;
    
    private Map<String, Object> properties;
    
    private TestSuite parentSuite;
    
    private final List<TestSuite> suites = new ArrayList<>();
    
    public List<TestCase> listTestCases() {
        return listTestCases(this);
    }
    
    private List<TestCase> listTestCases(TestSuite suite) {
        System.out.println(suite);
        List<TestCase> testCases = new ArrayList<>();
        
        testCases.addAll(suite.getTestCases());
        
        for (TestSuite testSuite : suite.getSuites()) {
            testCases.addAll(listTestCases(testSuite));
        }
        return testCases;
    }
    
    @SneakyThrows
    public static TestSuite readTestConfig(String testSuitePath, ContextLoader contextLoader) {
        
        File testConfigFile = new File(testSuitePath);
        Preconditions.checkArgument(testConfigFile.isDirectory(), "Testconfig folder %s is not a directory", testSuitePath);
        TestSuite parentSuite;
        Path testSuiteConfigFilePath = Paths.get(testSuitePath, TEST_SUITE_FILE_NAME);
        if (testSuiteConfigFilePath.toFile().exists()) {
            parentSuite = parseFromJson(testSuiteConfigFilePath.toFile(), null, contextLoader);
        } else {
            parentSuite = new TestSuite();
            String descriptorFolder = testSuitePath.endsWith(File.separator) ? testSuitePath : testSuitePath + File.separator;
            parentSuite.setRootFolder(descriptorFolder);
        }
        Path descriptorFolder = testConfigFile.toPath();
        
        Files.list(descriptorFolder).filter(path -> Files.isDirectory(path)).forEach(path -> processFolder(path, parentSuite, contextLoader));
        
        return parentSuite;
    }
    
    @SneakyThrows
    private static void processFolder(Path folder, final TestSuite parentSuite, ContextLoader contextLoader) {
        Preconditions.checkArgument(Files.isDirectory(folder));
        
        TestSuite currentSuit = parentSuite;
        File descriptorFile = Paths.get(folder.toString(), TEST_SUITE_FILE_NAME).toFile();
        if (descriptorFile.exists()) {
            currentSuit = parseFromJson(descriptorFile, parentSuite, contextLoader);
        }
        
        final TestSuite newParent = currentSuit;
        if(currentSuit.getTestCases().isEmpty()) {
            Files.list(folder).filter(path -> Files.isDirectory(path)).forEach(path -> processFolder(path, newParent, contextLoader));
        }

        return;
    }
    
    public static TestSuite parseFromJson(File testConfigFile, TestSuite parentSuite, ContextLoader contextLoader) {
        
        Gson gson = new Gson();
        TestSuite testSuite = new TestSuite();
        testSuite.setParentSuite(parentSuite);
        if (parentSuite != null) {
            parentSuite.getSuites().add(testSuite);
        }
        testSuite.setConfigPath(testConfigFile.getAbsolutePath());
        String descriptorFolder = testConfigFile.getParentFile().getAbsolutePath() + File.separator;
        
        testSuite.setRootFolder(descriptorFolder);
        try (FileReader fileReader = new FileReader(testConfigFile)) {
            Map testSuiteMap = gson.fromJson(fileReader, Map.class);
            
            testSuite.setProperties((Map<String, Object>) testSuiteMap.get(PROPERTIES_KEY));
            
            Boolean caseAutoDetect = (Boolean) testSuiteMap.get(CASE_AUTO_DETECT_KEY);
            testSuite.setCaseAutoDetect(caseAutoDetect);
            
            Map<String, Object> defaultProperties = (Map<String, Object>) testSuiteMap.getOrDefault(DEFAULT_PROPERTIES_KEY, new HashMap<>());

           

            if (Boolean.TRUE.equals(caseAutoDetect)) {
                List<TestCase> testCases = Files.list(Paths.get(descriptorFolder)).filter(Files::isDirectory).map(path -> {
                    File testCaseFolder = path.toFile();
                    TestCase testCase = new TestCase();
                    testCase.setPath(testCaseFolder.getAbsolutePath());
                    testCase.setName(testCaseFolder.getName());


                    List<InitActionConfig> initActions = initActionConfigCreator.getInitActionConfigs(contextLoader, defaultProperties, testCaseFolder);
                    testCase.getInitActionConfigs().addAll(initActions);

                    List<AssertActionConfig> assertActionConfigs = assertActionConfigCreator.getAssertActionConfigs(contextLoader, defaultProperties, testCaseFolder);
                    testCase.getAssertActionConfigs().addAll(assertActionConfigs);
    
                    if (testSuiteMap.get(EXECUTIONS_KEY) != null) {
                        List<Map<String, String>> executionActions = (List<Map<String, String>>) testSuiteMap.getOrDefault(EXECUTIONS_KEY, Collections.singletonMap(TYPE, "noOps"));
                        List<ExecutionActionConfig> executionActionConfigs = executionActionConfigCreator.getExecutionActionConfigs(contextLoader, executionActions);
                        testCase.getExecutionActionConfigs().addAll(executionActionConfigs);
                    }
                    return testCase;
                }).collect(Collectors.toList());
                
                testSuite.getTestCases().addAll(testCases);
            }
            
            List<Map<String, Object>> testCases = (List<Map<String, Object>>) testSuiteMap.get(TEST_CASES_KEY);
            if (testCases != null) {
                setTestCases(testSuite, descriptorFolder, testCases);
            }
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return testSuite;
        
    }

    private static void setTestCases(TestSuite testSuite, String descriptorFolder, List<Map<String, Object>> testCases) {
        for (Map<String, Object> testCaseMap : testCases) {
            TestCase testCase = new TestCase();
            String caseName = (String) testCaseMap.get("name");
            testCase.setName(caseName);

            List<Map<String, Object>> initActions = (List<Map<String, Object>>) testCaseMap.get("initActions");
            for (Map<String, Object> initActionMap : initActions) {
                InitActionConfig initActionConfig = new InitActionConfig();
                initActionConfig.setSystem((String) initActionMap.remove(SYSTEM));
                initActionConfig.setType(InitActionType.valueOf((String) initActionMap.remove(TYPE)));
                initActionConfig.setDescriptorFolder(descriptorFolder + caseName + File.separator);
                initActionConfig.getProperties().putAll(initActionMap);

                testCase.getInitActionConfigs().add(initActionConfig);
            }

            List<Map<String, Object>> assertActions = (List<Map<String, Object>>) testCaseMap.get("assertActions");
            for (Map<String, Object> assertActionMap : assertActions) {
                final String type = (String) assertActionMap.get(TYPE);

                AssertActionConfig assertActionConfig = new AssertActionConfig();
                assertActionConfig.setSystem((String) assertActionMap.remove(SYSTEM));
                assertActionConfig.setType(type);
                assertActionConfig.setDescriptorFolder(descriptorFolder + caseName + File.separator);
                assertActionConfig.setProperties(assertActionMap);
                testCase.getAssertActionConfigs().add(assertActionConfig);
            }

            testSuite.getTestCases().add(testCase);
        }
    }
}
