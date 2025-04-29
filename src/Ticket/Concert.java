package Ticket;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Concert {
    private String name;
    private String date;
    private int price;
    private int totalSeats;
    private int remainingSeats;
    private List<String> reservedSeats;


    public Concert(String name, String date, int price, int totalSeats, int remainingSeats, List<String> reservedSeats) {
        this.name = name;
        this.date = date;
        this.price = price;
        this.totalSeats = totalSeats;
        this.remainingSeats = remainingSeats;
        this.reservedSeats = new ArrayList<>(reservedSeats); // 복사본 생성
    }
    private Map<String, Boolean> seatMap = new HashMap<>();
    // ⭐ Concert.java에 추가

    // 좌석 상태를 출력하는 메서드
    public void showSeatMap() {
        System.out.println("\n----- 좌석 현황 -----");
        for (String seat : seatMap.keySet()) {
            String status = seatMap.get(seat) ? "[예약됨]" : "[비어있음]";
            System.out.printf("%s %s\t", seat, status);
        }
        System.out.println("\n----------------------");
    }

    // 좌석 유효성 검사 (좌석 이름이 seatMap에 존재하는지)
    public boolean isValidSeat(String seat) {
        return seatMap.containsKey(seat);
    }

    // 해당 좌석이 이미 예약되었는지 확인
    public boolean isSeatReserved(String seat) {
        return seatMap.getOrDefault(seat, false);
    }

    // 좌석을 예약 처리 (비어있는 좌석이면 true로 설정)
    public void reserveSeat(String seat) {
        if (seatMap.containsKey(seat) && !seatMap.get(seat)) {
            seatMap.put(seat, true);
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRemainingSeats() {
        return remainingSeats;
    }

    public void setRemainingSeats(int remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    public List<String> getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(List<String> reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
// 생성자, getter/setter 생략
}