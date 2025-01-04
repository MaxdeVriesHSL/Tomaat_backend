package tomaat.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import java.security.NoSuchAlgorithmException;

public class PasswordService {
//    public static String createTemporaryPassword() {
//        return RandomStringUtils.random(7, true, true);
//    }
//
//    public static String createVerifyCode() {
//        return RandomStringUtils.random(10, true, true);
//    }

    public static String createHashPassword(String password, String salt) throws NoSuchAlgorithmException {
        return BCrypt.hashpw(password, salt);
    }

    public static String createSalt() {
        return BCrypt.gensalt();
    }
}
