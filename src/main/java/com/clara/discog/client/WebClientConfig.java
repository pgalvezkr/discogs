package com.clara.discog.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;

@Configuration
public class WebClientConfig {

    @Value("${oauth_consumer_key}")
    private String consumerKey;
    @Value("${oauth_token}")
    private String tokenKey;
    @Value("${oauth_signature}")
    private String signatureKey;
    @Value("${oauth_signature_method}")
    private String signatureMethod;
    @Value("${oauth_nonce}")
    private String nonceKey;
    @Value("${discogs_url}")
    private String url;


    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl(url)
                .defaultHeader("Authorization", getAuthorizationHeader())
                .build();
    }

    private String getAuthorizationHeader() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return "OAuth oauth_consumer_key=\"" + consumerKey + "\", oauth_nonce=\"" + nonceKey +
                "\", oauth_token=\"" + tokenKey + "\", oauth_signature=\"" + signatureKey + "\", oauth_signature_method=\"" + signatureMethod +
                "\", oauth_timestamp=\"" + timestamp.getTime() + "\"";
    }
}
