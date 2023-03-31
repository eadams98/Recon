package com.idea.recon.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.idea.recon.dtos.UserLoginDTO;
import com.idea.recon.entities.Role;
import com.idea.recon.entities.TraineeLogin;
import com.idea.recon.repositories.TraineeLoginRepository;
import com.idea.recon.services.TraineeLoginService;

@Service
public class TraineeLoginServiceImpl implements TraineeLoginService {
	
    @Autowired
    TraineeLoginRepository traineeLoginRepository;
    private static final Logger LOGGER = LogManager.getLogger();

    public TraineeLoginServiceImpl() {
    }

    // PRE SPRING SECURITY
    public String validateUserLogin(Long id, String password) {
        Optional<TraineeLogin> potentialUserLogin = traineeLoginRepository.getTraineeLoginByUserId(id);
        LOGGER.info(potentialUserLogin);
        
        if (potentialUserLogin.isPresent()) {
            return ((TraineeLogin)potentialUserLogin.get()).getPassword().equals(password) ? "Login Succussful" : "Incorrect Password for given Id";
        } else {
            return "No User Found with given ID";
        }
    }
    
    
}