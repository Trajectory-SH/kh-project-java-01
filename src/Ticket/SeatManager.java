package Ticket;

import java.util.List;

public class SeatManager {

    public static void printSeatMatrix(Concert concert) {
        List<String> reserved = concert.getReservedSeats();
        int totalSeats = concert.getTotalSeats();

        int rows = (totalSeats + 9) / 10; // 10개씩 나누기
        char rowChar = 'A';

        for (int i = 0; i < rows; i++) {
            System.out.printf("%c ", rowChar);
            for (int j = 1; j <= 10; j++) {
                int seatNumber = i * 10 + j;
                if (seatNumber > totalSeats) break;

                String seatCode = rowChar + String.valueOf(j);
                if (reserved.contains(seatCode)) {
                    System.out.print("[X] ");
                } else {
                    System.out.printf("[%d] ", j);
                }
            }
            System.out.println();
            rowChar++;
        }
    }

    public static boolean isValidSeat(String seatCode, int totalSeats) {
        if (seatCode.length() < 2) return false;

        char row = seatCode.charAt(0);
        String colStr = seatCode.substring(1);

        if (!colStr.matches("\\d+")) return false;

        int col = Integer.parseInt(colStr);

        int maxRow = (totalSeats + 9) / 10;
        char lastRow = (char) ('A' + maxRow - 1);

        if (row < 'A' || row > lastRow) return false;
        if (col < 1 || col > 10) return false;

        int seatNumber = (row - 'A') * 10 + col;
        return seatNumber <= totalSeats;
    }
}