package com.userops.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User Operations Entity representing user assignments to operational entities")
public class UserOpsEntity {
    
    @Schema(description = "Personnel ID", example = "1")
    private Long personnelId;
    
    @Schema(description = "Company ID", example = "COMP1")
    private String companyId;
    
    @Schema(description = "Operations Entity ID", example = "OPS1")
    private String opsEntityId;
    
    @Schema(description = "Operations Company ID", example = "OPSCOMP1")
    private String opsCompanyId;
    
    @Schema(description = "Administrative role")
    private AdminRole adminRole;
}
