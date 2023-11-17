package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.Role;
import com.example.finalprojectvirtualteacher.models.Topic;

import java.util.List;

public interface RoleService {
    List<Role> getAll();
    Role getById(int id);
}
