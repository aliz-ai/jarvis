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

    private List<ExecutionActionConfig> readExecutionActionConfigs(String context, String testSuiteJsonPath){
        Mockito.when(contextLoader.getContext(Mockito.eq(context))).thenReturn(createDummyTestContext());
        InputStream resourceAsStream = getClass().getResourceAsStream(testSuiteJsonPath);
        Gson gson = new Gson();
        Map map = gson.fromJson(new InputStreamReader(resourceAsStream), Map.class);
        List<ExecutionActionConfig> executionActionConfig = executionActionConfigFactory.getExecutionActionConfig(map);
        return executionActionConfig;
    }

    private List<ExecutionActionConfig> readWithLocalContext(String testSuiteJsonPath){
        return readExecutionActionConfigs("local", testSuiteJsonPath);
    }

    private List<ExecutionActionConfig> readWithInvalidContext(String testSuiteJsonPath){
        return readExecutionActionConfigs("invalid", testSuiteJsonPath);
    }

    private ExecutionActionConfig getSingleExecutionActionConfig(String testSuiteJsonPath){
        List<ExecutionActionConfig> executionActionConfigs = this.readWithLocalContext(testSuiteJsonPath);
        return Iterables.getOnlyElement(executionActionConfigs);
    }

    private ExecutionActionConfig getSingleExecutionActionConfigWithBadContext(String testSuiteJsonPath){
        List<ExecutionActionConfig> executionActionConfigs = this.readWithInvalidContext(testSuiteJsonPath);
        return Iterables.getOnlyElement(executionActionConfigs);
    }

    @Test
    public void testValidExecutionActionConfig(){
        ExecutionActionConfig executionActionConfig = getSingleExecutionActionConfig("/execution/valid.json");
        assertThat(executionActionConfig.getProperties().get("sourcePath"), is("test/project/src/test/resources/sample_tests/test_sql/test.sql"));
        assertThat(executionActionConfig.getType(), is(ExecutionType.BqQuery));
        assertNull(executionActionConfig.getDescriptorFolder());
        assertThat(executionActionConfig.getExecutionContext(), is("test_bq"));
    }

    @Test
    public void testAirflowExecutionType(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = getSingleExecutionActionConfig("/execution/airflow.json");
        assertThat(executionActionConfig.getType(), is(ExecutionType.Airflow));
    }

    @Test
    public void testWithoutQueryPath(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = getSingleExecutionActionConfig("/execution/invalidWithoutQueryPath.json");
    }

    @Test
    public void testWithoutExecutionType(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = getSingleExecutionActionConfig("/execution/invalidWithoutExecutionType.json");
    }

    @Test
    public void testWithoutExecutionContext(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = getSingleExecutionActionConfig("/execution/invalidWithoutExecutionContext.json");
    }

    @Test
    public void testBadQueryPath(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = getSingleExecutionActionConfig("/execution/invalidBadQueryPath.json");
    }

    @Test
    public void testBadExecutionType(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = getSingleExecutionActionConfig("/execution/invalidBadExecutionType.json");
    }

    @Test
    public void testBadExecutionContext(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = getSingleExecutionActionConfig("/execution/invalidBadExecutionContext.json");
    }

    @Test
    public void testReadExecutionActionConfigsWithoutLocalContext(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = getSingleExecutionActionConfigWithBadContext("/execution/valid.json");
    }
}