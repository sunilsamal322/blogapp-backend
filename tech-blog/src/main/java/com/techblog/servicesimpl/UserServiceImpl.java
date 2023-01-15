package com.techblog.servicesimpl;

import com.techblog.dto.PasswordRequest;
import com.techblog.dto.UserDto;
import com.techblog.exception.AdminCodeNotMatchException;
import com.techblog.exception.EmailAlreadyExistException;
import com.techblog.exception.ResourceNotFoundException;
import com.techblog.helper.AppConstants;
import com.techblog.model.Role;
import com.techblog.model.User;
import com.techblog.repository.RoleRepository;
import com.techblog.repository.UserRepository;
import com.techblog.security.JwtRequestFilter;
import com.techblog.services.UserServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    public UserDto createUser(UserDto userDto) {

        if(userRepository.findByEmail(userDto.getEmail().toLowerCase()).isPresent())
        {
            throw new EmailAlreadyExistException("Email already exists");
        }
        User user=modelMapper.map(userDto,User.class);
        user.setAccountCreatedTime(new Date());

        Role role=new Role();
        role.setId(AppConstants.NORMAL_USER);
        role.setName("ROLE_NORMAL");
        roleRepository.save(role);

        Set<Role> roles=new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail().toLowerCase());
        userRepository.save(user);
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto createAdminUser(UserDto userDto) {
        if(userDto.getSecretCode()==null)
        {
            throw new AdminCodeNotMatchException("Secret code required for sign up as admin");
        }
        if(!userDto.getSecretCode().equals(AppConstants.secretCodeForAdmin))
        {
            throw new AdminCodeNotMatchException("Secret code didn't match,you are not able to register as admin");
        }
        if(userRepository.findByEmail(userDto.getEmail().toLowerCase()).isPresent())
        {
            throw new EmailAlreadyExistException("Email already exists");
        }
        User user=modelMapper.map(userDto,User.class);
        user.setAccountCreatedTime(new Date());
        Role role=new Role();
        role.setId(AppConstants.ADMIN_USER);
        role.setName("ROLE_ADMIN");
        roleRepository.save(role);

        Set<Role> roles=new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
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
        if(!user.getEmail().equals(userDto.getEmail().toLowerCase()))
        {
            if(userRepository.findByEmail(userDto.getEmail().toLowerCase()).isPresent())
            {
                throw new EmailAlreadyExistException("This Email id already associated with other user");
            }
            user.setEmail(userDto.getEmail().toLowerCase());
        }
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

//        Set<RoleDto> roleDtoSet =userDto.getRoles();
//        List<Role> roleList=roleDtoSet.stream().map((roleDto -> modelMapper.map(roleDto,Role.class))).collect(Collectors.toList());
//
//        roleList.forEach((role)->{
//            if(!roleRepoitory.findById(role.getId()).isPresent())
//            {
//                Role newRole=new Role();
//                newRole.setId(role.getId());
//                newRole.setName(role.getName());
//                roleRepoitory.save(newRole);
//            }
//        });
//        user.setRoles(new HashSet<>(roleList));
        userRepository.save(user);
        return modelMapper.map(user,UserDto.class);
    }
    @Override
    public boolean changePassword(Integer id, PasswordRequest passwordRequest){
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("user","id",id));
        boolean passwordMatch=passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword());
        if(passwordMatch) {
            user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
            userRepository.save(user);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void deleteUser(Integer id) {
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("user","id",id));
        userRepository.delete(user);
    }
}
