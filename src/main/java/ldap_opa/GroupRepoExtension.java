package ldap_opa;

import java.util.List;

public interface GroupRepoExtension {
    List<String> getAllGroupNames();
    void create(Entry group);
    void addUser(String groupDn, String userDn);
    void removeUser(String groupDn, String userDn);
}
