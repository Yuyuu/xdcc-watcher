package watcher.infrastructure.persistence.memory;

import com.google.common.collect.Lists;
import fr.vter.xdcc.infrastructure.persistence.memory.MemoryRepository;
import watcher.model.bot.Bot;
import watcher.model.bot.BotRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MemoryBotRepository extends MemoryRepository<Bot> implements BotRepository {

  @Override
  public long count() {
    return entities.size();
  }

  @Override
  public List<Bot> getAllWithoutLoadingPacks() {
    return Lists.newArrayList(entities);
  }

  @Override
  public Optional<Bot> findByNickname(String nickname) {
    return entities.stream()
        .filter(entity -> entity.nickname().equals(nickname))
        .findFirst();
  }

  @Override
  public void addAll(Collection<Bot> bots) {
    entities.addAll(bots);
  }

  @Override
  public void removeAll(Collection<Bot> bots) {
    entities.removeAll(bots);
  }

  @Override
  public List<Bot> paginate(int max, int offset) {
    return entities.stream().skip(offset).limit(max).collect(Collectors.toList());
  }
}
