package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.JsonRepository;

import java.util.Optional;

public class WiseSayingService {
    private final JsonRepository repo = new JsonRepository("db/wiseSaying");

    // 명언 추가
    public WiseSaying add(String quote, String author) {
        int id = repo.nextId();
        WiseSaying ws = new WiseSaying(id, quote, author);
        repo.save(ws);
        repo.flush("db/wiseSaying");
        return ws;
    }
    // 명언 수정 (존재하는 경우 수정하고 true 반환, 없으면 false 반환)
    public boolean update(int id, String quote, String author) {
        var optionalWiseSaying = repo.findById(id);
        if (optionalWiseSaying.isEmpty()) {
            return false;
        }
        WiseSaying ws = optionalWiseSaying.get();
        ws.setQuote(quote);
        ws.setAuthor(author);
        repo.save(ws);
        repo.flush("db/wiseSaying");
        return true;
    }

    // 명언 삭제 (존재하는 경우 삭제하고 true 반환, 없으면 false 반환)
    public boolean delete(int id) {
        boolean deleted = repo.deleteById(id);
        if (deleted) {
            repo.flush("db/wiseSaying");
        }
        return deleted;
    }

    public Optional<WiseSaying> getWiseSayingById(int id) {
        return repo.findById(id);
    }
}
