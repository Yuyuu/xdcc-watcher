package watcher.model.bot;

import fr.vter.xdcc.model.Repository;

import java.util.List;
import java.util.Optional;

public interface BotRepository extends Repository<Bot> {

  long count();

  List<Bot> findAllWithoutLoadingPacks();

  Optional<Bot> findByNickname(String nickname);

  List<Bot> paginateWithoutLoadingPacks(int max, int offset);
}
