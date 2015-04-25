package watcher.worker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sanitizer {

  static String sanitizeBotNickname(String nickname) {
    matcher.reset(nickname);
    return matcher.replaceAll("").toLowerCase();
  }

  private static final Matcher matcher = Pattern.compile("[`\\[\\]]").matcher("");
}
