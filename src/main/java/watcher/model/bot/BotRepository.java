package watcher.model.bot;

import fr.vter.xdcc.model.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BotRepository extends Repository<Bot> {

  long count();

  List<Bot> getAllWithoutLoadingPacks();

  Optional<Bot> findByNickname(String nickname);

  void addAll(Collection<Bot> bots);

  void removeAll(Collection<Bot> bots);

  List<Bot> paginate(int max, int offset);
}
