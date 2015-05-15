package watcher.worker;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watcher.model.RepositoryLocator;
import watcher.model.bot.Bot;
import watcher.model.bot.BotRepository;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BotWorker {

  public void updateAvailableBots(List<String> namesOfBotsInChannel) {
    final List<Bot> botsInRepository = RepositoryLocator.bots().findAllWithoutLoadingPacks();

    addNewBots(botsInRepository, namesOfBotsInChannel);
    removeObsoleteBots(botsInRepository, namesOfBotsInChannel);
  }

  private static void addNewBots(List<Bot> botsInRepository, List<String> namesOfBotsInChannel) {
    final List<String> namesOfBotsInRepository = botsInRepository.stream()
        .map(Bot::nickname)
        .collect(Collectors.toList());

    BotRepository repository = RepositoryLocator.bots();
    namesOfBotsInChannel.stream().forEach(nickname -> {
      if (namesOfBotsInRepository.contains(nickname)) {
        Bot bot = botsInRepository.stream()
            .filter(b -> b.nickname().equals(nickname))
            .findFirst().orElseThrow(IllegalStateException::new);
        if (bot.dateOfFirstUnavailability() != null) {
          bot.available();
        }
      } else {
        LOGGER.info("Adding a new bot: {}", nickname);
        repository.add(new Bot(nickname));
      }
    });
  }

  private static void removeObsoleteBots(List<Bot> botsInRepository, List<String> namesOfBotsInChannel) {
    final List<Bot> unavailableBots = botsInRepository.stream()
        .filter(bot -> !namesOfBotsInChannel.contains(bot.nickname()))
        .collect(Collectors.toList());

    unavailableBots.stream().forEach(removeIfNeeded());
  }

  private static Consumer<Bot> removeIfNeeded() {
    BotRepository repository = RepositoryLocator.bots();
    return bot -> {
      DateTime date = bot.dateOfFirstUnavailability();
      if (date == null) {
        bot.unavailable();
      } else if (Days.daysBetween(date, DateTime.now()).getDays() >= 7) {
        LOGGER.info("Removing {} which has been unavailable for more than a week", bot.nickname());
        repository.remove(bot);
      }
    };
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(BotWorker.class);
}
