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
import org.springframework.stereotype.Service;

import com.idea.recon.entities.Role;
import com.idea.recon.entities.TraineeLogin;
import com.idea.recon.repositories.TraineeLoginRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
	
	 @Autowired
	 TraineeLoginRepository traineeLoginRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // in all actuality it's email
		TraineeLogin traineeLogin = traineeLoginRepository.getTraineeLoginByEmailId(username).orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    	return new User(
    			traineeLogin.getTrainee().getEmail(),
    			traineeLogin.getPassword(),
    			mapRoleToAuthority(traineeLogin.getTrainee().getRole())
    	);
	}
	
	// SPRING SECURITY
    /* List version
    private Collection<GrantedAuthority> mapRoleToAuthority(List<Role> roles) {
    	return roles.stream()
    			.map(role -> new SimpleGrantedAuthority(role.getName()))
    			.collect(Collectors.toList());
    			
    }
    */
    
    private Collection<GrantedAuthority> mapRoleToAuthority(Role role) {
    	ArrayList<GrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority(role.getName()));
    	return authorities;
    			
    }
    

}
