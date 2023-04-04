package com.idea.recon.config;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.idea.recon.entities.Role;
import com.idea.recon.entities.School;
import com.idea.recon.exceptions.SchoolException;
import com.idea.recon.repositories.SchoolRepository;

@Service
public class JwtSchoolDetailsService implements UserDetailsService {

	@Autowired
	SchoolRepository schoolRepository;
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		School school = schoolRepository.getByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found."));
				
		return new User(
				school.getEmail(),
				school.getPassword(),
				mapRoleToAuthority(school.getRole())
		);
	}
	
	public School loadSchoolByEmail(String eamil) throws SchoolException {
		School school = schoolRepository.getByEmail(eamil).orElseThrow(() -> new SchoolException("School.NOT_FOUND"));
		return school;
	}
	
	private Collection<GrantedAuthority> mapRoleToAuthority(Role role) {
		logger.info("mapRoleForContractor: " + role.getName());
    	ArrayList<GrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority(role.getName()));
    	return authorities;
    			
    }

}
