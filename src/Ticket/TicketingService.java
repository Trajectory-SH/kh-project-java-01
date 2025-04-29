package Ticket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TicketingService {

    private static final String RESERVATION_FILE_PATH = "src/resource/reservation_data.txt";

    public static void printTicketInfo(String userId) {
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(RESERVATION_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts[0].equals(userId)) {
                    String concertName = parts[1];
                    String concertDate = parts[2];
                    int price = Integer.parseInt(parts[3]);
                    String seat = parts[4];

                    System.out.println("\n=============================");
                    System.out.println("|        Concert Ticket       |");
                    System.out.println("|-----------------------------|");
                    System.out.printf("| 공연명 : %-18s |\n", concertName);
                    System.out.printf("| 날짜   : %-18s |\n", concertDate);
                    System.out.printf("| 좌석   : %-18s |\n", seat);
                    System.out.printf("| 가격   : %,15d원 |\n", price);
                    System.out.println("|-----------------------------|");
                    System.out.println("|       Have a Nice Day       |");
                    System.out.println("===============================\n");

                    found = true;
                }
            }

            if (!found) {
                System.out.println("[알림] 예매한 공연이 없습니다.");
            }
        } catch (IOException e) {
            System.out.println("[오류] 예매 정보 불러오기 실패: " + e.getMessage());
        }
    }
}