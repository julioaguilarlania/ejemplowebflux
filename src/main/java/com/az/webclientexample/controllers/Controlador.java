package com.az.webclientexample.controllers;

import com.az.webclientexample.dtos.Track;
import com.az.webclientexample.services.ServicioClima;
import com.az.webclientexample.services.ServicioSpotify;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class Controlador {

    ServicioClima servicioClima;
    ServicioSpotify servicioSpotify;

    public Controlador(ServicioClima servicioClima, ServicioSpotify servicioSpotify) {
        this.servicioClima = servicioClima;
        this.servicioSpotify = servicioSpotify;
    }

    @GetMapping(value="/musica", params = "ciudad")
    public Flux<Track> getCancionesPorClima(@RequestParam("ciudad") String ciudad) {
        return servicioClima.getTemperatura(ciudad)
                .map(Controlador::tempToGenero)
                .flatMapMany(g -> servicioSpotify.getTracks(g));
    }

    private static Genero tempToGenero(double temp) {
        if (temp < 10) return Genero.CLASSIC;
        if (temp < 15) return Genero.ROCK;
        if (temp < 30) return Genero.POP;
        return Genero.PARTY;
    }
}
