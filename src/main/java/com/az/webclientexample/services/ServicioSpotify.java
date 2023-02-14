package com.az.webclientexample.services;

import com.az.webclientexample.dtos.Token;
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

import javax.annotation.PostConstruct;

@Service
public class ServicioSpotify {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServicioSpotify.class);

    AutenticadorSpotify autenticador;

    @Value("${spotify.api_base_url}")
    String baseUrl;

    public ServicioSpotify(AutenticadorSpotify autenticador) {
        this.autenticador = autenticador;
    }

    @PostConstruct
    void cargarListas() {
        Token token = this.autenticador.getAccessToken();
        WebClient cliente = WebClient.create(this.baseUrl);
        String genero = "pop";
        ResponseSpec respSpec = cliente
                .method(HttpMethod.GET)
                .uri(ub -> ub
                        .path("/search")
                        .queryParam("q", "genre:"+genero)
                        .queryParam("type", "track")
                        .build()
                )
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve();
        Mono<String> response = respSpec.bodyToMono(String.class);
        LOGGER.debug(response.share().block());
    }
}
