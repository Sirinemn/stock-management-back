package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.entities.Group;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.exceptions.custom.GroupNotFountException;
import fr.sirine.stock_management_back.repository.GroupRepository;
import fr.sirine.stock_management_back.service.IGroupService;
import fr.sirine.stock_management_back.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class GroupService implements IGroupService {
    private final GroupRepository groupRepository;
    private final IUserService userService;

    public GroupService(GroupRepository groupRepository, IUserService userService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }
    public Group findById(Integer id) {
        return groupRepository.findById(id).orElseThrow(() -> new GroupNotFountException("Group not found"));
    }
    public void updateGroup(Integer userId, String groupName) {
        User user = userService.findById(userId);
        Group group = findById(user.getGroup().getId());
        group.setName(groupName);
        groupRepository.save(group);
    }
}
