package com.idea.recon.dtos;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode
@Getter
@Setter
@JsonSerialize(include = Inclusion.NON_NULL)
public class TraineeDTO {
	private Integer traineeId;
	private String email;
    private String firstName;
    private String lastName;
    private ContractorDTO supervisor;
    
}
