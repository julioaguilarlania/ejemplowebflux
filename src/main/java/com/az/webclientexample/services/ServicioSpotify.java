package com.az.webclientexample.services;

import com.az.webclientexample.controllers.Genero;
import com.az.webclientexample.dtos.Token;
import com.az.webclientexample.dtos.Track;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.client.WebClient.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ServicioSpotify {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServicioSpotify.class);

    AutenticadorSpotify autenticador;

    @Value("${spotify.api_base_url}")
    String baseUrl;

    public ServicioSpotify(AutenticadorSpotify autenticador) {
        this.autenticador = autenticador;
    }

    //@PostConstruct
    private Flux<Track> requestTracks(Genero genero, Token authToken) {
        LOGGER.trace("requestTracks()");
        WebClient cliente = WebClient.create(this.baseUrl);
        //String genero = "pop";
        ResponseSpec respSpec = cliente
                .method(HttpMethod.GET)
                .uri(ub -> ub
                        .path("/search")
                        .queryParam("q", "genre:"+genero.name().toLowerCase())
                        .queryParam("type", "track")
                        .build()
                )
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.getAccessToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve();

        return respSpec.bodyToMono(String.class)
                .flatMapMany(this::parseTracks);
    }

    public Flux<Track> getTracks(Genero genero) {
        LOGGER.debug("getTracks()");
        Mono<Token> token = this.autenticador.getAccessToken();
        return token.flatMapMany(t -> requestTracks(genero, t));
    }
    ObjectMapper mapper = new ObjectMapper();
    Flux<Track> parseTracks(String json) {
        LOGGER.debug("parseTracks()");
        try {
            JsonNode items = mapper.readTree(json)
                    .path("tracks")
                    .path("items");
            Iterator<JsonNode> it = items.elements();
            List<Track> tracks = new ArrayList<>();
            while (it.hasNext()) {
                JsonNode t = it.next();
                LOGGER.trace(t.toString());
                tracks.add(new Track(
                        t.path("name").asText(),
                        t.path("artists").elements().next().path("name").asText()
                ));
            }
            return Flux.fromIterable(tracks);
        } catch (JsonProcessingException e) {
            LOGGER.error("No se pudo leer tracks", e);
            return Flux.empty();
        }
    }
}
