package info.ivicel.springflasky;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestMain {

    public static void main(String[] args) {
        String password = "$2a$10$3tiA2Tbn0uBlYzmmNBhoKeFEkbIeJEUbMuFAlpGPrtadVUiLQvHgC";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.matches("123123", password));
    }
}
