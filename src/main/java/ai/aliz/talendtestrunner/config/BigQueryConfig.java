package ai.aliz.talendtestrunner.config;

import ai.aliz.talendtestrunner.service.BigQueryService;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BigQueryConfig {

    @Bean
    public BigQuery bigQuery() {
        return BigQueryOptions.getDefaultInstance().getService();
    }

    @Bean
    public BigQueryService bigQueryService() {
        return new BigQueryService();
    }
}
