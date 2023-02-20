package com.az.webclientexample.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("requests")
public class Request {

    @Id
    @Column("request_id")
    private Long id;

    @Column("nombre_ciudad")
    private String nombreCiudad;

    @Column("temperatura")
    private Double temperatura;

    @Column("momento")
    private Instant momento;

    public Request() {
    }

    public Request(String ciudad, Double t) {
        this.nombreCiudad = ciudad;
        this.temperatura = t;
        this.momento = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Instant getMomento() {
        return momento;
    }

    public void setMomento(Instant momento) {
        this.momento = momento;
    }
}
