package ai.aliz.jarvis.testconfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

import ai.aliz.jarvis.context.TestContext;
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

    private List<ExecutionActionConfig> readExecutionActionConfigs(String testSuiteJsonPath){
        Mockito.when(contextLoader.getContext(Mockito.eq("local"))).thenReturn(createDummyTestContext());
        InputStream resourceAsStream = getClass().getResourceAsStream(testSuiteJsonPath);
        Gson gson = new Gson();
        Map map = gson.fromJson(new InputStreamReader(resourceAsStream), Map.class);
        List<ExecutionActionConfig> executionActionConfig = executionActionConfigFactory.getExecutionActionConfig(map);
        return executionActionConfig;
    }

    private List<ExecutionActionConfig> readExecutionActionConfigsWithBadContext(String testSuiteJsonPath){
        Mockito.when(contextLoader.getContext(Mockito.eq("invalid"))).thenReturn(createDummyTestContext());
        InputStream resourceAsStream = getClass().getResourceAsStream(testSuiteJsonPath);
        Gson gson = new Gson();
        Map map = gson.fromJson(new InputStreamReader(resourceAsStream), Map.class);
        List<ExecutionActionConfig> executionActionConfig = executionActionConfigFactory.getExecutionActionConfig(map);
        return executionActionConfig;
    }

    private ExecutionActionConfig getOneExecutionActionConfig(String testSuiteJsonPath){
        List<ExecutionActionConfig> executionActionConfigs = this.readExecutionActionConfigs(testSuiteJsonPath);
        ExecutionActionConfig executionActionConfig = executionActionConfigs.get(0);
        if (executionActionConfigs.size()==1) {
            return executionActionConfig;
        }else{
            return null;
        }
    }

    private ExecutionActionConfig getOneExecutionActionConfigWithBadContext(String testSuiteJsonPath){
        List<ExecutionActionConfig> executionActionConfigs = this.readExecutionActionConfigsWithBadContext(testSuiteJsonPath);
        ExecutionActionConfig executionActionConfig = executionActionConfigs.get(0);
        if (executionActionConfigs.size()==1) {
            return executionActionConfig;
        }else{
            return null;
        }
    }

    @Test
    public void testValidExecutionActionConfig(){
        ExecutionActionConfig executionActionConfig = getOneExecutionActionConfig("/execution/valid.json");
        assertThat(executionActionConfig.getProperties().get("sourcePath"), is("test/project/src/test/resources/sample_tests/test_sql/test.sql"));
        assertThat(executionActionConfig.getType(), is(ExecutionType.BqQuery));
        assertNull(executionActionConfig.getDescriptorFolder());
        assertThat(executionActionConfig.getExecutionContext(), is("test_bq"));
    }

    @Test
    public void testBqQueryExecutionType(){
        ExecutionActionConfig executionActionConfig = getOneExecutionActionConfig("/execution/valid.json");
        assertThat(executionActionConfig.getType(), is(ExecutionType.BqQuery));
        assertThat(executionActionConfig.getProperties().get("sourcePath"), is("test/project/src/test/resources/sample_tests/test_sql/test.sql"));
        assertThat(executionActionConfig.getExecutionContext(), is("test_bq"));
    }

    @Test
    public void testOtherExecutionType(){
        ExecutionActionConfig executionActionConfig = getOneExecutionActionConfig("/execution/valid.json");
        exceptionRule.expect(AssertionError.class);
        assertThat(executionActionConfig.getType(), is("SQL"));
    }

    @Test
    public void testWithoutQueryPath(){
        ExecutionActionConfig executionActionConfig = getOneExecutionActionConfig("/execution/invalidWithoutQueryPath.json");
        exceptionRule.expect(AssertionError.class);
        assertThat(executionActionConfig.getProperties().get("sourcePath"), is("test/project/src/test/resources/sample_tests/test_sql/test.sql"));
    }

    @Test
    public void testWithoutExecutionType(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = getOneExecutionActionConfig("/execution/invalidWithoutExecutionType.json");
    }

    @Test
    public void testWithoutExecutionContext(){
        exceptionRule.expect(NullPointerException.class);
        ExecutionActionConfig executionActionConfig = getOneExecutionActionConfig("/execution/invalidWithoutExecutionContext.json");
    }

    @Test
    public void testBadQueryPath(){
        ExecutionActionConfig executionActionConfig = getOneExecutionActionConfig("/execution/invalidBadQueryPath.json");
        exceptionRule.expect(AssertionError.class);
        assertThat(executionActionConfig.getProperties().get("sourcePath"), is("test/project/src/test/resources/sample_tests/test_sql/test.sql"));
    }

    @Test
    public void testBadExecutionType(){
        exceptionRule.expect(RuntimeException.class);
        ExecutionActionConfig executionActionConfig = getOneExecutionActionConfig("/execution/invalidBadExecutionType.json");
    }

    @Test
    public void testBadExecutionContext(){
        ExecutionActionConfig executionActionConfig = getOneExecutionActionConfig("/execution/invalidBadExecutionContext.json");
        exceptionRule.expect(AssertionError.class);
        assertThat(executionActionConfig.getExecutionContext(), is("test_bq"));
    }

    @Test
    public void testReadExecutionActionConfigsWithoutLocalContext(){
        exceptionRule.expect(NullPointerException.class);
        ExecutionActionConfig executionActionConfig = getOneExecutionActionConfigWithBadContext("/execution/valid.json");
    }

    @Test
    public void testNoOpsExecutionType(){
        ExecutionActionConfig executionActionConfig = getOneExecutionActionConfig("/execution/valid.json");
        exceptionRule.expect(AssertionError.class);
        assertThat(executionActionConfig.getType(), is(ExecutionType.Airflow));
    }
}
