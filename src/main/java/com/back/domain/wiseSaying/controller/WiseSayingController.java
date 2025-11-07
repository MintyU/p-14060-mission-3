package com.back.domain.wiseSaying.controller;


import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.Scanner;

public class WiseSayingController {
    private final Scanner scanner = new Scanner(System.in);
    private final WiseSayingService wiseSayingService = new WiseSayingService();

    public void actionWrite() {
        System.out.println("명언: ");
        String content = scanner.nextLine().trim();
        System.out.println("작가: ");
        String author = scanner.nextLine().trim();
        WiseSaying wiseSaying  = wiseSayingService.add(content, author);
        System.out.printf("%d번 명언이 등록되었습니다.\n", wiseSaying.getId());
    }
}
