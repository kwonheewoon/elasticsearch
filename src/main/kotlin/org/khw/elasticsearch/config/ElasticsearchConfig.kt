package org.khw.elasticsearch.config

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.ElasticsearchTransport
import co.elastic.clients.transport.rest_client.RestClientOptions
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ElasticsearchConfig {

    @Bean
    fun restClientTransport(
        restClient: RestClient?,
        restClientOptions: ObjectProvider<RestClientOptions?>
    ): RestClientTransport {
        return RestClientTransport(restClient, JacksonJsonpMapper(), restClientOptions.getIfAvailable())
    }

    @Bean
    fun restClient(): RestClient {
        // Elasticsearch 서버의 주소를 설정. 여러 노드가 있을 경우, 콤마로 구분하여 추가
        return RestClient.builder(
            HttpHost("localhost", 9200, "http") // 여러 노드를 추가하는 예: new HttpHost("localhost", 9201, "http")
        ).build()
    }

    @Bean
    fun elasticsearchClient(restClient: RestClient?): ElasticsearchClient {
        val mapper = jacksonMapperBuilder()
            .addModule(JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build()

        // RestClient를 기반으로 ElasticsearchTransport를 생성
        val transport: ElasticsearchTransport = RestClientTransport(
            restClient, JacksonJsonpMapper(mapper)
        )

        // ElasticsearchTransport를 사용하여 ElasticsearchClient 인스턴스를 생성
        return ElasticsearchClient(transport)
    }
}