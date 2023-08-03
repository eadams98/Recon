package com.idea.recon.services;

import java.util.Set;

import com.idea.recon.dtos.SchoolDTO;
import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.School;
import com.idea.recon.exceptions.ContractorException;
import com.idea.recon.exceptions.SchoolException;
import com.idea.recon.exceptions.TraineeException;

public interface SchoolService {

	Set<TraineeDTO> getStudents(Integer id, String token) throws SchoolException;
	SchoolDTO getMyDetails(Integer id, String token) throws SchoolException;
	String updateMyDetails(SchoolDTO updateInfo, String token) throws SchoolException;
	School loadSchoolByEmail(String eamil) throws SchoolException;
	String RegisterStudent(Integer myId, String studentEmail, String token) throws SchoolException, TraineeException;
	School getSchoolByEmail(String email) throws SchoolException;
	String RegisterContractor(Integer id, String contractorEmail, String token) throws SchoolException, TraineeException, ContractorException;
}
