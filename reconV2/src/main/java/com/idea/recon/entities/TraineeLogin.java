package com.idea.recon.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class TraineeLogin {
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    Integer login_id;
    String password;
    String recovery_question;
    String recovery_answer;
    Boolean first_login;
    @Column(name = "email_id", unique = true)
    private String email;
    @OneToOne
    @JoinColumn(name = "user_id")
    Trainee trainee;

    
    public TraineeLogin(final Integer login_id, final String password, final String recovery_question, final String recovery_answer, final Boolean first_login) {
        this.login_id = login_id;
        this.password = password;
        this.recovery_question = recovery_question;
        this.recovery_answer = recovery_answer;
        this.first_login = first_login;
    }

    public TraineeLogin() {
    }
}
