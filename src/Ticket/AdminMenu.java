package Ticket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    public static void run(Scanner scanner) {
        while (true) {
            System.out.println("\n===== 관리자 메뉴 =====");
            System.out.println("1. 회원 정보 출력");
            System.out.println("2. 콘서트 목록 수정");
            System.out.println("0. 프로그램 종료");
            System.out.print("메뉴 선택: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    displayUserList(scanner);
                    break;
                case "2":
                    ConcertManager.concertMenuManagement(scanner);
                    break;
                case "0":
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }

    private static void displayUserList(Scanner scanner) {
        System.out.println("=========회원정보 호출=========");
        List<String[]> userList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/resource/user_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // 빈 문자열도 포함
                userList.add(parts);
            }
        } catch (IOException e) {
            System.out.println("회원 정보를 불러오는 중 오류가 발생했습니다.");
            return;
        }

        int totalUsers = userList.size();
        int pageSize = 5;
        int totalPages = (int) Math.ceil(totalUsers / (double) pageSize);

        while (true) {
            System.out.println("\n총 회원 수: " + totalUsers + "명 / 총 페이지: " + totalPages + "페이지");
            System.out.print("확인할 페이지 번호 입력 (0 입력 시 관리자 메뉴로 이동): ");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                break;
            }

            int page;
            try {
                page = Integer.parseInt(input);
                if (page < 1 || page > totalPages) {
                    System.out.println("잘못된 페이지 번호입니다.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
                continue;
            }

            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, totalUsers);

            System.out.println("\n===== 회원 정보 (" + page + "페이지) =====");
            for (int i = start; i < end; i++) {
                String[] user = userList.get(i);
                String name = user[0];
                String id = user[1];
                String maskedPw = "*".repeat(user[2].length());
                String phone = user[3];
                String concert = user.length > 4 ? user[4] : "";
                String seat = user.length > 5 ? user[5] : "";

                System.out.printf("[%d] 이름: %s | 아이디: %s | 비밀번호: %s | 전화번호: %s | 예매 공연: %s | 좌석: %s\n",
                        i + 1, name, id, maskedPw, phone, concert, seat);
            }
        }
    }


    private static void displayConcertReport() {
        List<String[]> concertList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/resource/concert_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                concertList.add(parts);
            }
        } catch (IOException e) {
            System.out.println("콘서트 정보를 불러오는 중 오류가 발생했습니다.");
            return;
        }

        System.out.println("\n================================ 콘서트 매출 보고서 ================================");

        // 최대 길이에 맞춰 포맷 지정
        String headerFormat = "| %-3s | %-14s | %-10s | %-7s | %-6s | %-11s |";
        String rowFormat = "| %-4d | %-16s | %-11s | %-9d | %-7d | %,12d원 |";

        // 헤더 출력
        System.out.printf(headerFormat, "번호", "콘서트 이름", "날짜", "잔여 좌석", "총 좌석", "총 판매 금액");
        System.out.println("\n|------|------------------|-------------|-----------|---------|---------------|");

        int index = 1;
        for (String[] concert : concertList) {
            String name = concert[0];
            String date = concert[1];
            int price = Integer.parseInt(concert[2]);
            int totalSeats = Integer.parseInt(concert[3]);
            int remainingSeats = Integer.parseInt(concert[4]);
            int totalSales = Integer.parseInt(concert[5]);

            // 데이터 출력
            System.out.printf(rowFormat, index++, name, date, remainingSeats, totalSeats, totalSales);
            System.out.println();
        }

        System.out.println("===============================================================================\n");
    }

}