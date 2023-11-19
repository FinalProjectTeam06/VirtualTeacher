package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.models.Role;
import com.example.finalprojectvirtualteacher.models.Topic;
import com.example.finalprojectvirtualteacher.repositories.contracts.RoleRepository;
import com.example.finalprojectvirtualteacher.services.contacts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.getAll();
    }

    @Override
    public Role getById(int id) {
        return roleRepository.getById(id);
    }
}
