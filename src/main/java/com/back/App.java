package com.back;

import com.back.domain.wiseSaying.controller.WiseSayingController;

import java.util.Scanner;

public class App {
    WiseSayingController wiseSayingController = new WiseSayingController();
    private final Scanner scanner;

    public App() {
        this.scanner = new Scanner(System.in);
    }



    public void run() {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equals("종료")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else if (cmd.startsWith("등록")) {
                wiseSayingController.actionWrite();

            } else if (cmd.startsWith("목록")) {

            } else if (cmd.startsWith("수정")) {

            } else if (cmd.startsWith("삭제")) {

            } else {
                System.out.println("알 수 없는 명령입니다.");
            }
        }
    }
}
