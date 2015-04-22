package fr.vter.xdcc.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public abstract class GenericEntity<TId> implements Entity<TId> {

  protected GenericEntity() {}

  protected GenericEntity(TId id) {
    this.id = id;
  }

  @Override
  public TId getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GenericEntity<?> that = (GenericEntity<?>) o;
    return Objects.equal(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .toString();
  }

  private TId id;
}
