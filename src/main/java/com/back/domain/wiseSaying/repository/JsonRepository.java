package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class JsonRepository implements WiseSayingRepository {

    private final Map<Integer, WiseSaying> wiseSayings = new LinkedHashMap<>();
    private static int lastId = 0;
    private boolean isDirty = false;

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
        isDirty = true;
    }

    @Override
    public Optional<WiseSaying> findById(int id) {
        // ID로 명언을 찾아서 Optional로 반환
        return Optional.ofNullable(wiseSayings.get(id));
    }

    @Override
    public boolean deleteById(int id) {
        // 명언을 ID로 삭제하고 성공 여부를 반환
        boolean removed = wiseSayings.remove(id) != null;
        if (removed) {
            isDirty = true;
        }
        return removed;
    }

    @Override
    public void flush(String filePath) {
        if (!isDirty) return;
        try {
            // lastId.txt 파일에 마지막 ID 저장
            Path lastIdPath = Path.of(filePath + "/lastId.txt");
            Files.writeString(lastIdPath, String.valueOf(lastId));

            // 각 명언을 개별 JSON 파일로 저장
            for (WiseSaying wiseSaying : wiseSayings.values()) {
                Path wiseSayingPath = Path.of(filePath + "/" + wiseSaying.getId() + ".json");
                String wiseSayingJson = String.format("{\"id\":%d,\"quote\":\"%s\",\"author\":\"%s\"}", wiseSaying.getId(), wiseSaying.getQuote(), wiseSaying.getAuthor());
                Files.writeString(wiseSayingPath, wiseSayingJson);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 모든 데이터를 data.json이라는 이름의 파일로 저장
        try {
            Path dataPath = Path.of(filePath + "/data.json");
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            for (WiseSaying wiseSaying : wiseSayings.values()) {
                String wiseSayingJson = String.format("  {\"id\":%d,\"quote\":\"%s\",\"author\":\"%s\"},\n", wiseSaying.getId(), wiseSaying.getQuote(), wiseSaying.getAuthor());
                sb.append(wiseSayingJson);
            }
            if (!wiseSayings.isEmpty()) {
                sb.setLength(sb.length() - 2); // 마지막 쉼표와 줄바꿈 제거
                sb.append("\n");
            }
            sb.append("]");
            Files.writeString(dataPath, sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 존재하지 않는 ID의 파일들을 삭제
        try {
            for (int i = 1; i <= lastId; i++) {
                if (!wiseSayings.containsKey(i)) {
                    Path wiseSayingPath = Path.of(filePath + "/" + i + ".json");
                    if (Files.exists(wiseSayingPath)) {
                        Files.delete(wiseSayingPath);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        isDirty = false;
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
                    String[] parts = wiseSayingJson.replace("{", "").replace("}", "").replace("\"", "").split(",");
                    int id = Integer.parseInt(parts[0].split(":")[1]);
                    String quote = parts[1].split(":")[1];
                    String author = parts[2].split(":")[1];
                    WiseSaying wiseSaying = new WiseSaying(id, quote, author);
                    wiseSayings.put(id, wiseSaying);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
