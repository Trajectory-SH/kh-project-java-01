package Ticket;

import java.io.*;
import java.util.*;
import java.util.ArrayList;

public class ConcertDataManager {

    private static final String CONCERT_FILE_PATH = "src/resource/concert_data.txt";

    // 콘서트 리스트 반환
    public static List<Concert> loadConcertData() {
        List<Concert> concertList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CONCERT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // 빈 줄은 무시

                String[] parts = line.split("/");
                if (parts.length < 6) {
                    System.out.println("잘못된 형식의 데이터: " + line);
                    continue;
                }

                String name = parts[0];
                String date = parts[1];
                int price = Integer.parseInt(parts[2]);
                int totalSeats = Integer.parseInt(parts[3]);
                int remainingSeats = Integer.parseInt(parts[4]);

                List<String> reservedSeats = new ArrayList<>();
                if (!parts[5].isEmpty()) {
                    reservedSeats = Arrays.asList(parts[5].split(","));
                }

                Concert concert = new Concert(name, date, price, totalSeats, remainingSeats, reservedSeats);
                concertList.add(concert);
            }
        } catch (FileNotFoundException e) {
            System.out.println("콘서트 데이터 파일을 찾을 수 없습니다.");
        } catch (IOException e) {
            System.out.println("콘서트 데이터 파일 읽기 오류가 발생했습니다.");
        }

        return concertList;
    }
}