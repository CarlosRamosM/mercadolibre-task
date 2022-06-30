package mx.com.ml.rebell.alliance.message.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ml.rebell.alliance.message.dto.UserSecurityDto;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserContextHolder {

  private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

  public static void setUser(final UserSecurityDto user) {
    if (user == null) {
      return;
    }
    final String jsonPayload = user.asJson().toString();
    log.info("Storing user Json Payload: {}", jsonPayload);
    CONTEXT.set(jsonPayload);
  }

  public void unload() {
    CONTEXT.remove();
  }
}
