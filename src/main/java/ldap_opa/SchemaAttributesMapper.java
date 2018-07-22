package ldap_opa;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.HashSet;
import java.util.Set;

public class SchemaAttributesMapper implements AttributesMapper<Set<Schema>> {
    private Set set = new HashSet();
    private final LdapTemplate ldapTemplate;

    public SchemaAttributesMapper(LdapTemplate ldapTemplate) {
        super();
        this.ldapTemplate = ldapTemplate;
    }

    public Set<Schema> mapFromAttributes(Attributes attributes) throws NamingException {
        Attribute systemMustContain = attributes.get("systemMustContain");
        Attribute systemMayContain = attributes.get("systemMayContain");
        Attribute mustContain = attributes.get("mustContain");
        Attribute mayContain = attributes.get("mayContain");

        // Read-only
        strip(systemMustContain, true);
        strip(systemMayContain, true);
        // ReadWrite
        strip(mustContain, false);
        strip(mayContain, false);

        return set;
    }

    private void strip(Attribute attribute, Boolean readOnly) throws NamingException {
        if (attribute != null) {
            NamingEnumeration<?> e = attribute.getAll();
            while (e.hasMore()) {
                String name = (String)e.next();
                Schema schema = new Schema();
                schema.setName(name);
                schema.setReadOnly(readOnly);
                schema.setReadWrite(!readOnly);
                //TODO: Introspect further to get data type and multiple values
                set.add(schema);
            }
        }
    }
}
