package Ticket;

import java.io.*;
import java.util.*;

public class ReservationManager {
    private static final String FILE_PATH = "resource/reservation_data.txt";


    Scanner scanner = new Scanner(System.in);
    public static void showAvailableConcerts(Scanner scanner, List<Concert> concertList) {
        System.out.println("\n================= 예매 가능 콘서트 목록 =================");
        int index = 1;
        Map<Integer, Concert> selectableConcerts = new HashMap<>();

        for (Concert concert : concertList) {
            if (concert.getRemainingSeats() > 0) {
                System.out.printf("%d. 콘서트명: %s | 날짜: %s | 가격: %,d원 | 잔여 좌석: %d석\n",
                        index, concert.getName(), concert.getDate(), concert.getPrice(), concert.getRemainingSeats());
                selectableConcerts.put(index, concert);
                index++;
            }
        }

        if (selectableConcerts.isEmpty()) {
            System.out.println("현재 예매 가능한 콘서트가 없습니다.");
            return;
        }

        System.out.println("0. 뒤로가기");
        System.out.println("========================================================");
        System.out.print("예매할 콘서트 번호를 입력하세요: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
            return;
        }

        if (choice == 0) {
            return;
        } else if (selectableConcerts.containsKey(choice)) {
            Concert selectedConcert = selectableConcerts.get(choice);
            // 다음 단계: 좌석 예매 페이지로 이동
            reserveSeat(scanner, selectedConcert);
        } else {
            System.out.println("잘못된 번호입니다.");
        }
    }
    public static void saveUserReservation(String userId, Concert concert, String seat) {
        try {
            // ⭐ 디렉토리 먼저 체크하고 없으면 생성
            File resourceDir = new File("src/resource");
            if (!resourceDir.exists()) {
                resourceDir.mkdirs(); // 폴더 생성
            }

            File file = new File("src/resource/reservation_data.txt");
            if (!file.exists()) {
                file.createNewFile(); // 파일 생성
            }

            System.out.println("[디버그] 예약 저장 시도: " + userId + "," + concert.getName() + "," + seat);

            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(userId + "," + concert.getName() + "," + concert.getDate() + "," +
                     concert.getPrice() + "," + seat);
            bw.newLine();
            bw.close();

            System.out.println("[디버그] 예약 저장 성공!");

        } catch (IOException e) {
            System.out.println("[오류] 예약 정보 저장 실패: " + e.getMessage());
            e.printStackTrace(); // 예외 전체 출력
        }
    }

    public static void showUserReservation(String userId) {
        boolean found = false;
        System.out.println("\n===== 나의 예매 정보 =====");

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[0].equals(userId)) { // ⭐ userId로 예약 찾기
                    System.out.println("공연명 : " + parts[1]);
                    System.out.println("날짜   : " + parts[2]);
                    System.out.printf("가격   : %,d원\n", Integer.parseInt(parts[3]));
                    System.out.println("좌석   : " + parts[4]);
                    System.out.println("-----------------------------");
                    found = true;
                }
            }
        } catch (IOException e) {
            System.out.println("[오류] 예약 정보 로드 실패: " + e.getMessage());
        }

        if (!found) {
            System.out.println("[알림] 현재 예매한 내역이 없습니다.");
        }
    }

    public static void reserveSeat(Scanner scanner, Concert concert) {
        System.out.println("\n================= 좌석 선택 =================");
        concert.showSeatMap(); // 콘서트 객체에 좌석 보여주는 메서드가 있어야 함

        System.out.print("예약할 좌석을 입력하세요 (예: A1): ");
        String seat = scanner.nextLine().toUpperCase();

        if (!concert.isValidSeat(seat)) { // 좌석 유효성 검사
            System.out.println("[오류] 존재하지 않는 좌석입니다.");
            return;
        }

        if (concert.isSeatReserved(seat)) { // 이미 예약된 좌석
            System.out.println("[오류] 이미 예약된 좌석입니다.");
            return;
        }

        // 실제 예약
        concert.reserveSeat(seat);

        // 파일에 저장
        User user = UserManager.getLoggedInUser();
        if (user == null) {
            System.out.println("[오류] 로그인 정보가 없습니다. 다시 로그인 해주세요.");
            return;
        }

        saveUserReservation(user.getUsername(), concert, seat); // 기존 메서드 사용
        updateUserReservation(user.getUsername(), concert, seat); // ⭐ 추가할 user_data 업데이트

        System.out.println("[성공] 좌석 예약이 완료되었습니다!");
    }
    // ⭐ ReservationManager.java에 추가
    public static void updateUserReservation(String userId, Concert concert, String seat) {
        File userDataFile = new File("src/resource/user_data.txt");
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(userDataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(userId)) {
                    // 기존 정보에 예매 정보 추가
                    StringBuilder updatedLine = new StringBuilder(line);
                    updatedLine.append(",").append(concert.getName()).append("-").append(seat);
                    updatedLines.add(updatedLine.toString());
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("[오류] 유저 데이터 읽기 실패: " + e.getMessage());
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userDataFile))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("[오류] 유저 데이터 저장 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

}