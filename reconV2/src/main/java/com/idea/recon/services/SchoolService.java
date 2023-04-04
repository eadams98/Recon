package com.idea.recon.services;

import java.util.Set;

import com.idea.recon.dtos.SchoolDTO;
import com.idea.recon.dtos.TraineeDTO;
import com.idea.recon.entities.School;
import com.idea.recon.exceptions.SchoolException;

public interface SchoolService {

	Set<TraineeDTO> getStudents(Integer id, String token) throws SchoolException;
	SchoolDTO getMyDetails(Integer id, String token) throws SchoolException;
	String updateMyDetails(SchoolDTO updateInfo, String token) throws SchoolException;
	School loadSchoolByEmail(String eamil) throws SchoolException;
}
