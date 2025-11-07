package com.back.domain.wiseSaying.controller;


import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.Optional;
import java.util.Scanner;

public class WiseSayingController {
    private final Scanner scanner = new Scanner(System.in);
    private final WiseSayingService wiseSayingService = new WiseSayingService();

    public void actionWrite() {
        System.out.print("명언: ");
        String content = scanner.nextLine().trim();
        System.out.print("작가: ");
        String author = scanner.nextLine().trim();
        WiseSaying wiseSaying  = wiseSayingService.add(content, author);
        System.out.printf("%d번 명언이 등록되었습니다.\n", wiseSaying.getId());
    }

    public void actionModify(String cmd) {
        String[] parts = cmd.split("\\?", 2);
        if (parts.length < 2) {
            System.out.println("명령어 형식이 올바르지 않습니다.");
            return;
        }
        String[] paramParts = parts[1].split("=", 2);
        if (paramParts.length < 2 || !paramParts[0].equals("id")) {
            System.out.println("id 파라미터가 올바르지 않습니다.");
            return;
        }
        int id = Integer.parseInt(paramParts[1]);
        Optional<WiseSaying> wiseSaying = wiseSayingService.getWiseSayingById(id);
        if (wiseSaying.isEmpty()) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
            return;
        }
        System.out.printf("기존 명언: %s\n", wiseSaying.get().getQuote());
        System.out.print("명언: ");
        String newQuote = scanner.nextLine().trim();
        System.out.printf("기존 작가: %s\n", wiseSaying.get().getAuthor());
        System.out.print("작가: ");
        String newAuthor = scanner.nextLine().trim();
        boolean updated = wiseSayingService.update(id, newQuote, newAuthor);
        if (updated) {
            System.out.printf("%d번 명언이 수정되었습니다.\n", id);
        }
    }
}
