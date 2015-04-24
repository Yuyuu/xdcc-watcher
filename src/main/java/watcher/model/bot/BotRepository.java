package watcher.model.bot;

import fr.vter.xdcc.model.Repository;

import java.util.Optional;

public interface BotRepository extends Repository<Bot> {

  Optional<Bot> findByNickname(String nickname);
}
