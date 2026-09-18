package es.upm.miw.devops.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.upm.miw.devops.model.Role;
import es.upm.miw.devops.model.User;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserDto {

    @JsonProperty("id")
    private final String id;
    @JsonProperty("name")
    private final String name;
    @JsonProperty("familyName")
    private final String familyName;
    @JsonProperty("email")
    private final String email;
    @JsonProperty("identity")
    private final String identity;
    @JsonProperty("address")
    private final String address;
    @JsonProperty("city")
    private final String city;
    @JsonProperty("province")
    private final String province;
    @JsonProperty("postalCode")
    private final String postalCode;
    @JsonProperty("role")
    private Role role;
    @JsonProperty("active")
    private final Boolean active;
    @JsonProperty("isBillable")
    private Boolean isBillable;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.familyName = user.getFamilyName();
        this.email = user.getEmail();
        this.identity = user.getIdentity();
        this.address = user.getAddress();
        this.city = user.getCity();
        this.province = user.getProvince();
        this.postalCode = user.getPostalCode();
        this.role = user.getRole();
        this.active = user.getActive();
        this.isBillable = user.isBillable();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getFamilyName() { return familyName; }
    public String getEmail() { return email; }
    public String getIdentity() { return identity; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getProvince() { return province; }
    public String getPostalCode() { return postalCode; }
    public Role getRole() { return role; }
    public Boolean getActive() { return active; }
    public Boolean isBillable() { return isBillable; }

}
