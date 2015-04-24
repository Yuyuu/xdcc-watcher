package watcher.worker;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watcher.model.RepositoryLocator;
import watcher.model.bot.Bot;
import watcher.model.bot.Pack;
import watcher.worker.parser.ListFileParser;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ListFileWorker {

  @Inject
  public ListFileWorker(ListFileParser listFileParser) {
    this.listFileParser = listFileParser;
  }

  public void updateAvailablePacks(String botNickname, File packsFile) {
    Map<Long, String> packDataFromFile = listFileParser.parsePacksFrom(packsFile);

    Set<Pack> packs = packDataFromFile.entrySet().stream()
        .map(entry -> new Pack(entry.getKey(), entry.getValue()))
        .collect(Collectors.toSet());

    Bot bot = getBotWithNickname(botNickname);

    if (!packs.equals(bot.packs())) {
      bot.updatePacks(packs);
      LOGGER.info("Bot {} has new packs", bot.getNickname());
    } else {
      LOGGER.debug("Packs of bot {} remain unchanged", bot.getNickname());
    }

    bot.checked();
  }

  private Bot getBotWithNickname(String nickname) {
    final Optional<Bot> optional = RepositoryLocator.bots().findByNickname(nickname);
    return optional.orElseGet(createBot(nickname));
  }

  private Supplier<Bot> createBot(String botNickname) {
    return () -> {
      final Bot result = new Bot(botNickname);
      RepositoryLocator.bots().add(result);
      return result;
    };
  }

  private final ListFileParser listFileParser;
  private static final Logger LOGGER = LoggerFactory.getLogger(ListFileWorker.class);
}
