package ldap_opa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.ldap.odm.annotations.*;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.support.LdapUtils;

import javax.naming.Name;


@Entry(objectClasses = { "organizationalPerson", "person", "top", "user" })
public class User {

    @JsonIgnore
    @Id
    private Name id;

    @Attribute(name = "cn")
    @DnAttribute(value="cn", index=1)
    private String fullName;

    @Attribute(name = "givenName")
    private String firstName;

    @Attribute(name = "sn")
    private String lastName;

    @Attribute(name = "mail")
    private String mail;

    @Attribute(name = "unicodePwd")
    private String password;

    @Attribute(name = "distinguishedName")
    private String distinguishedName;

    @DnAttribute(value="cn", index=0)
    @Transient
    private String rootCommonName;

    @Attribute(name = "userAccountControl")
    private Integer userAccountControl;

    public String getRootCommonName() {
        return rootCommonName;
    }

    public void setRootCommonName(String rootCommonName) {
        this.rootCommonName = rootCommonName;
    }

    public Name getId() {
        return id;
    }

    public void setId(Name id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = LdapUtils.newLdapName(id);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() { return mail; }

    public void setMail(String mail) { this.mail = mail; }

    public Integer getUserAccountControl() { return userAccountControl; }

    public void setUserAccountControl(Integer userAccountControl) { this.userAccountControl = userAccountControl; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }
}
