package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JsonRepository implements WiseSayingRepository {

    private final Map<Integer, WiseSaying> wiseSayings = new LinkedHashMap<>();
    private static int lastId = 0;

    public JsonRepository(String filePath) {
        loadFromFile(filePath);
    }

    @Override
    public int nextId() {
        return ++lastId;
    }

    @Override
    public void save(WiseSaying entity) {
        // 명언을 맵에 저장
        wiseSayings.put(entity.getId(), entity);
    }

    @Override
    public Optional<WiseSaying> findById(int id) {
        // ID로 명언을 찾아서 Optional로 반환
        return Optional.ofNullable(wiseSayings.get(id));
    }

    @Override
    public boolean deleteById(int id) {
        // 명언을 ID로 삭제하고 성공 여부를 반환
        return wiseSayings.remove(id) != null;
    }

    public void loadFromFile(String filePath) {
        // lastId.txt 파일에서 마지막 ID를 읽어옴
        try {
            Path path = Path.of(filePath + "/lastId.txt");
            if (!Files.exists(path)) {
                Files.createFile(path);
                Files.writeString(path, "0");
            } else {
                String lastIdStr = Files.readString(path).trim();
                lastId = Integer.parseInt(lastIdStr);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 명언 파일들을 읽어서 wiseSayings 리스트에 추가
        try {
            for (int i = 1; i <= lastId; i++) {
                Path wiseSayingPath = Path.of(filePath + "/" + i + ".json");

                // 명언 파일이 존재하는 경우 Json 파일을 읽어서 명언 객체로 변환
                if (Files.exists(wiseSayingPath)) {
                    String wiseSayingJson = Files.readString(wiseSayingPath).trim();
                    String[] parts = wiseSayingJson.split("\",\"");
                    int id = Integer.parseInt(parts[0].replace("{\"id\":", ""));
                    String quote = parts[1].replace("quote\":\"", "");
                    String author = parts[2].replace("author\":\"", "").replace("\"}", "");
                    WiseSaying wiseSaying = new WiseSaying(id, quote, author);
                    wiseSayings.put(id, wiseSaying);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
