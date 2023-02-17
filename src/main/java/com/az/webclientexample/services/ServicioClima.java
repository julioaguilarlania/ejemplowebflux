package com.az.webclientexample.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.client.WebClient.*;

@Service
public class ServicioClima {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServicioClima.class);

    @Value("${clima.api_url}")
    String climaApiUrl;

    public Mono<Double> getTemperatura(String nombreCiudad) {
        LOGGER.debug("getTemperatura()");
        try {
            WebClient cliente = WebClient.create(climaApiUrl);
            ResponseSpec respSpec = cliente
                    .method(HttpMethod.GET)
                    .uri("/temperatura")
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve();
            return respSpec.bodyToMono(Double.class);
        } catch(Exception ex) {
            LOGGER.error("Error al recuperar temperatura", ex);
            return Mono.just(-100.0);
        }
    }
}
