package watcher.model.bot;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.bson.types.ObjectId;

public class Pack {

  protected Pack() {}

  public Pack(long position, String title, ObjectId botId) {
    this.botId = botId;
    this.position = position;
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pack pack = (Pack) o;
    return Objects.equal(position, pack.position) &&
        Objects.equal(title, pack.title) &&
        Objects.equal(botId, pack.botId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(position, title, botId);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("position", position)
        .add("title", title)
        .toString();
  }

  private long position;
  private String title;
  private ObjectId botId;
}
