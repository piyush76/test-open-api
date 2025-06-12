Feature: User Operations Entity Management
  As an API consumer
  I want to manage user operations entities
  So that I can retrieve and update user administrative roles

  Background:
    Given the UserOps API is running
    And I am authenticated as a valid user

  Scenario: Successfully retrieve an existing user operations entity
    Given a user operations entity exists with personnel ID 1 and company ID "COMP1"
    When I request the user operations entity for personnel ID 1 and company ID "COMP1"
    Then the response status should be 200
    And the response should contain a list of user operations entities
    And the list should contain 1 entity
    And the first entity personnel ID should be 1
    And the first entity company ID should be "COMP1"
    And the first entity operations entity ID should be present
    And the first entity admin role should be present

  Scenario: Attempt to retrieve a non-existent user operations entity
    Given no user operations entity exists for personnel ID 999 and company ID "NONEXISTENT"
    When I request the user operations entity for personnel ID 999 and company ID "NONEXISTENT"
    Then the response status should be 200
    And the response should contain an empty list of user operations entities

  Scenario: Successfully update admin role for an existing user operations entity
    Given a user operations entity exists with personnel ID 1 and company ID "COMP1"
    And the current admin role is "Admin"
    When I update the admin role to "Grant Admin" for personnel ID 1 and company ID "COMP1"
    Then the response status should be 200
    And the response should contain the updated user operations entity
    And the admin role should be "Grant Admin"

  Scenario: Attempt to update admin role for a non-existent user operations entity
    Given no user operations entity exists for personnel ID 999 and company ID "NONEXISTENT"
    When I update the admin role to "Admin" for personnel ID 999 and company ID "NONEXISTENT"
    Then the response status should be 404
    And the response should contain an error message

  Scenario: Update admin role with invalid role value
    Given a user operations entity exists with personnel ID 1 and company ID "COMP1"
    When I update the admin role to "Invalid Role" for personnel ID 1 and company ID "COMP1"
    Then the response status should be 500
    And the response should contain a validation error message

  Scenario: Access API without authentication
    When I request the user operations entity for personnel ID 1 and company ID "COMP1" without authentication
    Then the response status should be 401
    And the response should contain an authentication error message
