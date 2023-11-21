package com.example.finalprojectvirtualteacher.helpers;

import com.example.finalprojectvirtualteacher.models.dto.RegisterMvcDto;
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

    public User fromRegisterMvcDto(RegisterMvcDto registerMvcDto){
        User user=new User();
        user.setFirstName(registerMvcDto.getFirstName());
        user.setLastName(registerMvcDto.getLastName());
        user.setEmail(registerMvcDto.getEmail());

        if (registerMvcDto.isTeacher()){
            user.setRole(roleService.getById(2));
        } else{
            user.setRole(roleService.getById(1));
        }

        user.setPassword(registerMvcDto.getPassword());
        user.setProfilePictureUrl("url");

        return user;
    }


}
