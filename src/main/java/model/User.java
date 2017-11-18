package model;

import java.io.Serializable;

public class User implements Serializable {
  private static final long serialVersionUID = 6271507457262106974L;

  private String username;
  private String password;
  private String salt;
  private boolean canCreate;
  private boolean canEdit;
  private boolean canDelete;
  private boolean canSearch;
  private boolean alreadyLoggedOn;
  private boolean isAdmin;
  private long dateOfCreation;
  private long lastUpdated;

  public User(String username, String password, boolean canCreate, boolean canEdit,
      boolean canDelete, boolean canSearch, boolean isAdmin) {
    this.username = username;
    this.password = password;
    this.canCreate = canCreate;
    this.canEdit = canEdit;
    this.canDelete = canDelete;
    this.canSearch = canSearch;
    this.isAdmin = isAdmin;
  }

  public User(String username, String password, boolean canCreate, boolean canEdit,
      boolean canDelete, boolean canSearch, boolean isAdmin, String salt) {
    this.username = username;
    this.password = password;
    this.canCreate = canCreate;
    this.canEdit = canEdit;
    this.canDelete = canDelete;
    this.canSearch = canSearch;
    this.isAdmin = isAdmin;
    this.salt = salt;
  }

  public User(String username, String password, boolean canCreate, boolean canEdit,
      boolean canDelete, boolean canSearch, boolean isAdmin, String salt, boolean alreadyLoggedOn,
      long dateOfCreation, long lastUpdated) {
    this.username = username;
    this.password = password;
    this.canCreate = canCreate;
    this.canEdit = canEdit;
    this.canDelete = canDelete;
    this.canSearch = canSearch;
    this.isAdmin = isAdmin;
    this.salt = salt;
    this.alreadyLoggedOn = alreadyLoggedOn;
    this.dateOfCreation = dateOfCreation;
    this.lastUpdated = lastUpdated;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isCanCreate() {
    return canCreate;
  }

  public void setCanCreate(boolean canCreate) {
    this.canCreate = canCreate;
  }

  public boolean isCanEdit() {
    return canEdit;
  }

  public void setCanEdit(boolean canEdit) {
    this.canEdit = canEdit;
  }

  public boolean isCanDelete() {
    return canDelete;
  }

  public void setCanDelete(boolean canDelete) {
    this.canDelete = canDelete;
  }

  public boolean isCanSearch() {
    return canSearch;
  }

  public void setCanSearch(boolean canSearch) {
    this.canSearch = canSearch;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public boolean isAlreadyLoggedOn() {
    return alreadyLoggedOn;
  }

  public void setAlreadyLoggedOn(boolean alreadyLoggedOn) {
    this.alreadyLoggedOn = alreadyLoggedOn;
  }

  public long getDateOfCreation() {
    return dateOfCreation;
  }

  public void setDateOfCreation(long dateOfCreation) {
    this.dateOfCreation = dateOfCreation;
  }

  public void updateFields(String username, String password, boolean canCreate, boolean canEdit,
      boolean canDelete, boolean canSearch, boolean isAdmin) {
    this.username = username;
    this.password = password;
    this.canCreate = canCreate;
    this.canEdit = canEdit;
    this.canDelete = canDelete;
    this.canSearch = canSearch;
    this.isAdmin = isAdmin;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAdmin(boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public long getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(long lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
}
