package eu.europeana.uim.model.europeana;

import java.io.Serializable;

import org.theeuropeanlibrary.model.common.FieldId;

public class EuropeanaRedirectId implements Serializable {
  private static final long serialVersionUID = -6212905967307455736L;
  @FieldId(1)
  private String newId;
  @FieldId(2)
  private String oldId;
  
  public EuropeanaRedirectId(String newId, String oldId) {
    super();
    this.newId = newId;
    this.oldId = oldId;
  }
  public String getNewId() {
    return newId;
  }
  public void setNewId(String newId) {
    this.newId = newId;
  }
  public String getOldId() {
    return oldId;
  }
  public void setOldId(String oldId) {
    this.oldId = oldId;
  }

  
}
