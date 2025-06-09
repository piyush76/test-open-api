package com.userops.api.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userops.api.model.AdminRole;
import com.userops.api.model.UserInventoryGroup;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class UserInventoryGroupStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    private ResponseEntity<String> response;
    private String baseUrl;
    private HttpHeaders headers;

    @Given("a user inventory group exists with company ID {string}, personnel ID {long}, and inventory group {string}")
    public void aUserInventoryGroupExistsWithCompanyIDPersonnelIDAndInventoryGroup(String companyId, Long personnelId, String inventoryGroup) {
        if (baseUrl == null) {
            baseUrl = "http://localhost:" + port;
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            restTemplate = restTemplate.withBasicAuth("user", "password");
        }
        
        jdbcTemplate.execute("DELETE FROM user_inventory_group WHERE company_id = '" + companyId + "' AND personnel_id = " + personnelId + " AND inventory_group = '" + inventoryGroup + "'");
        jdbcTemplate.update(
            "INSERT INTO user_inventory_group (company_id, personnel_id, inventory_group, hub, ops_entity_id, ops_company_id, admin_role) VALUES (?, ?, ?, ?, ?, ?, ?)",
            companyId, personnelId, inventoryGroup, "HUB001", "TestOpsEntity", "TestOperatingCompany", "Admin"
        );
    }

    @When("I request the user inventory group for company ID {string}, personnel ID {long}, and inventory group {string}")
    public void iRequestTheUserInventoryGroupForCompanyIDPersonnelIDAndInventoryGroup(String companyId, Long personnelId, String inventoryGroup) {
        if (baseUrl == null) {
            baseUrl = "http://localhost:" + port;
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            restTemplate = restTemplate.withBasicAuth("user", "password");
        }
        
        String url = baseUrl + "/iam/users/" + personnelId + "/companies/" + companyId + "/inventory-groups/" + inventoryGroup;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    @When("I request the user inventory group for company ID {string}, personnel ID {long}, and inventory group {string} without authentication")
    public void iRequestTheUserInventoryGroupWithoutAuthentication(String companyId, Long personnelId, String inventoryGroup) {
        if (baseUrl == null) {
            baseUrl = "http://localhost:" + port;
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        
        TestRestTemplate unauthenticatedRestTemplate = new TestRestTemplate();
        String url = baseUrl + "/iam/users/" + personnelId + "/companies/" + companyId + "/inventory-groups/" + inventoryGroup;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = unauthenticatedRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    @When("I update the admin role to {string} for company ID {string}, personnel ID {long}, and inventory group {string}")
    public void iUpdateTheAdminRoleForCompanyIDPersonnelIDAndInventoryGroup(String adminRole, String companyId, Long personnelId, String inventoryGroup) {
        if (baseUrl == null) {
            baseUrl = "http://localhost:" + port;
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            restTemplate = restTemplate.withBasicAuth("user", "password");
        }
        
        String url = baseUrl + "/iam/users/" + personnelId + "/companies/" + companyId + "/inventory-groups/" + inventoryGroup;
        HttpEntity<String> entity = new HttpEntity<>("\"" + adminRole + "\"", headers);
        response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }

    @Then("the response should contain the user inventory group details")
    public void theResponseShouldContainTheUserInventoryGroupDetails() {
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("companyId"));
        assertTrue(response.getBody().contains("personnelId"));
        assertTrue(response.getBody().contains("inventoryGroup"));
    }

    @Then("the inventory group should be {string}")
    public void theInventoryGroupShouldBe(String expectedInventoryGroup) throws Exception {
        UserInventoryGroup entity = objectMapper.readValue(response.getBody(), UserInventoryGroup.class);
        assertEquals(expectedInventoryGroup, entity.getInventoryGroup());
    }

    @Then("the hub should be {string}")
    public void theHubShouldBe(String expectedHub) throws Exception {
        UserInventoryGroup entity = objectMapper.readValue(response.getBody(), UserInventoryGroup.class);
        assertEquals(expectedHub, entity.getHub());
    }

    @Then("the response should contain the updated user inventory group")
    public void theResponseShouldContainTheUpdatedUserInventoryGroup() {
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("companyId"));
        assertTrue(response.getBody().contains("adminRole"));
    }

    @Then("the inventory group response status should be {int}")
    public void theInventoryGroupResponseStatusShouldBe(int expectedStatus) {
        assertEquals(expectedStatus, response.getStatusCode().value());
    }

    @Then("the inventory group response should contain an error message")
    public void theInventoryGroupResponseShouldContainAnErrorMessage() {
        assertTrue(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
    }

    @Then("the inventory group response should contain a validation error message")
    public void theInventoryGroupResponseShouldContainAValidationErrorMessage() {
        assertTrue(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
        if (response.getBody() != null) {
            assertTrue(response.getBody().contains("error") || response.getBody().contains("validation") || response.getBody().contains("invalid"));
        }
    }

    @Then("the inventory group response should contain an authentication error message")
    public void theInventoryGroupResponseShouldContainAnAuthenticationErrorMessage() {
        assertEquals(401, response.getStatusCode().value());
    }

    @Then("the inventory group admin role should be {string}")
    public void theInventoryGroupAdminRoleShouldBe(String expectedAdminRole) throws Exception {
        UserInventoryGroup entity = objectMapper.readValue(response.getBody(), UserInventoryGroup.class);
        assertEquals(expectedAdminRole, entity.getAdminRole().getValue());
    }

    @Then("the inventory group company ID should be {string}")
    public void theInventoryGroupCompanyIDShouldBe(String expectedCompanyId) throws Exception {
        UserInventoryGroup entity = objectMapper.readValue(response.getBody(), UserInventoryGroup.class);
        assertEquals(expectedCompanyId, entity.getCompanyId());
    }

    @Then("the inventory group personnel ID should be {long}")
    public void theInventoryGroupPersonnelIDShouldBe(Long expectedPersonnelId) throws Exception {
        UserInventoryGroup entity = objectMapper.readValue(response.getBody(), UserInventoryGroup.class);
        assertEquals(expectedPersonnelId, entity.getPersonnelId());
    }

}
