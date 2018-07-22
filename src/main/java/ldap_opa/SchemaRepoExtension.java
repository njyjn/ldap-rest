package ldap_opa;

import java.util.List;
import java.util.Set;

public interface SchemaRepoExtension {
    Set getSchemaFromObjectClasses(List<String> objectClasses);
}
