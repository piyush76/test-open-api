Feature: UserOps Entity API Acceptance Tests
  As an API consumer
  I want to interact with the UserOps Entity API
  So that I can manage user operations entities and admin roles

  Background:
    Given the UserOps API is running and accessible
    And I have valid authentication credentials

  Scenario: Retrieve existing user operations entity
    Given a user operations entity exists for personnel ID 1 and company ID "COMP1"
    When I send a GET request to "/iam/users/1/companies/COMP1/ops-entity"
    Then the response status code should be 200
    And the response should contain valid user operations entity data
    And the personnel ID should be 1
    And the company ID should be "COMP1"
    And the operations entity ID should be present
    And the admin role should be present

  Scenario: Retrieve non-existent user operations entity
    When I send a GET request to "/iam/users/999/companies/NONEXISTENT/ops-entity"
    Then the response status code should be 404
    And the response should contain an error message

  Scenario: Update admin role for existing entity
    Given a user operations entity exists for personnel ID 1 and company ID "COMP1"
    When I send a PUT request to "/iam/users/1/companies/COMP1/ops-entity" with admin role "Grant Admin"
    Then the response status code should be 200
    And the response should contain the updated user operations entity
    And the admin role should be "Grant Admin"

  Scenario: Update admin role for non-existent entity
    When I send a PUT request to "/iam/users/999/companies/NONEXISTENT/ops-entity" with admin role "Admin"
    Then the response status code should be 404
    And the response should contain an error message

  Scenario: Update with invalid admin role
    Given a user operations entity exists for personnel ID 1 and company ID "COMP1"
    When I send a PUT request to "/iam/users/1/companies/COMP1/ops-entity" with admin role "Invalid Role"
    Then the response status code should be 500
    And the response should contain a validation error message

  Scenario: Access API without authentication
    When I send an unauthenticated GET request to "/iam/users/1/companies/COMP1/ops-entity"
    Then the response status code should be 401
    And the response should contain an authentication error message
