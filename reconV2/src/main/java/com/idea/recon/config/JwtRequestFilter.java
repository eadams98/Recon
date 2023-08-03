package com.idea.recon.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.idea.recon.exceptions.JwtTokenValidationException;

import io.jsonwebtoken.ExpiredJwtException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	@Qualifier("jwtTraineeDetailsService")
	private UserDetailsService jwtTraineeDetailsService;
	@Autowired
	@Qualifier("jwtContractorDetailsService")
	private UserDetailsService jwtContractorDetailsService;
	@Autowired
	@Qualifier("jwtSchoolDetailsService")
	private UserDetailsService jwtSchoolDetailsService;


	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private HandlerExceptionResolver handlerExceptionResolver;
	
	/*
	@Autowired
	public JwtRequestFilter(HandlerExceptionResolver handlerExceptionResolver) {
 
        this.handlerExceptionResolver = handlerExceptionResolver;
    }*/

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
			throws ServletException, IOException { 

		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			//logger.info ("role: " + jwtTokenUtil.getRoleFromToken(jwtToken));
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
				//throw new JwtTokenValidationException(e.getMessage());
				handlerExceptionResolver.resolveException(request, response, null, new JwtTokenValidationException("Unable to get JWT token"));
				return;
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
				handlerExceptionResolver.resolveException(request, response, null, new JwtTokenValidationException("Expired JWT token"));
				return;
				//throw new RuntimeException(new JwtTokenValidationException("Expired JWT token")); // throw as runtime exception
			} catch (Exception e) {
				System.out.println("general exception JWT Token");
				handlerExceptionResolver.resolveException(request, response, null, e);
				return;
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		System.out.println("JWT Token has expired AFTER");
		
		//if(request.getRequestURI().startsWith("/bucket") && jwtTokenUtil.getRoleFromToken(jwtToken) == "")
		
		//Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			logger.info(request.getRequestURI());
			UserDetails userDetails;
			if (request.getRequestURI().startsWith("/trainee") && !request.getRequestURI().startsWith("/trainee/unregistered") ||
					(request.getRequestURI().startsWith("/bucket") && jwtTokenUtil.getRoleFromToken(jwtToken).equals("trainee"))) {
				try {
					logger.info("JwtRequestFilter: use TraineeDetailsService"); 
		            userDetails = jwtTraineeDetailsService.loadUserByUsername(username);
				} catch( Exception ex) {
					handlerExceptionResolver.resolveException(request, response, null, ex);
					return;
	        	}
	        } else if (request.getRequestURI().startsWith("/school") || request.getRequestURI().startsWith("/trainee/unregistered") || request.getRequestURI().startsWith("/contractor/unregistered-to-school") ||
	        		(request.getRequestURI().startsWith("/bucket") && jwtTokenUtil.getRoleFromToken(jwtToken).equals("school"))) { 
	        	try {
		        	logger.info("JwtRequestFilter: use SchoolDetailsService");  
		            userDetails = jwtSchoolDetailsService.loadUserByUsername(username);
	        	} catch (Exception ex) {
					handlerExceptionResolver.resolveException(request, response, null, ex);
					return;
	        	}
	        } else {
	        	try {
		        	logger.info("JwtRequestFilter: use ContractorDetailsService");  
		            userDetails = jwtContractorDetailsService.loadUserByUsername(username);
	        	} catch (Exception ex) {
					handlerExceptionResolver.resolveException(request, response, null, ex);
					return;
	        	}
	        }

			// if token is valid configure Spring Security to manually set authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			logger.info(userDetails.getAuthorities().toString());
		}
		chain.doFilter(request, response);
	}

}
