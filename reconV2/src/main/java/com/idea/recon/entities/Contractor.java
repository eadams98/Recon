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
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Data
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supervisor")
    @JsonManagedReference
    //@JoinColumn(name = "supervisor_id")
    private List<Trainee> trainees;

   
}

