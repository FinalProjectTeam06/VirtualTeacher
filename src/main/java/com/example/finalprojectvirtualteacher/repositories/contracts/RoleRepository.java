package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Role;
import com.example.finalprojectvirtualteacher.models.Topic;

import java.util.List;

public interface RoleRepository {
    List<Role> getAll();

    Role getById(int id);
}
