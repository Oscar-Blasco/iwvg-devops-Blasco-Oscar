package es.upm.miw.devops.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    @NotBlank
    private String name;
    private String familyName;
    private String email;
    private String identity;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    @jakarta.persistence.Enumerated(jakarta.persistence.EnumType.STRING)
    private Role role;
    private Boolean active;

    protected User() {
    }

    public User(String id, String firstName, String familyName, String email, String identity,
                String address, String city, String province, String postalCode, Role role, Boolean active) {
        this.id = id;
        this.name = firstName;
        this.familyName = familyName;
        this.email = email;
        this.identity = identity;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.active = active;
        this.role = role;
    }

    public User(String id, String firstName, String familyName, String email, String identity,
                String address, String city, String province, String postalCode) {
        this(id, firstName, familyName, email, identity, address, city, province, postalCode, null, null);
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
    public Boolean getActive() { return active; }
    public Role getRole() { return role; }
    public boolean isBillable() {
        return hasContent(name)
                && hasContent(familyName)
                && hasContent(email)
                && hasContent(identity)
                && hasContent(address)
                && hasContent(city)
                && hasContent(province)
                && hasContent(postalCode);
    }

    private boolean hasContent(String value) {
        return value != null && !value.isBlank();
    }

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

    public void setActive(Boolean active) {
        this.active = active;
    }
}
