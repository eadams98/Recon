package com.idea.recon.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RelationshipVerificationDTO {
	private String byName;
	private String forName;
	private String byEmail;
	private String forEmail;
	private Integer byId;
	private Integer forId;
}
