package com.idea.recon.dtos;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.idea.recon.entities.SchoolToTrainee;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonSerialize(include = Inclusion.NON_NULL)
public class SchoolDTO {
	private Integer schoolId;
    private String schoolName;
    private String email;
    private List<SchoolToTrainee> students;
}
