Feature: User Inventory Group API
  As an API consumer
  I want to interact with the User Inventory Group API
  So that I can manage user inventory group assignments

  Background:
    Given the UserOps API is running
    And I am authenticated as a valid user

  Scenario: Retrieve existing user inventory group
    Given a user inventory group exists with company ID "HostCompany", personnel ID 3, and inventory group "ELECTRONICS"
    When I request the user inventory group for company ID "HostCompany", personnel ID 3, and inventory group "ELECTRONICS"
    Then the inventory group response status should be 200
    And the response should contain the user inventory group details
    And the inventory group company ID should be "HostCompany"
    And the inventory group personnel ID should be 3
    And the inventory group should be "ELECTRONICS"
    And the hub should be "HUB001"

  Scenario: Retrieve non-existent user inventory group
    When I request the user inventory group for company ID "NONEXISTENT", personnel ID 999, and inventory group "INVALID"
    Then the inventory group response status should be 404
    And the inventory group response should contain an error message

  Scenario: Update admin role for existing inventory group
    Given a user inventory group exists with company ID "HostCompany", personnel ID 2, and inventory group "FOOD"
    When I update the admin role to "Grant Admin" for company ID "HostCompany", personnel ID 2, and inventory group "FOOD"
    Then the inventory group response status should be 200
    And the response should contain the updated user inventory group
    And the inventory group admin role should be "Grant Admin"

  Scenario: Update admin role for non-existent inventory group
    When I update the admin role to "Admin" for company ID "NONEXISTENT", personnel ID 999, and inventory group "INVALID"
    Then the inventory group response status should be 404
    And the inventory group response should contain an error message

  Scenario: Update with invalid admin role
    Given a user inventory group exists with company ID "HostCompany", personnel ID 3, and inventory group "ELECTRONICS"
    When I update the admin role to "Invalid Role" for company ID "HostCompany", personnel ID 3, and inventory group "ELECTRONICS"
    Then the inventory group response status should be 500
    And the inventory group response should contain a validation error message

  Scenario: Access API without authentication
    When I request the user inventory group for company ID "HostCompany", personnel ID 3, and inventory group "ELECTRONICS" without authentication
    Then the inventory group response status should be 401
    And the inventory group response should contain an authentication error message
