package ldap_opa;

import java.util.Map;

public class Entry {
    private String fullName;
    private String distinguishedName;
    private Map<String,Object> attributes;

    public Entry() { }

    public Entry(String distinguishedName, Map<String, Object> attributes) {
        this.distinguishedName = distinguishedName;
        this.attributes = attributes;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void addAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }
}
