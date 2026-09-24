package es.upm.miw.devops.dto;

public class UserActiveDto {

    private String id;
    private Boolean active;

    public UserActiveDto() {
    }

    public String getId() {
        return id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
