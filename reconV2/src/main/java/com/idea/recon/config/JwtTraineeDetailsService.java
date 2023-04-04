package com.idea.recon.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.Role;
import com.idea.recon.entities.Trainee;
import com.idea.recon.entities.TraineeLogin;
import com.idea.recon.repositories.TraineeLoginRepository;

@Service
public class JwtTraineeDetailsService implements UserDetailsService {
	
	@Autowired
	TraineeLoginRepository traineeLoginRepository;

	//@Autowired
	//private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TraineeLogin traineeLogin = traineeLoginRepository.getTraineeLoginByEmailId(username).orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    	return new User(
    			traineeLogin.getTrainee().getEmail(),
    			traineeLogin.getPassword(),
    			mapRoleToAuthority(traineeLogin.getTrainee().getRole())
    	);
	}
	
	public TraineeLogin loadTraineeByEmail(String email) {
		TraineeLogin traineeLogin = traineeLoginRepository.getTraineeLoginByEmailId(email).orElseThrow(() -> new UsernameNotFoundException("Username not found."));
		return traineeLogin;
	}
	
	private Collection<GrantedAuthority> mapRoleToAuthority(Role role) {
    	ArrayList<GrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority(role.getName()));
    	return authorities;
    			
    }
	
	// FOR REGISTRATION: TO BE DONE
	public TraineeLogin save(TraineeDTO user) {
		Trainee newTrainee = new Trainee();
		TraineeLogin newTraineeLogin = new TraineeLogin();
		//newUser.setUsername(user.getUsername());
		//newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return traineeLoginRepository.save(newTraineeLogin);
	}

}
