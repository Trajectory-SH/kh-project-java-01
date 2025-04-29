package Ticket;

import java.io.*;
import java.util.*;

public class UserManager {
    static final String FILE_PATH = "src/resource/user_data.txt";
    static final String CONCERT_FILE_PATH = "src/resource/concert_data.txt";
    private List<User> users = new ArrayList<>();
    private static User loggedInUser = null;

    public void addUser(User user) {
        users.add(user);
    }

    public User findUserById(String id) {
        for (User user : users) {
            if (user.getUsername().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public boolean authenticate(String id, String password) {
        User user = findUserById(id);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUser = user; // ⭐ [추가] 로그인 성공 시 저장
            return true;
        }
        return false;
    }

    public static User getLoggedInUser() { // ⭐ [추가] 로그인한 유저를 가져오기 위한 메서드
        return loggedInUser;
    }

    public static void logout() { // ⭐ [추가] 로그아웃 처리
        loggedInUser = null;
    }

    public List<User> getUsers() {
        return users;
    }


    public UserManager() {
        loadUsers();
    }

    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    users.add(new User(data[0], data[1], data[2], data[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("회원 정보 파일을 불러오는 중 오류 발생: " + e.getMessage());
        }
    }

    void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                bw.write(user.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("회원 정보 저장 중 오류 발생: " + e.getMessage());
        }
    }


    public void signUp(Scanner scanner) {
        System.out.println("\n=== 회원가입 ===");

        System.out.print("이름: ");
        String name = scanner.nextLine();

        System.out.print("아이디: ");
        String id = scanner.nextLine();

        if (isIdDuplicate(id)) {
            System.out.println("이미 존재하는 아이디입니다.");
            return;
        }

        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        System.out.print("전화번호: ");
        String phone = scanner.nextLine();

        if (isPhoneDuplicate(phone)) {
            System.out.println("이미 등록된 전화번호입니다.");
            return;
        }

        User newUser = new User(name, id, password, phone);
        users.add(newUser);
        saveUsers();
        System.out.println("=======================================");
        System.out.println("회원가입이 완료되었습니다! 로그인 메뉴로 돌아갑니다.");
        System.out.println("=======================================");
    }


    public boolean login(Scanner scanner) {
        System.out.println("\n=== 로그인 ===");

        System.out.print("아이디: ");
        String id = scanner.nextLine();

        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(id) && user.getPassword().equals(password)) {
                loggedInUser = user;
                return true;
            }
        }

        return false;
    }

    public void saveUsers(List<User> userList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : userList) {
                bw.write(user.toDataString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isIdDuplicate(String id) {
        return users.stream().anyMatch(user -> user.getUsername().equals(id));
    }

    private boolean isPhoneDuplicate(String phone) {
        return users.stream().anyMatch(user -> user.getPhone().equals(phone));
    }
}