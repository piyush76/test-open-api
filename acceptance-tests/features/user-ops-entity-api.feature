Feature: UserOps Entity API Acceptance Tests
  As an API consumer
  I want to interact with the UserOps Entity API
  So that I can manage user operations entities and admin roles

  Background:
    Given the UserOps API is running and accessible
    And I have valid authentication credentials

  Scenario: Retrieve existing user operations entity
    Given a user operations entity exists for personnel ID 3 and company ID "HostCompany"
    When I send a GET request to "/iam/users/3/companies/HostCompany/ops-entity"
    Then the response status code should be 200
    And the response should contain a valid user operations entity array
    And the array should contain 1 entity
    And the first entity personnel ID should be 3
    And the first entity company ID should be "HostCompany"
    And the first entity operations entity ID should be "TestOpsEntity"
    And the first entity admin role should be "Grant Admin"

  Scenario: Retrieve non-existent user operations entity
    When I send a GET request to "/iam/users/999/companies/NONEXISTENT/ops-entity"
    Then the response status code should be 200
    And the response should contain an empty user operations entity array

  Scenario: Update admin role for existing entity
    Given a user operations entity exists for personnel ID 2 and company ID "HostCompany"
    When I send a PUT request to "/iam/users/2/companies/HostCompany/ops-entity" with ops entity admin role "Admin"
    Then the response status code should be 200
    And the response should contain the updated user operations entity
    And the admin role should be "Admin"

  Scenario: Update admin role for non-existent entity
    When I send a PUT request to "/iam/users/999/companies/NONEXISTENT/ops-entity" with ops entity admin role "Admin"
    Then the response status code should be 404
    And the response should contain an error message

  Scenario: Update with invalid admin role
    Given a user operations entity exists for personnel ID 3 and company ID "HostCompany"
    When I send a PUT request to "/iam/users/3/companies/HostCompany/ops-entity" with ops entity admin role "Invalid Role"
    Then the response status code should be 500
    And the response should contain a validation error message

  Scenario: Access API without authentication
    When I send an unauthenticated GET request to "/iam/users/3/companies/HostCompany/ops-entity"
    Then the response status code should be 401
    And the response should contain an authentication error message
