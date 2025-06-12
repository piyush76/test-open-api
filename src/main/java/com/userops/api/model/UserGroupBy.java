package com.userops.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Users grouped by role")
public class UserGroupBy {
    
    @Schema(description = "Role name", example = "Admin")
    private String role;
    
    @Schema(description = "Users with this role")
    private List<User> users;
    
    @Schema(description = "Total count of users with this role")
    private Integer count;
}
