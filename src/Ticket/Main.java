package Ticket;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager();

        while (true) {
            System.out.println("\n==== 콘서트 예매 시스템 ====");
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("0. 종료");
            System.out.print("선택: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    if (userManager.login(scanner)) {
                        System.out.println("---------");
                        System.out.println("로그인 성공!");
                        System.out.println("---------");

                        // 관리자 전용 메뉴 진입
                        if (UserManager.getLoggedInUser().getUsername().equals("admin")) {
                            AdminMenu.run(scanner);
                        } else {
                            UserMenu.run(scanner, UserManager.getLoggedInUser());
                        }

                    } else {
                        System.out.println("로그인 실패. 다시 시도해주세요.");
                    }
                    break;

                case "2":
                    userManager.signUp(scanner);
                    // 회원가입 완료 후 로그인 메뉴로 돌아가기 때문에 while 루프 유지
                    break;

                case "0":
                    System.out.println("프로그램을 종료합니다.");
                    return;

                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }
}