package com.az.webclientexample.controllers;

import com.az.webclientexample.dtos.Track;
import com.az.webclientexample.persistence.Request;
import com.az.webclientexample.persistence.RequestRepository;
import com.az.webclientexample.services.ServicioClima;
import com.az.webclientexample.services.ServicioPersistencia;
import com.az.webclientexample.services.ServicioSpotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class Controlador {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controlador.class);

    ServicioClima servicioClima;
    ServicioSpotify servicioSpotify;
    ServicioPersistencia servicioPersistencia;
    RequestRepository requestRepository;

    public Controlador(ServicioClima servicioClima, ServicioSpotify servicioSpotify, ServicioPersistencia servicioPersistencia, RequestRepository requestRepository) {
        this.servicioClima = servicioClima;
        this.servicioSpotify = servicioSpotify;
        this.servicioPersistencia = servicioPersistencia;
        this.requestRepository = requestRepository;
    }

    @GetMapping(value="/musica", params = "ciudad")
    public Flux<Track> getCancionesPorClima(@RequestParam("ciudad") String ciudad) {
        return servicioClima.getTemperatura(ciudad)
                .flatMap(t ->
                        servicioPersistencia.save(new Request(ciudad, t))
                                .thenReturn(t))
                .map(Controlador::tempToGenero)
                .flatMapMany(g -> servicioSpotify.getTracks(g));
    }

    @GetMapping("/requests")
    public Flux<Request> getRequests() {
        return requestRepository.findAll();
    }

    private static Genero tempToGenero(double temp) {
        if (temp < 10) return Genero.CLASSIC;
        if (temp < 15) return Genero.ROCK;
        if (temp < 30) return Genero.POP;
        return Genero.PARTY;
    }
}
