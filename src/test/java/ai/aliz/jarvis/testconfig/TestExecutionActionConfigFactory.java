package ai.aliz.jarvis.testconfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

import ai.aliz.jarvis.context.TestContext;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ai.aliz.jarvis.context.TestContextLoader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
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

    private List<ExecutionActionConfig> readExecutionAction(String context, String testSuiteJsonPath){
        Mockito.when(contextLoader.getContext(Mockito.eq(context))).thenReturn(createDummyTestContext());
        InputStream resourceAsStream = getClass().getResourceAsStream(testSuiteJsonPath);
        Gson gson = new Gson();
        Map map = gson.fromJson(new InputStreamReader(resourceAsStream), Map.class);
        List<ExecutionActionConfig> executionActionConfig = executionActionConfigFactory.getExecutionActionConfig(map);
        return executionActionConfig;
    }

    private List<ExecutionActionConfig> readExecutionActionConfigs(String testSuiteJsonPath){
        return readExecutionAction("local", testSuiteJsonPath);
    }

    private List<ExecutionActionConfig> readExecutionActionConfigsWithBadContext(String testSuiteJsonPath){
        return readExecutionAction("invalid", testSuiteJsonPath);
    }

    private ExecutionActionConfig singleExecutionActionConfig(String testSuiteJsonPath){
        List<ExecutionActionConfig> executionActionConfigs = this.readExecutionActionConfigs(testSuiteJsonPath);
        ExecutionActionConfig executionActionConfig = executionActionConfigs.get(0);
        return Iterables.getOnlyElement(executionActionConfigs);
    }

    private ExecutionActionConfig singleExecutionActionConfigWithBadContext(String testSuiteJsonPath){
        List<ExecutionActionConfig> executionActionConfigs = this.readExecutionActionConfigsWithBadContext(testSuiteJsonPath);
        ExecutionActionConfig executionActionConfig = executionActionConfigs.get(0);
        return Iterables.getOnlyElement(executionActionConfigs);
    }

    @Test
    public void testValidExecutionActionConfig(){
        ExecutionActionConfig executionActionConfig = singleExecutionActionConfig("/execution/valid.json");
        assertThat(executionActionConfig.getProperties().get("sourcePath"), is("test/project/src/test/resources/sample_tests/test_sql/test.sql"));
        assertThat(executionActionConfig.getType(), is(ExecutionType.BqQuery));
        assertNull(executionActionConfig.getDescriptorFolder());
        assertThat(executionActionConfig.getExecutionContext(), is("test_bq"));
    }

    @Test
    public void testAirflowExecutionType(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = singleExecutionActionConfig("/execution/airflow.json");
    }

    @Test
    public void testWithoutQueryPath(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = singleExecutionActionConfig("/execution/invalidWithoutQueryPath.json");
    }

    @Test
    public void testWithoutExecutionType(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = singleExecutionActionConfig("/execution/invalidWithoutExecutionType.json");
    }

    @Test
    public void testWithoutExecutionContext(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = singleExecutionActionConfig("/execution/invalidWithoutExecutionContext.json");
    }

    @Test
    public void testBadQueryPath(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = singleExecutionActionConfig("/execution/invalidBadQueryPath.json");
    }

    @Test
    public void testBadExecutionType(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = singleExecutionActionConfig("/execution/invalidBadExecutionType.json");
    }

    @Test
    public void testBadExecutionContext(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = singleExecutionActionConfig("/execution/invalidBadExecutionContext.json");
    }

    @Test
    public void testReadExecutionActionConfigsWithoutLocalContext(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = singleExecutionActionConfigWithBadContext("/execution/valid.json");
    }

    @Test
    public void testAirFlowExecutionType(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = singleExecutionActionConfig("/execution/valid.json");
    }
}