package com.az.webclientexample.persistence;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RequestRepository extends R2dbcRepository<Request, Long> {
}
