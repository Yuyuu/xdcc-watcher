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

  public Bot(String nickname) {
    id = ObjectId.get();
    this.nickname = nickname;
  }

  public void updatePacks(Set<Pack> packSet) {
    this.packs = packSet;
    updated();
  }

  public void checked() {
    lastChecked = new Date();
  }

  public void updated() {
    lastUpdated = new Date();
  }

  public void setListingUrl(String listingUrl) {
    this.listingUrl = listingUrl;
  }

  public boolean has(Pack pack) {
    return packs.contains(pack);
  }

  public String getNickname() {
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
    return Objects.equal(id, bot.id) &&
        Objects.equal(nickname, bot.nickname);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, nickname);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("nickname", nickname)
        .toString();
  }

  private ObjectId id;
  private String nickname;
  private Set<Pack> packs = Sets.newHashSet();
  private String listingUrl;
  private Date lastChecked;
  private Date lastUpdated;
  private long schemaVersion = SCHEMA_VERSION;

  public static final long SCHEMA_VERSION = 1;
}
