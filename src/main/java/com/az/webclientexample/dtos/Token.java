package com.az.webclientexample.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

public class Token {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Integer expiresIn;

    private Instant momentoExpiracion;

    public void registrarToken(Token token) {
        this.accessToken = token.getAccessToken();
        this.tokenType = token.getTokenType();
        this.expiresIn = token.expiresIn;
        this.momentoExpiracion = Instant.now()
                .plusSeconds(this.expiresIn)
                .minusSeconds(60);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public boolean isExpired() {
        return this.momentoExpiracion.isAfter(Instant.now());
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + "'" +
                ", tokenType='" + tokenType + "'" +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
