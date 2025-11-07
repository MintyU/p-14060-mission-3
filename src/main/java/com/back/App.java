package com.back;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;

import java.util.Scanner;

public class App {
    WiseSayingController wiseSayingController = new WiseSayingController();
    SystemController systemController = new SystemController();
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
                systemController.actionExit();
                return;
            } else if (cmd.startsWith("등록")) {
                wiseSayingController.actionWrite();
            } else if (cmd.startsWith("목록")) {
                wiseSayingController.actionList();
            } else if (cmd.startsWith("수정")) {
                wiseSayingController.actionModify(cmd);
            } else if (cmd.startsWith("삭제")) {
                wiseSayingController.actionDelete(cmd);
            } else {
                System.out.println("알 수 없는 명령입니다.");
            }
        }
    }
}
