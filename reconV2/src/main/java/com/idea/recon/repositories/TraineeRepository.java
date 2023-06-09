package com.idea.recon.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idea.recon.entities.Trainee;
import com.idea.recon.entities.TraineeLogin;

public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
	
	@Query(
    	value = "Select * from trainee t WHERE t.email_id = ?1",
    	nativeQuery = true
    )
    Optional<Trainee> getByEmail(String email_id);
	
}
