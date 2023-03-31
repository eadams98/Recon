package com.idea.recon.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idea.recon.entities.Trainee;
import com.idea.recon.entities.TraineeLogin;

public interface UserRepositories extends JpaRepository<Trainee, Integer> {
	
	@Query(
    	value = "Select * from Trainee t WHERE t.email_id = ?1",
    	nativeQuery = true
    )
    Optional<Trainee> getByEmailId(String email_id);
	
}
