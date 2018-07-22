package ldap_opa;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.naming.Name;
import javax.naming.ldap.LdapName;
import java.security.SecureRandom;
import java.util.List;
import java.util.Set;

public class SharedService implements BaseLdapNameAware {
    private final EntryRepo entryRepo;
    private final GroupRepo groupRepo;
    private final SchemaRepo schemaRepo;
    private LdapName baseLdapPath;
    private DirectoryType directoryType;

    @Autowired
    public SharedService(EntryRepo entryRepo, GroupRepo groupRepo, SchemaRepo schemaRepo) {
        this.entryRepo = entryRepo;
        this.groupRepo = groupRepo;
        this.schemaRepo = schemaRepo;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Group getUserGroup() {
        return groupRepo.findByName(GroupRepo.USER_GROUP);
    }

    public void setDirectoryType(DirectoryType directoryType) {
        this.directoryType = directoryType;
    }

    @Override
    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }

    public Iterable<Entry> searchEntries(String filter, Integer limit) {
        if (StringUtils.isEmpty(filter)) {
            return entryRepo.getAllEntries();
        } else {
            return entryRepo.searchEntries(filter,limit);
        }
    }

    public Entry findEntry(String dn) {
        return entryRepo.findOne(LdapUtils.newLdapName(dn));
    }

    public Entry createEntry(Entry entry) {
        entryRepo.create(entry);
        return findEntry(entry.getDistinguishedName());
    }

    public Entry updateEntry(Entry entry) {
        entryRepo.update(entry);
        return findEntry(entry.getDistinguishedName());
    }

    public Boolean changeUserPassword(String userDn, String password) {
        try {
            entryRepo.updateUserPassword(userDn, passwordEncoder.encode(password));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean resetUserPassword(String userDn) {
        try {
            SecureRandom sr = new SecureRandom();
            byte bytes[] = sr.generateSeed(20);
            sr.nextBytes(bytes);
            entryRepo.updateUserPassword(userDn, passwordEncoder.encode(bytes.toString()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Entry createGroup(Entry group) {
        groupRepo.create(group);
        return findEntry(group.getDistinguishedName());
    }

    public Entry addUserToGroup(String groupDn, String userDn) {
        groupRepo.addUser(groupDn, userDn);
        return findEntry(groupDn);
    }

    public Entry removeUserFromGroup(String groupDn, String userDn) {
        groupRepo.removeUser(groupDn, userDn);
        return findEntry(groupDn);
    }

    public LdapName toAbsoluteDn(Name relativeName) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add(relativeName)
                .build();
    }

    public String toAbsoluteDn(String relativeName) {
        return relativeName+","+baseLdapPath;
    }

    /**
     * This method expects absolute DNs of group members. In order to findEntry the actual users
     * the DNs need to have the base LDAP path removed.
     *
     * @param absoluteIds
     * @return
     */
    public Set<Entry> findAllMembers(Iterable<Name> absoluteIds) {
        return Sets.newLinkedHashSet(entryRepo.findAll(toRelativeIds(absoluteIds)));
    }

    public Iterable<Name> toRelativeIds(Iterable<Name> absoluteIds) {
        return Iterables.transform(absoluteIds, new Function<Name, Name>() {
            @Override
            public Name apply(Name input) {
                return LdapUtils.removeFirst(input, baseLdapPath);
            }
        });
    }

//    /**
//     * Update the user and - if its id changed - update all group references to the user.
//     *
//     * @param originalId the original id of the user.
//     * @param existingUser the user, populated with new data
//     *
//     * @return the updated entry
//     */
//    private User updateUserStandard(LdapName originalId, Entry existingUser) {
//        Entry savedUser = entryRepo.save(existingUser);
//
//        if(!originalId.equals(savedUser.getId())) {
//            // The user has moved - we need to update group references.
//            LdapName oldMemberDn = toAbsoluteDn(originalId);
//            LdapName newMemberDn = toAbsoluteDn(savedUser.getId());
//
//            Collection<Group> groups = groupRepo.findByMember(oldMemberDn);
//            updateGroupReferences(groups, oldMemberDn, newMemberDn);
//        }
//        return savedUser;
//    }
//
//    /**
//     * Special behaviour in AD forces us to get the group membership before the user is updated,
//     * because AD clears group membership for removed entries, which means that once the user is
//     * update we've lost track of which groups the user was originally member of, preventing us to
//     * update the membership references so that they point to the new DN of the user.
//     *
//     * This is slightly less efficient, since we need to get the group membership for all updates
//     * even though the user may not have been moved. Using our knowledge of which attributes are
//     * part of the distinguished name we can do this more efficiently if we are implementing specifically
//     * for Active Directory - this approach is just to highlight this quite significant difference.
//     *
//     * @param originalId the original id of the user.
//     * @param existingUser the user, populated with new data
//     *
//     * @return the updated entry
//     */
//    private User updateUserAd(LdapName originalId, User existingUser) {
//        LdapName oldMemberDn = toAbsoluteDn(originalId);
//        Collection<Group> groups = groupRepo.findByMember(oldMemberDn);
//
//        User savedUser = entryRepo.save(existingUser);
//        LdapName newMemberDn = toAbsoluteDn(savedUser.getId());
//
//        if(!originalId.equals(savedUser.getId())) {
//            // The user has moved - we need to update group references.
//            updateGroupReferences(groups, oldMemberDn, newMemberDn);
//        }
//        return savedUser;
//    }
//
//    private void updateGroupReferences(Collection<Group> groups, Name originalId, Name newId) {
//        for (Group group : groups) {
//            group.removeMember(originalId);
//            group.addMember(newId);
//
//            groupRepo.save(group);
//        }
//    }
//
//    public List<Entry> searchByNameName(String lastName) {
//        return entryRepo.findByFullNameContains(lastName);
//    }
//
//

    public GroupResult convertToGroupResult(Group group) {
        GroupResult gr = new GroupResult();
        BeanUtils.copyProperties(group, gr);
        return gr;
    }

    public Set getSchemas(List l) {
        return schemaRepo.getSchemaFromObjectClasses(l);
    }
}
