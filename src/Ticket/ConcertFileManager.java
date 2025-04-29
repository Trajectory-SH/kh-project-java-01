package Ticket;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Ticket.UserManager.FILE_PATH;

public class ConcertFileManager {
    private static final String CONCERT_FILE_PATH = "src/resource/concert_data.txt"; // 콘서트 정보 파일 경로

    public static List<Concert> loadConcerts() {
        List<Concert> concertList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CONCERT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 5) {
                    String name = parts[0];
                    String date = parts[1];
                    int price = Integer.parseInt(parts[2]);
                    int totalSeats = Integer.parseInt(parts[3]);
                    int remainingSeats = Integer.parseInt(parts[4]);

                    List<String> reservedSeats = new ArrayList<>();
                    if (parts.length > 5) {
                        reservedSeats = Arrays.asList(parts[5].split("\\|"));
                        // 예약된 좌석은 '|' 기준으로 구분
                    }

                    Concert concert = new Concert(name, date, price, totalSeats, remainingSeats, reservedSeats);
                    concertList.add(concert);
                }
            }
        } catch (IOException e) {
            System.out.println("[오류] 콘서트 정보를 불러오는 중 문제가 발생했습니다: " + e.getMessage());
        }

        return concertList;
    }
    public static void saveConcerts(List<Concert> concertList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONCERT_FILE_PATH))) {
            for (Concert c : concertList) {
                bw.write(c.getName() + "," + c.getDate() + "," + c.getPrice() + "," +
                         c.getTotalSeats() + "," + c.getRemainingSeats() + "," +
                         String.join("|", c.getReservedSeats()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("[오류] 콘서트 파일 저장 실패: " + e.getMessage());
        }
    }
}