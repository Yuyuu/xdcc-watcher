package fr.vter.xdcc.model.bot;

import fr.vter.xdcc.model.GenericEntityWithObjectId;

import java.util.Date;

public class Bot extends GenericEntityWithObjectId {
  
  protected Bot() {}

  public Bot(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Date getLastChecked() {
    return lastChecked;
  }

  public void setLastChecked(Date lastChecked) {
    this.lastChecked = lastChecked;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public long getSchemaVersion() {
    return schemaVersion;
  }

  private String name;
  private Date lastChecked;
  private Date lastUpdated;
  private long schemaVersion = SCHEMA_VERSION;

  public static final long SCHEMA_VERSION = 1;
}
