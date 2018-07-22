package ldap_opa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.LdapName;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class GroupRepoImpl implements GroupRepoExtension, BaseLdapNameAware {
    private final static LdapName ADMIN_USER = LdapUtils.newLdapName("cn=workato,cn=Users,dc=intra,dc=aletheon,dc=co");

    private final LdapTemplate ldapTemplate;
    private LdapName baseLdapPath;

    @Autowired
    public GroupRepoImpl(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    @Override
    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }

    @Override
    public List<String> getAllGroupNames() {
        LdapQuery query = query().attributes("cn")
                .where("objectclass").is("group");

        return ldapTemplate.search(query, new AttributesMapper<String>() {
            @Override
            public String mapFromAttributes(Attributes attributes) throws NamingException {
                return (String) attributes.get("cn").get();
            }
        });
    }

    @Override
    public void create(Entry group) {
        // A groupOfNames cannot be empty - add a system entry to all new groups.
        group.addAttribute("member", LdapUtils.prepend(ADMIN_USER, baseLdapPath));
        ldapTemplate.create(group);
    }

    @Override
    public void addUser(String groupDn, String userDn) {
        Name groupName = LdapUtils.newLdapName(groupDn);
        DirContextOperations context = ldapTemplate.lookupContext(groupName);
        Name userName = LdapUtils.newLdapName(userDn);
        context.addAttributeValue("member", LdapUtils.prepend(userName, baseLdapPath));
        ldapTemplate.modifyAttributes(context);
    }

    @Override
    public void removeUser(String groupDn, String userDn) {
        Name groupName = LdapUtils.newLdapName(groupDn);
        DirContextOperations context = ldapTemplate.lookupContext(groupName);
        Name userName = LdapUtils.newLdapName(userDn);
        context.removeAttributeValue("member", LdapUtils.prepend(userName, baseLdapPath));
        ldapTemplate.modifyAttributes(context);
    }

    public LdapName toAbsoluteDn(Name relativeName) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add(relativeName)
                .build();
    }


}
