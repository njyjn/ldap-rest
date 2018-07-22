package ldap_opa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapUtils;

import javax.naming.Name;
import java.util.List;
import java.util.Map;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class EntryRepoImpl implements EntryRepoExtension {

    private final LdapTemplate ldapTemplate;
    private final Integer DEFAULT_LIMIT = 1000;

    @Autowired
    public EntryRepoImpl(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    @Override
    public List<Entry> getAllEntries() {
        LdapQuery ldapQuery = query().where("objectclass").is("user");
        return ldapTemplate.search(ldapQuery, new EntryAttributesMapper());
    }

    @Override
    public List<Entry> searchEntries(String filter, Integer limit) {
        if (limit == null) {
            limit = DEFAULT_LIMIT;
        }
        LdapQuery ldapQuery = query().countLimit(limit).filter(filter);
        return ldapTemplate.search(ldapQuery, new EntryAttributesMapper());
    }

    public Entry findOne(Name dn) {
        return ldapTemplate.lookup(dn, new EntryAttributesMapper());
    }

    public void create(Entry user) {
        Name dn = LdapUtils.newLdapName(user.getDistinguishedName());
        DirContextAdapter context = new DirContextAdapter(dn);
        Map<String, Object> attributes = user.getAttributes();
        context.setAttributeValues("objectclass", new String[] {"top", "user", "person", "organizationalPerson"});
        for (Map.Entry<String,Object> entry : attributes.entrySet()) {
            context.setAttributeValue(entry.getKey(), entry.getValue());
        }
        ldapTemplate.bind(context);
    }

    public void update(Entry user) {
        Name dn = LdapUtils.newLdapName(user.getDistinguishedName());
        DirContextOperations context = ldapTemplate.lookupContext(dn);
        Map<String,Object> attributesToUpdate = user.getAttributes();
        for (Map.Entry<String,Object> entry : attributesToUpdate.entrySet()) {
            context.setAttributeValue(entry.getKey(), entry.getValue());
        }
        ldapTemplate.modifyAttributes(context);
    }

    public void updateUserPassword(String userDn, String encodedPwd) {
        Name dn = LdapUtils.newLdapName(userDn);
        DirContextOperations context = ldapTemplate.lookupContext(dn);
        context.setAttributeValue("userpassword", encodedPwd);
        ldapTemplate.modifyAttributes(context);
    }
}

