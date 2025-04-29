package Ticket;

import java.util.List;
import java.util.Scanner;

public class UserMenu {
    // UserMenu.java 클래스 내부
    private static UserManager userManager = new UserManager();
    Scanner scanner = new Scanner(System.in);
    public static void run(Scanner scanner, User currentUser) {
        while (true) {
            System.out.println("\n==== 사용자 메뉴 ====");
            System.out.println("1. 콘서트 예매");
            System.out.println("2. 프로그램 종료");
            System.out.print("메뉴 선택: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    reserveConcert();
                    break;
                case "2":
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                default:
                    System.out.println("올바른 번호를 입력해주세요.");
            }
        }
    }
    public static void displaySeatMatrix(Concert concert) {
        int totalSeats = concert.getTotalSeats();
        List<String> reservedSeats = concert.getReservedSeats();

        char row = 'A';
        int seatNumber = 1;
        System.out.println("===========================<STAGE>===========================");

        for (int i = 0; i < totalSeats; i++) {
            String seatLabel = row + String.valueOf(seatNumber);

            if (reservedSeats.contains(seatLabel)) {
                System.out.printf("[XX]  ");
            } else {
                System.out.printf("[%2s]  ", seatLabel);
            }

            seatNumber++;

            if (seatNumber > 10) {
                seatNumber = 1;
                row++;
                System.out.println();
            }
        }
        System.out.println("=============================================================");
        System.out.println(); // 마지막 줄 줄바꿈
    }
    private static void reserveConcert() {
        User currentUser = UserManager.getLoggedInUser();
        List<Concert> concertList = ConcertFileManager.loadConcerts();
        Scanner sc = new Scanner(System.in);
        if (concertList.isEmpty()) {
            System.out.println("[알림] 현재 예매 가능한 콘서트가 없습니다.");
            return;
        }

        System.out.println("\n=== 예매 가능한 콘서트 목록 ===");

        int index = 1;
        for (Concert concert : concertList) {
            if (concert.getRemainingSeats() > 0) {
                System.out.printf("%d. %s | 날짜: %s | 가격: %,d원 | 잔여 좌석: %d석\n",
                        index++, concert.getName(), concert.getDate(), concert.getPrice(), concert.getRemainingSeats());
            }
        }

        if (index == 1) {
            System.out.println("[알림] 현재 예매 가능한 콘서트가 없습니다.");
            return;
        }

        System.out.print("\n예매할 콘서트 번호를 선택하세요 (취소: 0): ");
        int select = Integer.parseInt(sc.nextLine());

        if (select == 0) {
            System.out.println("이전 메뉴로 돌아갑니다.");
            return;
        }

        if (select < 1 || select >= index) {
            System.out.println("[오류] 올바른 번호를 입력해주세요.");
            return;
        }

        // 선택한 콘서트 정보 가져오기
        Concert selectedConcert = concertList.get(select - 1);

        // 좌석 매트릭스 출력
        System.out.println("\n=== 좌석 선택 페이지 ===");
        UserMenu.displaySeatMatrix(selectedConcert);

        // 좌석 선택
        System.out.print("\n예약할 좌석 번호를 입력하세요 (예: A1): ");
        String seatInput = sc.nextLine().toUpperCase();

        if (selectedConcert.getReservedSeats().contains(seatInput)) {
            System.out.println("[오류] 이미 예약된 좌석입니다. 다시 시도하세요.");
            return;
        }

        if (!SeatManager.isValidSeat(seatInput, selectedConcert.getTotalSeats())) {
            System.out.println("[오류] 존재하지 않는 좌석입니다. 다시 시도하세요.");
            return;
        }

        // 예약 처리
        selectedConcert.getReservedSeats().add(seatInput);
        selectedConcert.setRemainingSeats(selectedConcert.getRemainingSeats() - 1);

        // 콘서트 파일 업데이트
        ConcertFileManager.saveConcerts(concertList);

        // 사용자 예약 정보 저장

        if (currentUser != null) {
            currentUser.setReservedConcertName(selectedConcert.getName());
            currentUser.setReservedSeat(seatInput);
            userManager.saveUsers(); // 변경된 사용자 정보 저장
        } else {
            System.out.println("[오류] 로그인 정보가 없습니다. 다시 로그인 해주세요.");
        }

        System.out.println("[성공] 예약이 완료되었습니다!");
    }
}