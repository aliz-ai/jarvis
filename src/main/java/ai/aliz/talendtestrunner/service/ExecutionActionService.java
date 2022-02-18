package ai.aliz.talendtestrunner.service;

import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ai.aliz.jarvis.context.TestContextLoader;
import ai.aliz.talendtestrunner.config.RetryConfig;
import ai.aliz.talendtestrunner.util.PlaceholderResolver;

@Service
public class ExecutionActionService {


    public void run(TestContextLoader contextLoader, String taskName) {
        RetryConfig retryConfig = new RetryConfig();
        RetryTemplate retryTemplate = retryConfig.retryTemplate(retryConfig.retryPolicy(), retryConfig.backOffPolicy());
        TalendApiService talendApiService = new TalendApiService(new PlaceholderResolver(), new RestTemplate(), retryTemplate);
        talendApiService.executeTask(taskName, contextLoader.getContext("TalendAPI"));
    }
}
