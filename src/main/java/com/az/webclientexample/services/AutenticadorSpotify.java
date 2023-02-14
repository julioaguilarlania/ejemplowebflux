package com.az.webclientexample.services;


import com.az.webclientexample.dtos.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.client.WebClient.*;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Component
public class AutenticadorSpotify {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutenticadorSpotify.class);
    @Value("${spotify.auth_url:https://accounts.spotify.com/api/token}")
    private String authUrl;

    @Value("${spotify.client_id}")
    private String clientId;

    @Value("${spotify.client_secret}")
    private String clientSecret;

    AutenticadorSpotify() {
        LOGGER.debug("ServicioSpotify()");
    }

    private Token authToken;
    public Token getAccessToken() {
        LOGGER.debug("getAccessToken()");
        if (this.authToken== null || this.authToken.isExpired()) {
            this.authToken = obtenerNuevoAccessToken();
        }
        return authToken;
    }


    private Token obtenerNuevoAccessToken() {
        LOGGER.debug("obtenerNuevoAccessToken()");

        String b64Codes = Base64
                .getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());
        LOGGER.trace("B64 codes: " + b64Codes + ", " + b64Codes.length() + " chars");
        WebClient client = WebClient.create("https://accounts.spotify.com/api");
        ResponseSpec respSepc = client
                .method(HttpMethod.POST)
                .uri("/token")
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .header(HttpHeaders.AUTHORIZATION, "Basic " + b64Codes)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve();
//        Mono<String> response = respSepc.bodyToMono(String.class);
//        LOGGER.trace(response.share().block());
        Mono<Token> response = respSepc.bodyToMono(Token.class);
        Token t = new Token();
        t.registrarToken(response.share().block());
        LOGGER.trace(t.toString());
        return t;
    }
}
