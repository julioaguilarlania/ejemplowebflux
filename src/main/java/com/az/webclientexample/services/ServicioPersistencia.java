package com.az.webclientexample.services;

import com.az.webclientexample.persistence.Request;
import com.az.webclientexample.persistence.RequestRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ServicioPersistencia {

    RequestRepository reqRepository;

    public ServicioPersistencia(RequestRepository reqRepository) {
        this.reqRepository = reqRepository;
    }

    public Mono<Void> save(Request req) {
        return reqRepository.save(req)
                .then();
    }
}
