package com.idea.recon.dtos;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private String token;
	private String type = "Bearer";
	private String refreshToken;
	private Integer id;
	private String username;
	private String email;
	private Collection<? extends GrantedAuthority> roles; //List<String> roles;

	public JwtResponse(String accessToken, String refreshToken, Integer integer, String username, String email, Collection<? extends GrantedAuthority> collection) {
		this.token = accessToken;
		this.refreshToken = refreshToken;
		this.id = integer;
		this.username = username;
		this.email = email;
		this.roles = collection;
	}
}
