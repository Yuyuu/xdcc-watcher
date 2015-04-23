package watcher.model.bot;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import fr.vter.xdcc.model.EntityWithObjectId;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Set;

public class Bot implements EntityWithObjectId {

  @SuppressWarnings("unused")
  protected Bot() {}

  @Override
  public ObjectId getId() {
    return id;
  }

  public Bot(String name) {
    id = ObjectId.get();
    this.name = name;
  }

  public void updatePacks(Set<Pack> packSet) {
    this.packSet = packSet;
    updated();
  }

  public void checked() {
    lastChecked = new Date();
  }

  public void updated() {
    lastUpdated = new Date();
  }

  public boolean has(Pack pack) {
    return packSet.contains(pack);
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Bot bot = (Bot) o;
    return Objects.equal(id, bot.id) &&
        Objects.equal(name, bot.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, name);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("name", name)
        .toString();
  }

  private ObjectId id;
  private String name;
  private Set<Pack> packSet = Sets.newHashSet();
  private Date lastChecked;
  private Date lastUpdated;
  private long schemaVersion = SCHEMA_VERSION;

  public static final long SCHEMA_VERSION = 1;
}
