package ldap_opa;

import org.springframework.data.ldap.repository.LdapRepository;

import javax.naming.Name;

public interface EntryRepo extends LdapRepository<Entry>, EntryRepoExtension {
    Entry findOne(Name dn);
    void create(Entry user);
    void update(Entry user);
}
