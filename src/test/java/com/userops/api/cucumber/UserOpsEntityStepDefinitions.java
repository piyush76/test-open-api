package com.userops.api.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.userops.api.model.AdminRole;
import com.userops.api.model.UserOpsEntity;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserOpsEntityStepDefinitions {

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

    @Given("the UserOps API is running")
    public void theUserOpsAPIIsRunning() {
        baseUrl = "http://localhost:" + port;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Given("I am authenticated as a valid user")
    public void iAmAuthenticatedAsAValidUser() {
        restTemplate = restTemplate.withBasicAuth("user", "password");
    }

    @Given("a user operations entity exists with personnel ID {long} and company ID {string}")
    public void aUserOperationsEntityExistsWithPersonnelIDAndCompanyID(Long personnelId, String companyId) {
        jdbcTemplate.execute("DELETE FROM user_ops_entity");
        jdbcTemplate.update(
            "INSERT INTO user_ops_entity (personnel_id, company_id, ops_entity_id, ops_company_id, admin_role) VALUES (?, ?, ?, ?, ?)",
            personnelId, companyId, "OPS1", "OPSCOMP1", "Admin"
        );
    }

    @Given("no user operations entity exists for personnel ID {long} and company ID {string}")
    public void noUserOperationsEntityExistsForPersonnelIDAndCompanyID(Long personnelId, String companyId) {
        jdbcTemplate.execute("DELETE FROM user_ops_entity WHERE personnel_id = " + personnelId + " AND company_id = '" + companyId + "'");
    }

    @Given("the current admin role is {string}")
    public void theCurrentAdminRoleIs(String adminRole) {
    }

    @When("I request the user operations entity for personnel ID {long} and company ID {string}")
    public void iRequestTheUserOperationsEntityForPersonnelIDAndCompanyID(Long personnelId, String companyId) {
        String url = baseUrl + "/iam/users/" + personnelId + "/companies/" + companyId + "/ops-entity";
        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    @When("I request the user operations entity for personnel ID {long} and company ID {string} without authentication")
    public void iRequestTheUserOperationsEntityWithoutAuthentication(Long personnelId, String companyId) {
        TestRestTemplate unauthenticatedRestTemplate = new TestRestTemplate();
        String url = baseUrl + "/iam/users/" + personnelId + "/companies/" + companyId + "/ops-entity";
        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = unauthenticatedRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    @When("I update the admin role to {string} for personnel ID {long} and company ID {string}")
    public void iUpdateTheAdminRoleForPersonnelIDAndCompanyID(String adminRole, Long personnelId, String companyId) {
        String url = baseUrl + "/iam/users/" + personnelId + "/companies/" + companyId + "/ops-entity";
        HttpEntity<String> entity = new HttpEntity<>("\"" + adminRole + "\"", headers);
        response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        assertEquals(expectedStatus, response.getStatusCode().value());
    }

    @Then("the response should contain a list of user operations entities")
    public void theResponseShouldContainAListOfUserOperationsEntities() {
        assertNotNull(response.getBody());
        assertTrue(response.getBody().startsWith("["));
        assertTrue(response.getBody().endsWith("]"));
        assertTrue(response.getBody().contains("personnelId"));
        assertTrue(response.getBody().contains("companyId"));
    }

    @Then("the list should contain {int} entity")
    public void theListShouldContainEntity(int expectedCount) throws Exception {
        List<UserOpsEntity> entities = objectMapper.readValue(response.getBody(), new TypeReference<List<UserOpsEntity>>(){});
        assertEquals(expectedCount, entities.size());
    }

    @Then("the first entity personnel ID should be {long}")
    public void theFirstEntityPersonnelIDShouldBe(Long expectedPersonnelId) throws Exception {
        List<UserOpsEntity> entities = objectMapper.readValue(response.getBody(), new TypeReference<List<UserOpsEntity>>(){});
        assertFalse(entities.isEmpty());
        assertEquals(expectedPersonnelId, entities.get(0).getPersonnelId());
    }

    @Then("the first entity company ID should be {string}")
    public void theFirstEntityCompanyIDShouldBe(String expectedCompanyId) throws Exception {
        List<UserOpsEntity> entities = objectMapper.readValue(response.getBody(), new TypeReference<List<UserOpsEntity>>(){});
        assertFalse(entities.isEmpty());
        assertEquals(expectedCompanyId, entities.get(0).getCompanyId());
    }

    @Then("the first entity operations entity ID should be present")
    public void theFirstEntityOperationsEntityIDShouldBePresent() throws Exception {
        List<UserOpsEntity> entities = objectMapper.readValue(response.getBody(), new TypeReference<List<UserOpsEntity>>(){});
        assertFalse(entities.isEmpty());
        assertNotNull(entities.get(0).getOpsEntityId());
    }

    @Then("the first entity admin role should be present")
    public void theFirstEntityAdminRoleShouldBePresent() throws Exception {
        List<UserOpsEntity> entities = objectMapper.readValue(response.getBody(), new TypeReference<List<UserOpsEntity>>(){});
        assertFalse(entities.isEmpty());
        assertNotNull(entities.get(0).getAdminRole());
    }

    @Then("the admin role should be {string}")
    public void theAdminRoleShouldBe(String expectedAdminRole) throws Exception {
        UserOpsEntity entity = objectMapper.readValue(response.getBody(), UserOpsEntity.class);
        assertEquals(expectedAdminRole, entity.getAdminRole().getValue());
    }

    @Then("the response should contain an empty list of user operations entities")
    public void theResponseShouldContainAnEmptyListOfUserOperationsEntities() throws Exception {
        List<UserOpsEntity> entities = objectMapper.readValue(response.getBody(), new TypeReference<List<UserOpsEntity>>(){});
        assertTrue(entities.isEmpty());
    }

    @Then("the response should contain an error message")
    public void theResponseShouldContainAnErrorMessage() {
        assertTrue(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
    }

    @Then("the response should contain the updated user operations entity")
    public void theResponseShouldContainTheUpdatedUserOperationsEntity() {
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("personnelId"));
        assertTrue(response.getBody().contains("adminRole"));
    }

    @Then("the response should contain a validation error message")
    public void theResponseShouldContainAValidationErrorMessage() {
        assertTrue(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
        if (response.getBody() != null) {
            assertTrue(response.getBody().contains("error") || response.getBody().contains("validation") || response.getBody().contains("invalid"));
        }
    }

    @Then("the response should contain an authentication error message")
    public void theResponseShouldContainAnAuthenticationErrorMessage() {
        assertEquals(401, response.getStatusCode().value());
    }
}
