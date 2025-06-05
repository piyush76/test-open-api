package com.userops.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userops.api.model.AdminRole;
import com.userops.api.model.UserOpsEntity;
import com.userops.api.service.UserOpsEntityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserOpsEntityController.class)
class UserOpsEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserOpsEntityService userOpsEntityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void getUserOpsEntity_ShouldReturnEntity_WhenExists() throws Exception {
        UserOpsEntity entity = new UserOpsEntity(1L, "COMP1", "OPS1", "OPSCOMP1", AdminRole.ADMIN);
        when(userOpsEntityService.getUserOpsEntity(1L, "COMP1")).thenReturn(entity);

        mockMvc.perform(get("/iam/users/1/companies/COMP1/ops-entity"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.personnelId").value(1))
                .andExpect(jsonPath("$.companyId").value("COMP1"))
                .andExpect(jsonPath("$.opsEntityId").value("OPS1"))
                .andExpect(jsonPath("$.adminRole").value("Admin"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getUserOpsEntity_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(userOpsEntityService.getUserOpsEntity(1L, "COMP1")).thenReturn(null);

        mockMvc.perform(get("/iam/users/1/companies/COMP1/ops-entity"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateUserOpsEntityAdminRole_ShouldReturnUpdatedEntity() throws Exception {
        UserOpsEntity updatedEntity = new UserOpsEntity(1L, "COMP1", "OPS1", "OPSCOMP1", AdminRole.GRANT_ADMIN);
        when(userOpsEntityService.updateUserOpsEntityAdminRole(eq(1L), eq("COMP1"), any(AdminRole.class)))
                .thenReturn(updatedEntity);

        mockMvc.perform(put("/iam/users/1/companies/COMP1/ops-entity")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Grant Admin\""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.adminRole").value("Grant Admin"));
    }
}
