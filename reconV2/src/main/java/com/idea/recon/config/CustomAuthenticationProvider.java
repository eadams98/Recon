package com.idea.recon.config;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


	@Autowired
	@Qualifier("jwtTraineeDetailsService")
	private UserDetailsService jwtTraineeDetailsService;
	@Autowired
	@Qualifier("jwtContractorDetailsService")
	private UserDetailsService jwtContractorDetailsService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        logger.info("mapping auth provider"); 

        // Check the requested URL to determine which UserDetailsService to use
        if (request.getRequestURI().startsWith("/trainee")) {
            UserDetails userDetails = jwtTraineeDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        } else {
            UserDetails userDetails = jwtContractorDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
