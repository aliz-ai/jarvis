package ai.aliz.talendtestrunner.actionConfig;

import ai.aliz.talendtestrunner.IntegrationTestRunner;
import ai.aliz.talendtestrunner.context.ContextLoader;
import ai.aliz.talendtestrunner.service.ActionConfigForBq;
import ai.aliz.talendtestrunner.service.AssertActionConfigCreator;
import ai.aliz.talendtestrunner.service.AssertServiceTest;
import ai.aliz.talendtestrunner.testconfig.AssertActionConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AssertActionConfigTest {

    private ActionConfigForBq actionConfigForBq = new ActionConfigForBq();

    private AssertActionConfigCreator assertActionConfigCreator = new AssertActionConfigCreator();

    @Test
    public void createBqAssertActionConfigTest() {
        Map<String, Object> defaultP = new HashMap<>();
        doTest(defaultP);
    }

    @Test
    public void createBqAssertActionConfigWithDefaultPropertiesTest() {
        Map<String, Object> defaultP = new HashMap<>();
        defaultP.put("assert.assertKeyColumns", "testProperty");
        AssertActionConfig assertActionConfig = doTest(defaultP);

        assertThat(assertActionConfig.getProperties().get("assertKeyColumns"), is("testProperty"));
    }

    @Test
    @SneakyThrows
    public void testCreateAssertActionConfig() {
        Map<String, Object> defaultP = new HashMap<>();
        ContextLoader contextLoader = new ContextLoader(new ObjectMapper());
        String contextPath = new File(Objects.requireNonNull(AssertServiceTest.class.getClassLoader().getResource("test_context.json").getFile())).getPath();
        contextLoader.parseContext(contextPath);
        String configPath = new File(Objects.requireNonNull(AssertServiceTest.class.getClassLoader().getResource("test_structure").getFile())).getPath() + File.separatorChar + "test_json";
        List<AssertActionConfig> assertActionConfigs = assertActionConfigCreator.getAssertActionConfigs(contextLoader, defaultP, new File(configPath));
        AssertActionConfig assertActionConfig = assertActionConfigs.get(0);

        assertThat(assertActionConfig.getProperties().get("sourcePath"), is(configPath + "\\assert\\TEST_ID\\test_dataset\\assertTest.json"));
        assertThat(assertActionConfig.getSystem(), is("TEST_ID"));
        assertThat(assertActionConfig.getType(), is("AssertDataEquals"));
    }

    private AssertActionConfig doTest(Map<String, Object> defaultP) {
        File file = new File(Objects.requireNonNull(AssertServiceTest.class.getClassLoader().getResource("test.json").getFile()));
        AssertActionConfig assertActionConfig = actionConfigForBq.getAssertActionConfigForBq(defaultP, "testSystem", "testDataset", file);

        assertThat(assertActionConfig.getSystem(), is("testSystem"));
        assertThat(assertActionConfig.getType(), is("AssertDataEquals"));
        assertThat(assertActionConfig.getProperties().get("dataset"), is("testDataset"));
        assertThat(assertActionConfig.getProperties().get("sourcePath"), is(file.getPath()));

        return assertActionConfig;
    }
}