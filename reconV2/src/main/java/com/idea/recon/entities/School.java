package com.idea.recon.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
//@NoArgsConstructor
public class School {
    @Id
    @GeneratedValue(
        strategy = GenerationType.AUTO
    )
    private Integer schoolId;
    private String schoolName;
    private String password;
    @Column(name = "email_id")
    private String email;
    
    @OneToMany(mappedBy = "school")
    private List<SchoolToTrainee> students;
    
    @OneToMany(mappedBy = "school")
    private List<SchoolToContractor> contractors;
    
    @ManyToOne
    @JoinColumn(name= "role_id")
    Role role;

}
