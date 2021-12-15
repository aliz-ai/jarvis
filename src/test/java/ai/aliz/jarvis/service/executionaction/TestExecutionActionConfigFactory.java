package ai.aliz.jarvis.service.executionaction;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import ai.aliz.jarvis.testconfig.*;
import ai.aliz.talendtestrunner.service.AssertServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.bigquery.BigQuery;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ai.aliz.jarvis.context.TestContextLoader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static ai.aliz.jarvis.util.JarvisConstants.DATASET;
import static ai.aliz.jarvis.util.JarvisConstants.ENVIRONMENT;
import static ai.aliz.jarvis.util.JarvisConstants.JSON_FORMAT;
import static ai.aliz.jarvis.util.JarvisConstants.NO_METADAT_ADDITION;
import static ai.aliz.jarvis.util.JarvisConstants.PRE;
import static ai.aliz.jarvis.util.JarvisConstants.SOURCE_FORMAT;
import static ai.aliz.jarvis.util.JarvisConstants.SOURCE_PATH;
import static ai.aliz.jarvis.util.JarvisConstants.TABLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(properties = "context=src/test/resources/test_context.json")
@ContextConfiguration(classes = {TestContextLoader.class, ExecutionActionConfigFactory.class})
public class TestExecutionActionConfigFactory {

    @Autowired
    private TestContextLoader contextLoader;

    @Autowired
    private ExecutionActionConfigFactory executionActionConfigFactory;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testValidExecutionActionConfig(){
        InputStream resourceAsStream = getClass().getResourceAsStream("/sample_tests/test_sql/testSuite.json");

        Gson gson = new Gson();
        Map map = gson.fromJson(new InputStreamReader(resourceAsStream), Map.class);
        List<ExecutionActionConfig> executionActionConfigs = executionActionConfigFactory.getExecutionActionConfig(map);
        assertEquals(1, executionActionConfigs.size());
        ExecutionActionConfig executionActionConfig = executionActionConfigs.get(0);
        assertThat(executionActionConfig.getProperties().get("sourcePath"), is("C:\\test\\project/src/test/resources/sample_tests/test_sql/test.sql"));
        assertThat(executionActionConfig.getType(), is(ExecutionType.BqQuery));
        assertNull(executionActionConfig.getDescriptorFolder());
        assertThat(executionActionConfig.getExecutionContext(), is("test_bq"));
        assertTrue(executionActionConfig.getAssertActionConfigs().isEmpty());
    }

//    @Test
    public void testGetExecutionActionConfig(){
        String contextPath = new File(Objects.requireNonNull(AssertServiceTest.class.getClassLoader().getResource("test_context.json").getFile())).getPath();
        List<Map<String, String>> executions = new ArrayList<>();
        Map<String, String> execution = new HashMap<>();

        executions.add(execution);
        List<ExecutionActionConfig> executionActionConfigs = executionActionConfigFactory.getExecutionActionConfig(execution);
        ExecutionActionConfig executionActionConfig = executionActionConfigs.get(0);
        assertThat(executionActionConfig.getProperties().get("sourcePath"), is("C:\\test\\project\\test\\test.json"));
        assertThat(executionActionConfig.getType(), is(ExecutionType.BqQuery));
        assertThat(executionActionConfig.getExecutionContext(), is("TEST"));
    }

//    @Test
//    public void testExecutorListCreation() {
//        contextLoader = new TestContextLoader(new Environment<TestContextLoader(new HashMap<>())>(ENVIRONMENT));
//        String contextPath = new File(Objects.requireNonNull(AssertServiceTest.class.getClassLoader().getResource("test_context.json").getFile())).getPath();
//        List<Map<String, String>> executions = new ArrayList<>();
//        Map<String, String> execution = new HashMap<>();
//        execution.put("executionType", "BqQuery");
//        execution.put("queryPath", "\\test\\test.json");
//        execution.put("executionContext", "TEST");
//        executions.add(execution);
////
//        List<ExecutionActionConfig> executionActionConfigs = executionActionConfigFactory.getExecutionActionConfig(execution);
////
//        ExecutionActionConfig executionActionConfig = executionActionConfigs.get(0);
//        assertThat(executionActionConfig.getProperties().get("sourcePath"), is("C:\\test\\project\\test\\test.json"));
//        assertThat(executionActionConfig.getType(), is(ExecutionType.BqQuery));
//        assertThat(executionActionConfig.getExecutionContext(), is("TEST"));
//    }
}
