package ldap_opa;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class EntryController {

    @Autowired
    private SharedService sharedService;

    @RequestMapping(value = "/v1/entry", method = GET)
    public List<Entry> search(@RequestParam(required = false) String filter,
                              @RequestParam(required = false) Integer limit) {
        return Lists.newArrayList(sharedService.searchEntries(filter, limit));
    }

    @RequestMapping(value = "/v1/entry/{dn}", method = GET)
    public Entry get(@PathVariable String dn) {
        return sharedService.findEntry(dn); // cn=workato,cn=Users
    }

    @RequestMapping(value = "/v1/entry/new", method = POST)
    public Entry create(@RequestBody Entry entry) {
        return sharedService.createEntry(entry);
    }

    @RequestMapping(value = "/v1/entry/update", method = POST)
    public Entry update(@RequestBody Entry entry) {
        return sharedService.updateEntry(entry);
    }

    @RequestMapping(value = "/v1/user/{dn}/changePwd", method = POST)
    public String changeUserPassword(@PathVariable String dn, @RequestBody String password) {
        String success = sharedService.changeUserPassword(dn, password).toString();
        return "{ \"success\": " + success + " }";
    }

    @RequestMapping(value = "/v1/user/{dn}/resetPwd", method = POST)
    public String resetUserPassword(@PathVariable String dn) {
        String success = sharedService.resetUserPassword(dn).toString();
        return "{ \"success\": " + success + " }";
    }

    @RequestMapping(value = "/v1/group", method = POST)
    public Entry createGroup(Entry group) {
        return sharedService.createGroup(group);
    }

    @RequestMapping(value = "/v1/group/{dn}/addUser", method = POST)
    public Entry addUserToGroup(@PathVariable String dn, @RequestBody RawRequest userDn) {
        return sharedService.addUserToGroup(dn, userDn.getValue());
    }

    @RequestMapping(value = "/v1/group/{dn}/removeUser", method = DELETE)
    public Entry removeUserFromGroup(@PathVariable String dn, @RequestBody RawRequest userDn) {
        return sharedService.removeUserFromGroup(dn, userDn.getValue());
    }

//    @RequestMapping(value = "/groups/{name}/ownership", method = POST)
//    public Group changeOwnershipOfGroup(@PathVariable String name, @RequestBody RawRequest userId) {
//        try {
//            Group group = groupRepo.findByName(name);
//            group.setManagedBy(sharedService.toAbsoluteDn(userId.getValue()));
//            groupRepo.save(group);
//            return sharedService.convertToGroupResult(group);
//        } catch (Exception e) {
//            GroupResult gr = new GroupResult();
//            gr.setStatus(512);
//            gr.setMessage(e.toString());
//            return gr;
//        }
//    }
//
//    @RequestMapping(value = "/groups/{name}/ownership", method = DELETE)
//    public Group removeOwnershipOfGroup(@PathVariable String name) {
//        try {
//            Group group = groupRepo.findByName(name);
//            group.removeManagedBy();
//            groupRepo.save(group);
//            return sharedService.convertToGroupResult(group);
//        } catch (Exception e) {
//            GroupResult gr = new GroupResult();
//            gr.setStatus(512);
//            gr.setMessage(e.toString());
//            return gr;
//        }
//    }
    // Method to enable users: RIP and WIP
//    @RequestMapping(value = "/users/{userid}/enable", method = POST)
//    public String enableUser(@PathVariable String userid) {
//        final int ACCOUNT_ENABLE = -2;
//        String success = sharedService.changePassword(userid, password).toString();
//        return "{ \"success\": " + success + " }";
//    }

}
