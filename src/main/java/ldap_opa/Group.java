package ldap_opa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.ldap.odm.annotations.*;
import org.springframework.ldap.odm.annotations.Entry;

import javax.naming.Name;
import java.util.HashSet;
import java.util.Set;

@Entry(objectClasses = {"group", "top"})
public class Group {

    @JsonIgnore
    @Id
    private Name id;

    @DnAttribute(value="cn", index=1)
    @Attribute(name = "cn")
    private String name;

    @Attribute(name = "description")
    private String description;

    @JsonIgnore
    @Attribute(name = "member")
    private Set<Name> members = new HashSet<Name>();

    @Attribute(name = "member")
    private Set<String> memberNames = new HashSet<String>();

    @Attribute(name = "managedBy")
    private String managedBy;

    @JsonIgnore
    @DnAttribute(value="cn", index=0)
    @Transient
    private String rootCommonName;

    public String getRootCommonName() {
        return rootCommonName;
    }

    public void setRootCommonName(String rootCommonName) {
        this.rootCommonName = rootCommonName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Name> getMembers() {
        return  members;
    }

    public Set<String> getMemberNames() { return memberNames; };

    public void addMember(Name newMember) {
        members.add(newMember);
        memberNames.add(newMember.toString());
    }

    public void removeMember(Name member) {
        members.remove(member);
        memberNames.remove(member.toString());
    }

    public Name getId() {
        return id;
    }

    public void setId(Name id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManagedBy() { return managedBy; }

    public void setManagedBy(String managedBy) { this.managedBy = managedBy; }

    public void removeManagedBy() { this.managedBy = null; };

}
