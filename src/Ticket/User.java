package Ticket;

public class User {
    private String name;
    private String username;
    private String password;
    private String phone;
    private String reservedConcertName;
    private String reservedSeat;

    public User(String name, String username, String password, String phone) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.reservedConcertName = "";
        this.reservedSeat = "";
    }
    public User(String name, String id, String password, String phone, String reservedConcertName, String reservedSeat) {
        this.name = name;
        this.username = id;
        this.password = password;
        this.phone = phone;
        this.reservedConcertName = reservedConcertName;
        this.reservedSeat = reservedSeat;
    }

    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public String getReservedSeat() { return reservedSeat; }
    public void setReservedSeat(String reservedSeat) { this.reservedSeat = reservedSeat; }
    public void setReservedConcertName(String reservedConcertName) { this.reservedConcertName = reservedConcertName; }
    public String toDataString() {
        return String.join(",", name, username, password, phone, reservedConcertName, reservedSeat);
    }

    public String toFileString() {
        return name + "," + username + "," + password + "," + phone;
    }
    public String toCSV() {
        return String.join(",", name, username, password, phone);
    }



}