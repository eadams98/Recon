package com.idea.recon.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;


import lombok.Data;

@Entity
@Data
@Table(name = "Contractor")
public class Contractor {
	
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Integer id;
    private String firstName;
    private String lastName;
    @Column(name = "email_id", unique = true)
    private String email;
    private String password;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supervisor")
    @JsonManagedReference
    //@JoinColumn(name = "supervisor_id")
    private List<Trainee> trainees;
    
    //Could be a list in future but for now just one
    @ManyToOne
    @JoinColumn(name= "role_id")
    private Role role;

   
}

