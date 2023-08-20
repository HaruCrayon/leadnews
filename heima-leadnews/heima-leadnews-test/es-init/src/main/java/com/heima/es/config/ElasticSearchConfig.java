package com.heima.es.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiJing
 * @version 1.0
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "es")
public class ElasticSearchConfig {
    private String host;
    private int port;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClient client = RestClient.builder(new HttpHost(host, port, "http")).build();
        ElasticsearchTransport transport = new RestClientTransport(client, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

}
