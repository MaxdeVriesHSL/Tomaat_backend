package tomaat.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import java.security.NoSuchAlgorithmException;

public class PasswordService {
    public static String createHashPassword(String password, String salt) throws NoSuchAlgorithmException {
        return BCrypt.hashpw(password, salt);
    }

    public static String createSalt() {
        return BCrypt.gensalt();
    }
}
