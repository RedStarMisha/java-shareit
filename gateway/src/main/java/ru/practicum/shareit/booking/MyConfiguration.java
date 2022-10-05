package ru.practicum.shareit.booking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class MyConfiguration {

    @Bean
    @Scope("prototype")
    public RestTemplate makeRestTemplate() {
        RestTemplate rest = new RestTemplate();
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return rest;
    }
}
