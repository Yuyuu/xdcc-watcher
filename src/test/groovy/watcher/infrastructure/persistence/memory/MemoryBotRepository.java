package watcher.infrastructure.persistence.memory;

import fr.vter.xdcc.infrastructure.persistence.memory.MemoryRepository;
import watcher.model.bot.Bot;
import watcher.model.bot.BotRepository;

import java.util.Optional;

public class MemoryBotRepository extends MemoryRepository<Bot> implements BotRepository {

  @Override
  public Optional<Bot> findByNickname(String nickname) {
    return entities.stream()
        .filter(entity -> entity.nickname().equals(nickname))
        .findFirst();
  }
}
