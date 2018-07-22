package ldap_opa;

import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class EntryAttributesMapper implements AttributesMapper<Entry> {

    public Entry mapFromAttributes(Attributes attributes) throws NamingException {
        Entry user = new Entry();
        user.setDistinguishedName((String)attributes.get("distinguishedName").get());

        Map<String,Object> attributesMap = new HashMap<>();
        NamingEnumeration<? extends Attribute> values = attributes.getAll();
        while (values.hasMore()) {
            Attribute vTop = values.next(); // Take out attribute
            if (vTop.size() > 1) {
                Collection childAttributesMap = new HashSet<Object>();
                NamingEnumeration childValues = vTop.getAll();
                while (childValues.hasMore()) {
                    Object vChild = childValues.next();
                    childAttributesMap.add(vChild);
                }
                attributesMap.put(vTop.getID(), childAttributesMap);
            } else {
//                if (vTop.getID().equalsIgnoreCase("objectguid") ||
//                        vTop.getID().equalsIgnoreCase("objectsid")){
//                    attributesMap.put(vTop.getID(),
//                            ByteStringDecoder.convertToByteString(vTop.get().toString().getBytes()));
//                } else
                    attributesMap.put(vTop.getID(), vTop.get());
            }
        }
        user.setAttributes(attributesMap);
        return user;
    }
}
