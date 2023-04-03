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

import com.idea.recon.entities.Contractor;
import com.idea.recon.entities.Role;
import com.idea.recon.repositories.ContractorRepository;

@Service
public class JwtContractorDetailsService implements UserDetailsService {
	
	@Autowired
	ContractorRepository contractorRepository;
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Contractor contractor = contractorRepository.getByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found."));
		logger.info(contractor.getRole().getName());
		return new User(
				contractor.getEmail(),
				contractor.getPassword(),
				mapRoleToAuthority(contractor.getRole())
		);
	}
	
	public Contractor loadTraineeByEmail(String email) {
		Contractor contractor = contractorRepository.getByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username not found."));
		return contractor;
	}
	
	private Collection<GrantedAuthority> mapRoleToAuthority(Role role) {
		logger.info("mapRoleForContractor: " + role.getName());
    	ArrayList<GrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority(role.getName()));
    	return authorities;
    			
    }

}
