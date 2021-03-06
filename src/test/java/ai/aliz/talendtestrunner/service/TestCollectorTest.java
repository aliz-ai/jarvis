package ai.aliz.talendtestrunner.service;

import ai.aliz.talendtestrunner.testcase.TestCase;
import ai.aliz.talendtestrunner.util.TestCollector;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Ignore
public class TestCollectorTest {

    @Autowired
    private TestCollector testCollector;

    @Test
    public void listTestCases() {
        List<TestCase> testCases = testCollector.listTestCases();

        System.out.println(testCases.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ")));
    }

}
