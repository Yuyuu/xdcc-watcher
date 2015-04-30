package watcher.irc.bot;

import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.User;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class Security {

  private Security() {}

  static boolean isABot(User user) {
    return BOT_PREFIXES.stream().anyMatch(prefix -> user.getNick().startsWith(prefix));
  }

  static boolean isSourceABot(DccFileTransfer transfer) {
    return LEGIT_SOURCE_PREFIXES.stream().anyMatch(prefix -> transfer.getNick().startsWith(prefix));
  }

  private static List<String> BOT_PREFIXES = newArrayList(
      "%[SeriaL]",
      "%[DarksiDe]",
      "%[Darkside]",
      "%iNFEXiOUS"
  );
  private static List<String> LEGIT_SOURCE_PREFIXES = newArrayList(
      "[SeriaL]Xdcc`",
      "[DarksiDe]`",
      "[Darkside]`",
      "iNFEXiOUS`"
  );
}
