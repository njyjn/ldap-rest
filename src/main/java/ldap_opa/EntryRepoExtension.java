package ldap_opa;

import java.util.List;

public interface EntryRepoExtension {
    List<Entry> getAllEntries();
    List<Entry> searchEntries(String filter, Integer limit);
    void updateUserPassword(String userDn, String encodedPwd);
}
