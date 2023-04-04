package com.idea.recon.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Trainee {
	
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Integer traineeId;
    private String firstName;
    private String lastName;
    @Column(name = "email_id", unique = true)
    private String email;
    
    //@JsonIgnore
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
   // @JoinColumn(name = "supervisor_id", referencedColumnName = "id")
    private Contractor supervisor;
    
    //Could be a list in future but for now just one
    @ManyToOne
    @JoinColumn(name= "role_id")
    private Role role; 
    
    @OneToMany(mappedBy="trainee")
    private List<SchoolToTrainee> school; // ONLY GOING TO BE ONE SCHOOL. HAVE TO RESTRICT WITH SQL AS WELL AS IMPL
   
}
