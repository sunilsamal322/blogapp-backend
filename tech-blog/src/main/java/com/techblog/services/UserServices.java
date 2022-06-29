package com.techblog.services;

import com.techblog.dto.UserDto;

import java.util.List;

public interface UserServices {

    UserDto createUser(UserDto userDto);

    UserDto createAdminUser(UserDto userDto,String secretCode);

    UserDto getUserById(Integer id);

    List<UserDto> getAllUsers();

    UserDto updateUser(UserDto userDto,Integer id);

    void deleteUser(Integer id);

}
