package ldap_opa;

public class Schema {
    private String name;
    private String ldapType;
    private Boolean readWrite;
    private Boolean readOnly;
    private Boolean multiple;

    public Schema() {};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getReadWrite() {
        return readWrite;
    }

    public void setReadWrite(Boolean readWrite) {
        this.readWrite = readWrite;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getLdapType() {
        return ldapType;
    }

    public void setLdapType(String ldapType) {
        this.ldapType = ldapType;
    }

    public Boolean getMultiple() {
        return multiple;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }
}
