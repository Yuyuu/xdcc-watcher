package watcher.model.bot;

import com.google.common.base.MoreObjects;
import fr.vter.xdcc.model.EntityWithObjectId;
import org.bson.types.ObjectId;

import java.util.Date;

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

  public void checked() {
    lastChecked = new Date();
  }

  public void updated() {
    lastUpdated = new Date();
  }

  public String getName() {
    return name;
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
  private Date lastChecked;
  private Date lastUpdated;
  private long schemaVersion = SCHEMA_VERSION;

  public static final long SCHEMA_VERSION = 1;
}
