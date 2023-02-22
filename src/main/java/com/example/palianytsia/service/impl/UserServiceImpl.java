package com.example.palianytsia.service.impl;

import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.exception.*;
import com.example.palianytsia.model.User;
import com.example.palianytsia.model.UserRoles;
import com.example.palianytsia.repository.RoleRepository;
import com.example.palianytsia.repository.UserRepository;
import com.example.palianytsia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

import static com.example.palianytsia.dto.Mapper.toUserDTO;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        super();
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
    }

    @Override
    public void signUp(UserDTO userDTO) throws ServiceException {
        User user=userRepository.findByEmail(userDTO.getEmail());
        if (user==null) {
            log.info("No such user!");
            log.info("Name:"+userDTO.getFirstName());
            user=new User()
                    .setFirstName(userDTO.getFirstName())
                    .setLastName(userDTO.getLastName())
                    .setEmail(userDTO.getEmail())
                    .setPassword(passwordEncoder.encode(userDTO.getPassword()))
                    .setMobileNumber(userDTO.getMobileNumber())
                    .setRoles(new HashSet<>(Arrays.asList(roleRepository.findByRole(UserRoles.USER))));
            log.info("Name of user: "+user.getFirstName());
            userRepository.save(user);
        } else {
        log.info("User already exists");
        throw new DuplicatedEmailException(); }
    }

    @Override
    public UserDTO signIn(UserDTO userDTO) throws ServiceException {
        User user=userRepository.findByEmail(userDTO.getEmail());
        if(user!=null) {
            if(passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                return toUserDTO(user);
            } else {
                log.info("Wrong password!");
                throw new WrongPasswordException();
            }
        } else {
            log.info("No such user!");
            throw new EntityNotFoundException(ExcConstants.NO_USER);
        }
    }

    @Override
    public UserDTO findByEmail(String email) {
        return null;
    }


}
