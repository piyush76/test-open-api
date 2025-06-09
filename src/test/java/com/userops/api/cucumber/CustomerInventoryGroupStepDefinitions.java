package com.userops.api.cucumber;

import com.userops.api.model.CustomerInventoryGroup;
import com.userops.api.service.CustomerInventoryGroupService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerInventoryGroupStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerInventoryGroupService customerInventoryGroupService;

    private ResponseEntity<CustomerInventoryGroup> response;
    private ResponseEntity<String> errorResponse;

    @Given("the Customer Inventory Group API is running and accessible")
    public void theCustomerInventoryGroupApiIsRunningAndAccessible() {
        assertNotNull(restTemplate);
    }

    @Given("I have valid authentication credentials for customer inventory group")
    public void iHaveValidAuthenticationCredentialsForCustomerInventoryGroup() {
    }

    @Given("a customer inventory group exists for company {string}, inventory group {string}, and stocking method {string}")
    public void aCustomerInventoryGroupExists(String companyId, String inventoryGroup, String stockingMethod) {
        CustomerInventoryGroup entity = customerInventoryGroupService.getCustomerInventoryGroup(companyId, inventoryGroup, stockingMethod);
        assertNotNull(entity, "Customer inventory group should exist");
    }

    @When("I send a GET request to {string} for customer inventory group")
    public void iSendAGetRequestToForCustomerInventoryGroup(String endpoint) {
        response = restTemplate.withBasicAuth("user", "password")
                .getForEntity(endpoint, CustomerInventoryGroup.class);
    }

    @When("I send a PUT request to {string} with updated data")
    public void iSendAPutRequestToWithUpdatedData(String endpoint) {
        CustomerInventoryGroup updateData = new CustomerInventoryGroup();
        updateData.setMinShelfLife(45);
        updateData.setMinShelfLifeMethod("DAYS");
        updateData.setSourceHub("HUB999");
        updateData.setDropship("Y");
        updateData.setRelaxShelfLife(10);
        updateData.setShortShelfLifeDays(3);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<CustomerInventoryGroup> entity = new HttpEntity<>(updateData, headers);

        response = restTemplate.withBasicAuth("user", "password")
                .exchange(endpoint, HttpMethod.PUT, entity, CustomerInventoryGroup.class);
    }

    @When("I send an unauthenticated GET request to {string} for customer inventory group")
    public void iSendAnUnauthenticatedGetRequestToForCustomerInventoryGroup(String endpoint) {
        errorResponse = restTemplate.getForEntity(endpoint, String.class);
    }

    @Then("the customer inventory group response status code should be {int}")
    public void theCustomerInventoryGroupResponseStatusCodeShouldBe(int expectedStatus) {
        if (response != null) {
            assertEquals(expectedStatus, response.getStatusCodeValue());
        } else if (errorResponse != null) {
            assertEquals(expectedStatus, errorResponse.getStatusCodeValue());
        }
    }

    @Then("the response should contain valid customer inventory group data")
    public void theResponseShouldContainValidCustomerInventoryGroupData() {
        assertNotNull(response.getBody());
        CustomerInventoryGroup data = response.getBody();
        assertNotNull(data.getCompanyId());
        assertNotNull(data.getInventoryGroup());
        assertNotNull(data.getStockingMethod());
    }

    @Then("the customer inventory group company ID should be {string}")
    public void theCustomerInventoryGroupCompanyIdShouldBe(String expectedCompanyId) {
        assertEquals(expectedCompanyId, response.getBody().getCompanyId());
    }

    @Then("the inventory group should be {string}")
    public void theInventoryGroupShouldBe(String expectedInventoryGroup) {
        assertEquals(expectedInventoryGroup, response.getBody().getInventoryGroup());
    }

    @Then("the stocking method should be {string}")
    public void theStockingMethodShouldBe(String expectedStockingMethod) {
        assertEquals(expectedStockingMethod, response.getBody().getStockingMethod());
    }

    @Then("the customer inventory group response should contain an error message")
    public void theCustomerInventoryGroupResponseShouldContainAnErrorMessage() {
        assertTrue(response.getStatusCodeValue() >= 400);
    }

    @Then("the response should contain the updated customer inventory group")
    public void theResponseShouldContainTheUpdatedCustomerInventoryGroup() {
        assertNotNull(response.getBody());
        CustomerInventoryGroup data = response.getBody();
        assertEquals(45, data.getMinShelfLife());
        assertEquals("HUB999", data.getSourceHub());
        assertEquals("Y", data.getDropship());
    }

    @Then("the customer inventory group response should contain an authentication error message")
    public void theCustomerInventoryGroupResponseShouldContainAnAuthenticationErrorMessage() {
        assertEquals(401, errorResponse.getStatusCodeValue());
    }
}
