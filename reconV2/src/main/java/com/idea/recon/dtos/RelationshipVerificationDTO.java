package com.idea.recon.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RelationshipVerificationDTO {
	private String byEmail;
	private String forEmail;
	private Integer byId;
	private Integer forId;
}
