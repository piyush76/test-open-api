package com.incora.api.iam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOpsEntity {

    @NotNull
    @JsonProperty("personnelId")
    private Long personnelId;

    @NotBlank
    @JsonProperty("companyId")
    private String companyId;

    @NotBlank
    @JsonProperty("opsEntityId")
    private String opsEntityId;

    @JsonProperty("opsCompanyId")
    private String opsCompanyId;

    @JsonProperty("adminRole")
    private AdminRole adminRole;
}
