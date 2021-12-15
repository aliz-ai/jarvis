package ai.aliz.jarvis.service.executionaction;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import ai.aliz.jarvis.testconfig.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ai.aliz.jarvis.context.TestContextLoader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

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

    @Test
    public void testInvalidExecutionActionConfig(){
        InputStream resourceAsStream = getClass().getResourceAsStream("/sample_tests/test_sql/invalid.json");
        Gson gson = new Gson();
        exceptionRule.expect(NullPointerException.class);
        Map map = gson.fromJson(new InputStreamReader(resourceAsStream), Map.class);
    }

    @Test
    public void testInvalidParametersExecutionActionConfig(){
        InputStream resourceAsStream = getClass().getResourceAsStream("/config/invalid.json");
        Gson gson = new Gson();
        Map map = gson.fromJson(new InputStreamReader(resourceAsStream), Map.class);
        List<ExecutionActionConfig> executionActionConfigs = executionActionConfigFactory.getExecutionActionConfig(map);
        assertEquals(1, executionActionConfigs.size());
        ExecutionActionConfig executionActionConfig = executionActionConfigs.get(0);
        assertThat(executionActionConfig.getExecutionContext(), not("test_bq"));
    }
}
