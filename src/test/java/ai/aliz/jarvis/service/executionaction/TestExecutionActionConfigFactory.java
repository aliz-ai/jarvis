package ai.aliz.jarvis.service.executionaction;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

import ai.aliz.jarvis.context.TestContext;
import ai.aliz.jarvis.context.TestContextType;
import ai.aliz.jarvis.testconfig.ExecutionActionConfig;
import ai.aliz.jarvis.testconfig.ExecutionActionConfigFactory;
import ai.aliz.jarvis.testconfig.ExecutionType;
import com.google.gson.Gson;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ai.aliz.jarvis.context.TestContextLoader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@TestPropertySource(properties = "context=src/test/resources/test_context.json")
@ContextConfiguration(classes = {ExecutionActionConfigFactory.class})
public class TestExecutionActionConfigFactory {

    @MockBean
    private TestContextLoader contextLoader;

    @Autowired
    private ExecutionActionConfigFactory executionActionConfigFactory;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private TestContext createDummyTestContext(){
        return TestContext.builder().parameter("repositoryRoot", "test/project").build();
    }

    private List<ExecutionActionConfig> readExecutionActionConfigs(String testSuiteJsonPath){
        Mockito.when(contextLoader.getContext(Mockito.eq("local"))).thenReturn(createDummyTestContext());
        InputStream resourceAsStream = getClass().getResourceAsStream(testSuiteJsonPath);
        Gson gson = new Gson();
        Map map = gson.fromJson(new InputStreamReader(resourceAsStream), Map.class);
        List<ExecutionActionConfig> executionActionConfigs = executionActionConfigFactory.getExecutionActionConfig(map);
        return executionActionConfigs;
    }

    @Test
    public void testValidExecutionActionConfig(){
        List<ExecutionActionConfig> executionActionConfigs = this.readExecutionActionConfigs("/config/valid.json");
        assertEquals(1, executionActionConfigs.size());
        ExecutionActionConfig executionActionConfig = executionActionConfigs.get(0);
        assertThat(executionActionConfig.getProperties().get("sourcePath"), is("test/project/src/test/resources/sample_tests/test_sql/test.sql"));
        assertThat(executionActionConfig.getType(), is(ExecutionType.BqQuery));
        assertNull(executionActionConfig.getDescriptorFolder());
        assertThat(executionActionConfig.getExecutionContext(), is("test_bq"));
    }

    @Test
    public void testBqQueryExecutionType(){
        List<ExecutionActionConfig> executionActionConfigs = this.readExecutionActionConfigs("/config/valid.json");
        ExecutionActionConfig executionActionConfig = executionActionConfigs.get(0);
        executionActionConfig.setType(ExecutionType.valueOf("BqQuery"));
        assertThat(executionActionConfig.getProperties().get("sourcePath"), is("test/project/src/test/resources/sample_tests/test_sql/test.sql"));
        assertThat(executionActionConfig.getExecutionContext(), is("test_bq"));
    }

    @Test
    public void testOtherExecutionType(){
        List<ExecutionActionConfig> executionActionConfigs = this.readExecutionActionConfigs("/config/valid.json");
        ExecutionActionConfig executionActionConfig = executionActionConfigs.get(0);
        exceptionRule.expect(IllegalArgumentException.class);
        executionActionConfig.setType(ExecutionType.valueOf("SQL"));
    }
}
