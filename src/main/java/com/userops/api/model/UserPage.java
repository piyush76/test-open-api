package com.userops.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User page assignment")
public class UserPage {
    
    @Schema(description = "User ID", example = "3")
    private String userId;
    
    @Schema(description = "Company ID", example = "HostCompany")
    private String companyId;
    
    @Schema(description = "Page ID", example = "dashboard")
    private String pageId;
    
    @Schema(description = "Page name", example = "Dashboard")
    private String pageName;
    
    @Schema(description = "Access level", example = "READ_WRITE")
    private String accessLevel;
    
    @Schema(description = "Page URL", example = "/dashboard")
    private String pageUrl;
}
