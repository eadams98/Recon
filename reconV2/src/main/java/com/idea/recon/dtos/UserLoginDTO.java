package com.idea.recon.dtos;

import lombok.Data;

@Data
public class UserLoginDTO {
	Integer login_id;
	String email;
    String password;
    String recovery_question;
    String recovery_answer;
    Boolean first_login;
}
