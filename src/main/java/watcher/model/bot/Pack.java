package watcher.model.bot;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import fr.vter.xdcc.model.EntityWithObjectId;
import org.bson.types.ObjectId;

public class Pack implements EntityWithObjectId {

  protected Pack() {}

  public Pack(long position, String title) {
    id = ObjectId.get();
    this.position = position;
    this.title = title;
  }

  @Override
  public ObjectId getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pack pack = (Pack) o;
    return Objects.equal(position, pack.position) &&
        Objects.equal(title, pack.title);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(position, title);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("position", position)
        .add("title", title)
        .toString();
  }

  private ObjectId id;
  private long position;
  private String title;
}
