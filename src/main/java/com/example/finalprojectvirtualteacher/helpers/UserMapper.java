package com.example.finalprojectvirtualteacher.helpers;

import com.example.finalprojectvirtualteacher.models.dto.UserDto;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.services.contacts.RoleService;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final RoleService roleService;

    public UserMapper(RoleService roleService) {
        this.roleService = roleService;
    }

    public User fromDto(UserDto userDto){
        User user =  new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRole(roleService.getById(userDto.getRoleId()));
        user.setPassword(userDto.getPassword());
        user.setProfilePictureUrl("url");
        return user;
    }

    public UserDtoUpdate toDto(User user){
        UserDtoUpdate userDtoUpdate=new UserDtoUpdate();
        userDtoUpdate.setFirstName(user.getFirstName());
        userDtoUpdate.setLastName(user.getLastName());
        userDtoUpdate.setPasswordConfirm(user.getPassword());
        return userDtoUpdate;
    }


}
