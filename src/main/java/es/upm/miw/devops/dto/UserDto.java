package es.upm.miw.devops.dto;

import es.upm.miw.devops.model.User;

public class UserDto {

    private final String id;
    private final String name;
    private final String familyName;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.familyName = user.getFamilyName();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFamilyName() {
        return familyName;
    }
}