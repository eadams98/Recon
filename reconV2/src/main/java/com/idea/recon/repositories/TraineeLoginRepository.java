package com.idea.recon.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idea.recon.entities.TraineeLogin;

public interface TraineeLoginRepository extends JpaRepository<TraineeLogin, Integer> {
    @Query(
        value = "Select * from trainee_login where user_id = ?1",
        nativeQuery = true
    )
    Optional<TraineeLogin> getTraineeLoginByUserId(Long user_id);
    
    @Query(
    	value = "Select * from trainee_Login tl JOIN Trainee t ON tl.user_id = t.trainee_id WHERE t.email_id = ?1",
    	nativeQuery = true
    )
    Optional<TraineeLogin> getTraineeLoginByEmailId(String email_id); 
}