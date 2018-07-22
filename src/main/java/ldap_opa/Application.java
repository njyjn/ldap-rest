package ldap_opa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathBeanPostProcessor;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableLdapRepositories
public class Application {

    private final String basePath = "DC=intra,DC=aletheon,DC=co";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ContextSource contextSource() {
      LdapContextSource ldapContextSource = new LdapContextSource();
      ldapContextSource.setUrl("ldap://104.198.15.126:389");
      ldapContextSource.setBase(basePath);
      ldapContextSource.setUserDn("CN=workato,CN=Users,DC=intra,DC=aletheon,DC=co");
      ldapContextSource.setPassword("N$R=GQ;N$.:tSK5");
      ldapContextSource.setReferral("follow");
      Map map = new HashMap();
      map.put("java.naming.ldap.attributes.binary", "objectSid");
      map.put("java.naming.ldap.attributes.binary", "objectGUID");
      ldapContextSource.setBaseEnvironmentProperties(map);
      return ldapContextSource;
    }

    @Bean
    public BaseLdapPathBeanPostProcessor ldapPathBeanPostProcessor(){
        BaseLdapPathBeanPostProcessor baseLdapPathBeanPostProcessor = new BaseLdapPathBeanPostProcessor();
        baseLdapPathBeanPostProcessor.setBasePath(basePath);
        return baseLdapPathBeanPostProcessor;
    }

    @Bean
    LdapTemplate ldapTemplate(ContextSource contextSource) {
      return new LdapTemplate(contextSource);
    }

    @Bean
    SharedService sharedService(EntryRepo entryRepo, GroupRepo groupRepo, SchemaRepo schemaRepo) {
        SharedService ss = new SharedService(entryRepo, groupRepo, schemaRepo);
        ss.setDirectoryType(DirectoryType.AD);
        ss.setBaseLdapPath(LdapUtils.newLdapName(basePath));
        return ss;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
