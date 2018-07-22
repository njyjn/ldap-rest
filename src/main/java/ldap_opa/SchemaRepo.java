package ldap_opa;

import org.springframework.data.ldap.repository.LdapRepository;

public interface SchemaRepo extends LdapRepository<Schema>, SchemaRepoExtension { }
