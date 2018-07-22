package ldap_opa;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.query.LdapQuery;

import javax.naming.ldap.LdapName;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class SchemaRepoImpl implements SchemaRepoExtension, BaseLdapNameAware {
    private final LdapTemplate ldapTemplate;
    private LdapName baseLdapPath;

    public SchemaRepoImpl(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    @Override
    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }

    @Override
    public Set getSchemaFromObjectClasses(List<String> objectClasses) {
        Set set = new HashSet();
        for (String clazz : objectClasses) {
            LdapQuery ldapQuery = query().where("objectclass").is("classSchema").
                        and("cn").is(clazz);
            set.addAll(ldapTemplate.search(ldapQuery, new SchemaAttributesMapper(ldapTemplate)).get(0));
        }
        return set;
    }
}
