package ldap_opa;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class SchemaController {

    @Autowired
    private SharedService sharedService;

    @RequestMapping(value = "/schema", method = GET)
    public Set getSchemaFromObjectClass(@RequestParam(required = true) String objectClasses) {
        List l = Lists.newArrayList(objectClasses.split(","));
        return sharedService.getSchemas(l);
    }

}