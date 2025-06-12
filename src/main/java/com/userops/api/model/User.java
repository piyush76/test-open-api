package com.userops.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User work context information")
public class User {
    
    @Schema(description = "Personnel ID", example = "3")
    private Long personnelId;
    
    @Schema(description = "Company ID", example = "HostCompany")
    private String companyId;
    
    @Schema(description = "User type (Incora/Customer)", example = "Customer")
    private String userType;
    
    @Schema(description = "Work context (Hub/Facility)", example = "Facility")
    private String workContext;
    
    @Schema(description = "Hub or Facility ID", example = "HUB001")
    private String contextId;
    
    @Schema(description = "Administrative role")
    private AdminRole adminRole;
}
