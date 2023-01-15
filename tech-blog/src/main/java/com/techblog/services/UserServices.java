package com.techblog.services;

import com.techblog.dto.PasswordRequest;
import com.techblog.dto.UserDto;

import java.util.List;

public interface UserServices {

    UserDto createUser(UserDto userDto);

    UserDto createAdminUser(UserDto userDto);

    UserDto getUserById(Integer id);

    List<UserDto> getAllUsers();

    UserDto updateUser(UserDto userDto,Integer id);

    void deleteUser(Integer id);

    boolean changePassword(Integer id, PasswordRequest passwordRequest);

}
