package com.userops.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User Inventory Group representing user assignments to inventory groups")
public class UserInventoryGroup {
    
    @Schema(description = "Company ID", example = "HostCompany")
    private String companyId;
    
    @Schema(description = "Personnel ID", example = "3")
    private Long personnelId;
    
    @Schema(description = "Inventory Group", example = "ELECTRONICS")
    private String inventoryGroup;
    
    @Schema(description = "Hub", example = "HUB001")
    private String hub;
    
    @Schema(description = "Operations Entity ID", example = "TestOpsEntity")
    private String opsEntityId;
    
    @Schema(description = "Operations Company ID", example = "TestOperatingCompany")
    private String opsCompanyId;
    
    @Schema(description = "Administrative role")
    private AdminRole adminRole;
}
