package es.upm.miw.devops.model;

public class User {

    private String id;
    private String name;
    private String familyName;

    public User() {
    }

    public User(String id, String name, String familyName) {
        this.id = id;
        this.name = name;
        this.familyName = familyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}
