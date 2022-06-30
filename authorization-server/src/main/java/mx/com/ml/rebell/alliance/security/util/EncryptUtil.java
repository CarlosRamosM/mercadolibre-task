package mx.com.ml.rebell.alliance.security.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class EncryptUtil {

  private EncryptUtil() {
  }

  public static String encrypt(final String key) {
    return BCrypt.hashpw(key, BCrypt.gensalt(12));
  }

  public static boolean validateKey(final String plainText, final String hashed) {
    return BCrypt.checkpw(plainText, hashed);
  }
}
