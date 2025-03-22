package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.entities.Group;
import fr.sirine.stock_management_back.exceptions.custom.GroupNotFountException;
import fr.sirine.stock_management_back.repository.GroupRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }
    public Group findById(Integer id) {
        return groupRepository.findById(id).orElseThrow(() -> new GroupNotFountException("Group not found"));
    }
}
