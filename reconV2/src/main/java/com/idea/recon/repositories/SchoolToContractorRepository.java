package com.idea.recon.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.idea.recon.entities.Contractor;
import com.idea.recon.entities.School;
import com.idea.recon.entities.SchoolToContractor;
import com.idea.recon.entities.Trainee;

public interface SchoolToContractorRepository extends JpaRepository<SchoolToContractor, Integer>  {

	@Query("SELECT c FROM School_to_Contractor s RIGHT JOIN s.contractor c WHERE s.id IS NULL OR s.school != ?1")
	//@Query("SELECT c FROM Contractor c LEFT JOIN School_to_Contractor s ON s.contractor = c AND s.school = :school WHERE s.id IS NULL")
	Set<Contractor> getContractorsNotAssignedToSchool(School school);
	
	@Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
	           "FROM School_to_Contractor s " +
	           "WHERE s.school = :schoolId AND s.contractor = :contractorId")
	Boolean contractorIsRegisteredToMySchool(@Param("schoolId") School schoolId, @Param("contractorId") Contractor contractorId);
	 
}
