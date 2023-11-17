package com.virtualteacher.helpers;

import com.virtualteacher.models.User;
import com.virtualteacher.models.dto.UserDto;
import com.virtualteacher.models.dto.UserDtoUpdate;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User fromDto(UserDto userDto){
        User user =  new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(user.getPassword());
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
