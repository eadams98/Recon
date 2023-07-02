package com.idea.recon.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.idea.recon.entities.School;
import com.idea.recon.entities.SchoolToTrainee;
import com.idea.recon.entities.Trainee;

public interface SchoolToTraineeRepository extends JpaRepository<SchoolToTrainee, Integer> {
	
	@Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
	           "FROM School_to_Trainee s " +
	           "WHERE s.school = :schoolId AND s.trainee = :traineeId")
	Boolean traineeIsRegisteredToMySchool(@Param("schoolId") School schoolId, @Param("traineeId") Trainee traineeId);
	
	/*@Query(value = "Select t.* FROM School_to_Trainee stt "
			+ "RIGHT OUTER JOIN Trainee t ON stt.trainee_id = t.trainee_id "
			+ "WHERE stt.school_id is NULL", nativeQuery = true)*/
	@Query("SELECT t FROM Trainee t LEFT JOIN t.school s WHERE s IS NULL")
	Set<Trainee> getTraineesNotAssinedToSchool();

}