package ldap_opa;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class GroupController {

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private SharedService sharedService;

    @RequestMapping(value = "/groups", method = GET)
    public List<String> listGroups() {
        return Lists.newArrayList(groupRepo.getAllGroupNames());
    }
//
//    @RequestMapping(value = "/groups", method = POST)
//    public String newGroup(Group group) {
//        groupRepo.create(group);
//        return "redirect:groups/" + group.getName();
//    }
//
//    @RequestMapping(value = "/groups/{name}", method = GET)
//    public String editGroup(@PathVariable String name, ModelMap map) {
//        Group foundGroup = groupRepo.findByName(name);
//        map.put("group", foundGroup);
//        final Set<User> groupMembers = sharedService.findAllMembers(foundGroup.getMembers());
//        map.put("members", groupMembers);
//
//        Iterable<User> otherUsers = Iterables.filter(sharedService.searchEntries(), new Predicate<User>() {
//            @Override
//            public boolean apply(User user) {
//                return !groupMembers.contains(user);
//            }
//        });
//        map.put("nonMembers", Lists.newLinkedList(otherUsers));
//
//        return "editGroup";
//    }

    @RequestMapping(value = "/groups/{name}/", method = GET)
    public Group getGroup(@PathVariable String name) {
        try {
            Group group = groupRepo.findByName(name);
            return sharedService.convertToGroupResult(group);
        } catch (Exception e) {
            GroupResult gr = new GroupResult();
            gr.setStatus(512);
            gr.setMessage(e.toString());
            return gr;
        }
//        Group group = groupRepo.findByName(name);
//        return group;
    }


    @RequestMapping(value = "/groups/{name}/members", method = POST)
    public Group addUserToGroup(@PathVariable String name, @RequestBody RawRequest userId) {
        try{
            Group group = groupRepo.findByName(name);
            group.addMember(sharedService.toAbsoluteDn(LdapUtils.newLdapName(userId.getValue())));
            groupRepo.save(group);
            return sharedService.convertToGroupResult(group);
        } catch (Exception e) {
            GroupResult gr = new GroupResult();
            gr.setStatus(512);
            gr.setMessage(e.toString());
            return gr;
        }
//        Group group = groupRepo.findByName(name);
//        group.addMember(sharedService.toAbsoluteDn(LdapUtils.newLdapName(userId.getValue())));
//        groupRepo.save(group);
//        return group;
    }

    @RequestMapping(value = "/groups/{name}/members", method = DELETE)
    public Group removeUserFromGroup(@PathVariable String name, @RequestBody RawRequest userId) {
        try {
            Group group = groupRepo.findByName(name);
            group.removeMember(sharedService.toAbsoluteDn(LdapUtils.newLdapName(userId.getValue())));
            groupRepo.save(group);
            return sharedService.convertToGroupResult(group);
        } catch (Exception e) {
            GroupResult gr = new GroupResult();
            gr.setStatus(512);
            gr.setMessage(e.toString());
            return gr;
        }
//        Group group = groupRepo.findByName(name);
//        group.removeMember(sharedService.toAbsoluteDn(LdapUtils.newLdapName(userId.getValue())));
//        groupRepo.save(group);
//        return group;
    }

    @RequestMapping(value = "/groups/{name}/ownership", method = POST)
    public Group changeOwnershipOfGroup(@PathVariable String name, @RequestBody RawRequest userId) {
        try {
            Group group = groupRepo.findByName(name);
            group.setManagedBy(sharedService.toAbsoluteDn(userId.getValue()));
            groupRepo.save(group);
            return sharedService.convertToGroupResult(group);
        } catch (Exception e) {
            GroupResult gr = new GroupResult();
            gr.setStatus(512);
            gr.setMessage(e.toString());
            return gr;
        }
//        Group group = groupRepo.findByName(name);
//        group.setManagedBy(sharedService.toAbsoluteDn(userId.getValue()));
//        groupRepo.save(group);
//        return group;

    }

    @RequestMapping(value = "/groups/{name}/ownership", method = DELETE)
    public Group removeOwnershipOfGroup(@PathVariable String name) {
        try {
            Group group = groupRepo.findByName(name);
            group.removeManagedBy();
            groupRepo.save(group);
            return sharedService.convertToGroupResult(group);
        } catch (Exception e) {
            GroupResult gr = new GroupResult();
            gr.setStatus(512);
            gr.setMessage(e.toString());
            return gr;
        }
//        Group group = groupRepo.findByName(name);
//        group.removeManagedBy();
//        groupRepo.save(group);
//        return group;
    }
}
