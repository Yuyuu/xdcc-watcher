package watcher.worker.parser;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListFileParser {

  public ListFileParser() {
    Pattern packPattern = Pattern.compile(PACK_ID_REGEX);
    patternToSplitLine = Pattern.compile(SPLIT_REGEX);
    packIdMatcher = packPattern.matcher("");
  }

  public Map<Long, String> parsePacksFrom(File file) {
    final Map<Long, String> packs = Maps.newHashMap();
    final Path pathToFile = FileSystems.getDefault().getPath(file.getAbsolutePath());

    LOGGER.debug("Parsing file: {}", file.getName());
    try {
      Files.lines(pathToFile)
          .filter(line -> line.trim().startsWith("#"))
          .map(this::extractPackEntry)
          .filter(pack -> pack != null)
          .forEach(pack -> packs.put(pack.id, pack.title));
    } catch (IOException e) {
      LOGGER.error("Failed to open {}: {}", file.getName(), e);
    }

    return packs;
  }

  private PackEntry extractPackEntry(String packLine) {
    PackEntry packEntry = null;
    final String[] splitPart = patternToSplitLine.split(packLine);

    if (splitPart.length != 2) {
      LOGGER.error("Cannot parse malformed line [{}]", packLine);
      return null;
    }

    packIdMatcher.reset(splitPart[0]);

    if (packIdMatcher.find()) {
      final String id = stripNumberSign(packIdMatcher.group());
      packEntry = new PackEntry(Long.parseLong(id), splitPart[1]);
    } else {
      LOGGER.error("No pack id match for line [{}]", packLine);
    }

    return packEntry;
  }

  private static String stripNumberSign(String id) {
    return id.substring(1);
  }

  private class PackEntry {

    public PackEntry(long id, String title) {
      this.id = id;
      this.title = title;
    }

    public long id;
    public String title;
  }

  private Pattern patternToSplitLine;
  private Matcher packIdMatcher;

  // Searches for the id of the pack declaration
  private static final String PACK_ID_REGEX = "#\\d+";
  // Searches for the end of the size declaration
  private static final String SPLIT_REGEX = "(?i)\\d{1,3}[KMG]\\]\\s";
  private static final Logger LOGGER = LoggerFactory.getLogger(ListFileParser.class);
}
