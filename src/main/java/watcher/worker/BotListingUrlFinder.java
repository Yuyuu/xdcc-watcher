package watcher.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BotListingUrlFinder {

  public String findListingUrl(String botNickname, InputStream stream) throws IOException {
    String botListingUrl = null;

    try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      String inputLine;
      while ((inputLine = reader.readLine()) != null) {
        if (inputLine.contains(botNickname)) {
          botListingUrl = extractListingUrl(inputLine);
          break;
        }
      }
    }

    return botListingUrl;
  }

  private static String extractListingUrl(String urlLine) {
    return urlLine.substring(
        urlLine.indexOf("href=\"") + 6,
        urlLine.indexOf("/\">") + 1
    );
  }
}
