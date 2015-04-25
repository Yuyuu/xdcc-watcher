package watcher.worker;

import com.google.inject.Inject;
import watcher.model.bot.Bot;
import watcher.worker.parser.ListFileParser;

import java.io.File;
import java.util.Map;

public class ListFileWorker extends Worker {

  @Inject
  public ListFileWorker(ListFileParser listFileParser) {
    this.listFileParser = listFileParser;
  }

  public void updateAvailablePacks(String botNickname, File packsFile) {
    final Map<Long, String> packDataFromFile = listFileParser.parsePacksFrom(packsFile);

    final Bot bot = getBotWithNickname(botNickname);

    updatePacks(bot, packDataFromFile);
  }

  private final ListFileParser listFileParser;
}
