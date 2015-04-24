package watcher.worker.parser;

import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class WebsiteParser {

  public Map<Long, String> parsePacksFrom(InputStream stream) throws IOException {
    Map<Long, String> packs = Maps.newHashMap();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      String inputLine;
      while ((inputLine = reader.readLine()) != null) {
        if (inputLine.contains("Pack #")) {
          long packId = extractPackIdFrom(inputLine);

          String packTitleLine = reader.readLine();
          String packTitle = extractPackTitleFrom(packTitleLine);

          packs.put(packId, packTitle);
        }
      }
    }

    return packs;
  }

  private long extractPackIdFrom(String packIdLine) {
    String idAsString = packIdLine.substring(packIdLine.indexOf("#") + 1, packIdLine.indexOf("</td>"));
    return Long.parseLong(idAsString);
  }

  private String extractPackTitleFrom(String packTitleLine) {
    return packTitleLine.substring(packTitleLine.indexOf("title=\"") + 7, packTitleLine.indexOf("\">"));
  }
}
