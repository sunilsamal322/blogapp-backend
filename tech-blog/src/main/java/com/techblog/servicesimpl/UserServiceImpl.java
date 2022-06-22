package com.techblog.servicesimpl;

import com.techblog.dto.UserDto;
import com.techblog.model.User;
import com.techblog.repository.UserRepository;
import com.techblog.services.UserServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserServices {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {

        User user=this.modelMapper.map(userDto,User.class);
        user.setAccountCreatedTime(Instant.now());
        this.userRepository.save(user);
        return this.modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user=this.userRepository.findById(id).get();
        return this.modelMapper.map(user,UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users=this.userRepository.findAll();
        List<UserDto> userDtos=users.stream().map(user -> this.modelMapper.map(user,UserDto.class)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer id) {
        User user=this.userRepository.findById(id).get();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return this.modelMapper.map(user,UserDto.class);
    }

    @Override
    public void deleteUser(Integer id) {
        User user=this.userRepository.findById(id).get();
        this.userRepository.delete(user);
    }
}
