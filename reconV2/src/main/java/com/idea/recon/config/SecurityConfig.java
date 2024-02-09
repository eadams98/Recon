package com.idea.recon.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.idea.recon.services.TraineeLoginService;
/* OLD
@Configuration
@EnableWebSecurity*/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	AuthenticationManagerBuilder builder;
	
	@Autowired
    private ObjectPostProcessor<Object> objectPostProcessor;

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
	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	CustomAuthenticationProvider mapperAuthProvider;
	
	@Autowired
	private final HandlerExceptionResolver handlerExceptionResolver;
	
	@Autowired
	public SecurityConfig(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		//auth.userDetailsService(jwtTraineeDetailsService).passwordEncoder(passwordEncoder());
		auth
        .authenticationProvider(mapperAuthProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Custom Auth Manager Start
	@Primary
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	 private AuthenticationManagerBuilder authenticationManagerBuilder() throws Exception {
	        return new AuthenticationManagerBuilder(objectPostProcessor);
	   }
	
	@Bean(name = "traineeAuthenticationManager")
    public AuthenticationManager traineeAuthenticationManager() throws Exception {
        AuthenticationManagerBuilder builder = authenticationManagerBuilder();
        builder.userDetailsService(jwtTraineeDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }
	
	@Bean(name = "contractorAuthenticationManager")
    public AuthenticationManager contractorAuthenticationManager() throws Exception {
        AuthenticationManagerBuilder builder = authenticationManagerBuilder();
        builder.userDetailsService(jwtContractorDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }
	
	@Bean(name = "schoolAuthenticationManager")
    public AuthenticationManager schoolAuthenticationManager() throws Exception {
        AuthenticationManagerBuilder builder = authenticationManagerBuilder();
        builder.userDetailsService(jwtSchoolDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }
	// Custom  Auth Manager End
	
	// Custom Auth Provider Start
	@Bean
	public AuthenticationProvider traineeAuthenticationProvider() {
	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setUserDetailsService(jwtTraineeDetailsService);
	    provider.setPasswordEncoder(passwordEncoder());
	    return provider;
	}
	
	@Bean
	public AuthenticationProvider contractorAuthenticationProvider() {
	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setUserDetailsService(jwtContractorDetailsService);
	    provider.setPasswordEncoder(passwordEncoder());
	    return provider;
	}
	// Custom Auth Provider End

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.cors().and().csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests().antMatchers("/user/authenticate/**", "/register", "/user/test", "/refresh/**", "/bucket/**", "/trainee/unregistered", "/contractor/unregistered-to-school").permitAll()
				
				// all other requests need to be authenticated
					.anyRequest().authenticated().and()
				// make sure we use stateless session; session won't be used to
				// store user's state.
						.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        logger.info("configuration: " + configuration.getAllowedOrigins());
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
	/* OLD
	// MYSQL
	@Autowired
	private CustomUserDetailService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/auth/api/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.httpBasic();
	}
	
	// MYSQL

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    */
	
	
	
}
