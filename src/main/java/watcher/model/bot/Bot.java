package watcher.model.bot;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import fr.vter.xdcc.model.EntityWithObjectId;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import java.util.Set;

public class Bot implements EntityWithObjectId {

  @SuppressWarnings("unused")
  protected Bot() {}

  @Override
  public ObjectId getId() {
    return _id;
  }

  public Bot(String nickname) {
    _id = ObjectId.get();
    this.nickname = nickname;
  }

  public void updatePacks(Set<Pack> packSet) {
    this.packs = packSet;
    updated();
  }

  public void checked() {
    lastChecked = new DateTime();
  }

  public void updated() {
    lastUpdated = new DateTime();
  }

  public void setListingUrl(String listingUrl) {
    this.listingUrl = listingUrl;
  }

  public boolean has(Pack pack) {
    return packs.contains(pack);
  }

  public String nickname() {
    return nickname;
  }

  public Set<Pack> packs() {
    return packs;
  }

  public String url() {
    return listingUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Bot bot = (Bot) o;
    return Objects.equal(_id, bot._id) &&
        Objects.equal(nickname, bot.nickname);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(_id, nickname);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", _id)
        .add("nickname", nickname)
        .toString();
  }

  private ObjectId _id;
  private String nickname;
  private Set<Pack> packs = Sets.newHashSet();
  private String listingUrl;
  private DateTime lastChecked;
  private DateTime lastUpdated;
  private long schemaVersion = SCHEMA_VERSION;

  public static final long SCHEMA_VERSION = 1;
}
