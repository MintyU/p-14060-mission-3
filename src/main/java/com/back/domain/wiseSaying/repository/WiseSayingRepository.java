package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.util.Optional;

public interface WiseSayingRepository {
    int nextId();

    void save(WiseSaying entity);

    Optional<WiseSaying> findById(int id);

    boolean deleteById(int id);
}