package es.upm.miw.devops.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.upm.miw.devops.model.Role;
import es.upm.miw.devops.model.User;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("familyName")
    private String familyName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("identity")
    private String identity;
    @JsonProperty("address")
    private String address;
    @JsonProperty("city")
    private String city;
    @JsonProperty("province")
    private String province;
    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Role role;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean active;
    @JsonProperty(value = "billable", access = JsonProperty.Access.READ_ONLY)
    private Boolean isBillable;

    public UserDto() {
    }

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
    public void setName(String name) {
        this.name = name;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
