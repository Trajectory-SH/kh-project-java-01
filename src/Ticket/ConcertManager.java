package Ticket;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConcertManager {
    Scanner scanner = new Scanner(System.in);
    public static void concertMenuManagement(Scanner scanner) {
        while (true) {
            System.out.println("\n==== 콘서트 목록 관리 ====");
            System.out.println("1. 콘서트 추가");
            System.out.println("2. 콘서트 삭제");
            System.out.println("3. 기존 콘서트 정보 수정");
            System.out.println("0. 뒤로가기");
            System.out.print(">> 선택: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    addConcert(scanner);
                    break;
                case "2":
                    deleteConcert(scanner);
                    break;
                case "3":
                    modifyConcert(scanner);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private static void addConcert(Scanner scanner) {
        System.out.println("\n==== 콘서트 추가 ====");

        System.out.print("콘서트 이름: ");
        String name = scanner.nextLine();

        System.out.print("콘서트 날짜 (예: 2025-06-15): ");
        String date = scanner.nextLine();

        System.out.print("콘서트 가격: ");
        int price = Integer.parseInt(scanner.nextLine());

        int totalSeats;
        while (true) {
            System.out.print("총 좌석 수 (10의 배수로 입력): ");
            totalSeats = Integer.parseInt(scanner.nextLine());
            if (totalSeats % 10 == 0) {
                break;
            } else {
                System.out.println("(주의)총 좌석 수는 반드시 10의 배수여야 합니다. 다시 입력해주세요.");
            }
        }

        int remainingSeats = totalSeats;
        int totalSales = 0;

        String concertData = String.format("%s,%s,%d,%d,%d,%d",
                name, date, price, totalSeats, remainingSeats, totalSales);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/resource/concert_data.txt", true))) {
            writer.write(concertData);
            writer.newLine();
            System.out.println("콘서트가 성공적으로 추가되었습니다.");
        } catch (IOException e) {
            System.out.println("콘서트 저장 중 오류 발생: " + e.getMessage());
        }
    }

    private static void deleteConcert(Scanner scanner) {
        List<String> concertList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/resource/concert_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                concertList.add(line);
            }
        } catch (IOException e) {
            System.out.println("콘서트 파일을 읽는 도중 오류가 발생했습니다.");
            return;
        }

        if (concertList.isEmpty()) {
            System.out.println("현재 등록된 콘서트가 없습니다.");
            return;
        }

        System.out.println("\n==== 삭제할 콘서트를 선택하세요 ====");
        for (int i = 0; i < concertList.size(); i++) {
            String[] parts = concertList.get(i).split(",");
            System.out.printf("%d. %s | 날짜: %s | 가격: %s원\n", i + 1, parts[0], parts[1], parts[2]);
        }

        System.out.print("삭제할 콘서트 번호 (0: 취소): ");
        String input = scanner.nextLine();

        try {
            int index = Integer.parseInt(input) - 1;

            if (index == -1) {
                System.out.println("삭제를 취소합니다.");
                return;
            }

            if (index < 0 || index >= concertList.size()) {
                System.out.println("잘못된 번호입니다.");
                return;
            }

            String removedConcert = concertList.remove(index);

            // 삭제 후 파일에 다시 저장
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/resource/concert_data.txt"))) {
                for (String concert : concertList) {
                    writer.write(concert);
                    writer.newLine();
                }
            }

            System.out.println("콘서트가 성공적으로 삭제되었습니다: " + removedConcert.split(",")[0]);

        } catch (NumberFormatException e) {
            System.out.println("숫자로 입력해주세요.");
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다.");
        }
    }

    public static void modifyConcert(Scanner scanner) {
        List<String> concertList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/resource/concert_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                concertList.add(line);
            }
        } catch (IOException e) {
            System.out.println("콘서트 파일을 읽는 중 오류 발생!");
            return;
        }

        if (concertList.isEmpty()) {
            System.out.println("등록된 콘서트가 없습니다.");
            return;
        }

        // 콘서트 목록 출력
        System.out.println("\n=== 수정할 콘서트를 선택하세요 ===");
        for (int i = 0; i < concertList.size(); i++) {
            String[] parts = concertList.get(i).split(",");
            System.out.printf("%d. %s | 날짜: %s | 가격: %s원\n", i + 1, parts[0], parts[1], parts[2]);
        }

        System.out.print("수정할 콘서트 번호 (0: 취소): ");
        String input = scanner.nextLine();

        try {
            int index = Integer.parseInt(input) - 1;

            if (index == -1) {
                System.out.println("수정을 취소합니다.");
                return;
            }

            if (index < 0 || index >= concertList.size()) {
                System.out.println("잘못된 번호입니다.");
                return;
            }

            String[] parts = concertList.get(index).split(",");

            System.out.println("\n수정할 항목을 선택하세요:");
            System.out.println("1. 콘서트 이름");
            System.out.println("2. 콘서트 날짜");
            System.out.println("3. 콘서트 가격");
            System.out.println("4. 총 좌석 수 (10의 배수만 입력 가능)");
            System.out.print("항목 번호: ");
            String fieldInput = scanner.nextLine();

            switch (fieldInput) {
                case "1":
                    System.out.print("새 콘서트 이름: ");
                    parts[0] = scanner.nextLine();
                    break;
                case "2":
                    System.out.print("새 콘서트 날짜 (예: 2025-06-20): ");
                    parts[1] = scanner.nextLine();
                    break;
                case "3":
                    System.out.print("새 콘서트 가격: ");
                    parts[2] = scanner.nextLine();
                    break;
                case "4":
                    System.out.print("새 총 좌석 수 (10의 배수): ");
                    int newTotal = Integer.parseInt(scanner.nextLine());
                    if (newTotal % 10 != 0) {
                        System.out.println("총 좌석 수는 10의 배수여야 합니다!");
                        return;
                    }
                    int reserved = Integer.parseInt(parts[3]) - Integer.parseInt(parts[4]);
                    parts[3] = String.valueOf(newTotal);
                    parts[4] = String.valueOf(newTotal - reserved); // 잔여좌석 수 업데이트
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    return;
            }

            // 수정된 콘서트 정보 반영
            concertList.set(index, String.join(",", parts));

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/resource/concert_data.txt"))) {
                for (String concert : concertList) {
                    writer.write(concert);
                    writer.newLine();
                }
            }

            System.out.println("콘서트 정보가 성공적으로 수정되었습니다.");

        } catch (NumberFormatException e) {
            System.out.println("숫자 형식이 올바르지 않습니다.");
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다.");
        }
    }
}