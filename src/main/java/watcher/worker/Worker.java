package watcher.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watcher.model.RepositoryLocator;
import watcher.model.bot.Bot;
import watcher.model.bot.Pack;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class Worker {

  protected void updatePacks(Bot bot, Map<Long,String> packDataFromExternalSource) {
    final Set<Pack> packs = packDataFromExternalSource.entrySet().stream()
        .map(entry -> new Pack(entry.getKey(), entry.getValue()))
        .collect(Collectors.toSet());

    if (!packs.equals(bot.packs())) {
      bot.updatePacks(packs);
      LOGGER.info("Bot {} has new packs", bot.nickname());
    } else {
      LOGGER.debug("Packs of bot {} remain unchanged", bot.nickname());
    }

    bot.checked();
  }

  protected Bot getBotWithNickname(String nickname) {
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

  private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);
}
