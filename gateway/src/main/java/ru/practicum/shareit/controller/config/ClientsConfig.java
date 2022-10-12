package ru.practicum.shareit.controller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.controller.booking.BookingClient;
import ru.practicum.shareit.controller.item.ItemClient;
import ru.practicum.shareit.controller.request.RequestClient;
import ru.practicum.shareit.controller.user.UserClient;

@Configuration
public class ClientsConfig {
    private RestTemplateBuilder builder;

    @Autowired
    public ClientsConfig(RestTemplateBuilder builder) {
        this.builder = builder;
    }

    @Value("${shareit-server.url}")
    private String serverUrl;

    @Bean
    public BookingClient makeBookingClient() {
        String prefix = "/bookings";
        return new BookingClient(makeRestTemplate(prefix));
    }

    @Bean
    public UserClient makeUserClient() {
        String prefix = "/users";
        return new UserClient(makeRestTemplate(prefix));
    }

    @Bean
    public ItemClient makeItemClient() {
        String prefix = "/items";
        return new ItemClient(makeRestTemplate(prefix));
    }

    @Bean
    public RequestClient makeRequestClient() {
        String prefix = "/requests";
        return new RequestClient(makeRestTemplate(prefix));
    }

    private RestTemplate makeRestTemplate(String prefix) {
        return builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + prefix))  //фабрика для построения URI
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)  //фабрика для создания HTTPRequest
                .build();
    }
}
