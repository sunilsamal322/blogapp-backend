package com.techblog.servicesimpl;

import com.techblog.dto.UserDto;
import com.techblog.exception.EmailAlreadyExistException;
import com.techblog.exception.ResourceNotFoundException;
import com.techblog.helper.AppConstants;
import com.techblog.model.Role;
import com.techblog.model.User;
import com.techblog.repository.RoleRepoitory;
import com.techblog.repository.UserRepository;
import com.techblog.services.UserServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserServices {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepoitory roleRepoitory;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDto createUser(UserDto userDto) {

        if(userRepository.findByEmail(userDto.getEmail()).isPresent())
        {
            throw new EmailAlreadyExistException("Email already exists");
        }
        User user=modelMapper.map(userDto,User.class);
        user.setAccountCreatedTime(new Date());
        Role role=roleRepoitory.findById(AppConstants.NORMAL_USER).get();
        Set<Role> roles=new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("user","id",id));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users=userRepository.findAll();
        List<UserDto> userDtos=users.stream().map(user -> modelMapper.map(user,UserDto.class)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer id) {
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("user","id",id));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        if(!user.getEmail().equals(userDto.getEmail()))
        {
            if(userRepository.findByEmail(userDto.getEmail()).isPresent())
            {
                throw new EmailAlreadyExistException("This Email id already associated with other user");
            }
            user.setEmail(userDto.getEmail());
        }
        user.setPassword(userDto.getPassword());
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public void deleteUser(Integer id) {
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("user","id",id));
        userRepository.delete(user);
    }
}
