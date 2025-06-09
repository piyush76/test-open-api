Feature: User Inventory Group API Acceptance Tests
  As an API consumer
  I want to interact with the User Inventory Group API
  So that I can manage user inventory group assignments

  Background:
    Given the User Inventory Group API is running and accessible
    And I have valid authentication credentials for user inventory group

  Scenario: Retrieve existing user inventory group
    Given a user inventory group exists for company "HostCompany", personnel ID 3, and inventory group "ELECTRONICS"
    When I send a GET request to "/iam/users/3/companies/HostCompany/inventory-groups/ELECTRONICS" for user inventory group
    Then the user inventory group response status code should be 200
    And the response should contain valid user inventory group data
    And the user inventory group company ID should be "HostCompany"
    And the user inventory group personnel ID should be 3
    And the user inventory group inventory group should be "ELECTRONICS"
    And the user inventory group hub should be "HUB001"

  Scenario: Retrieve non-existent user inventory group
    When I send a GET request to "/iam/users/999/companies/NONEXISTENT/inventory-groups/INVALID" for user inventory group
    Then the user inventory group response status code should be 404
    And the user inventory group response should contain an error message

  Scenario: Update user inventory group admin role
    Given a user inventory group exists for company "HostCompany", personnel ID 2, and inventory group "FOOD"
    When I send a PUT request to "/iam/users/2/companies/HostCompany/inventory-groups/FOOD" with inventory group admin role "Grant Admin"
    Then the user inventory group response status code should be 200
    And the response should contain the updated user inventory group
    And the user inventory group admin role should be "Grant Admin"

  Scenario: Update non-existent user inventory group
    When I send a PUT request to "/iam/users/999/companies/NONEXISTENT/inventory-groups/INVALID" with inventory group admin role "Admin"
    Then the user inventory group response status code should be 404
    And the user inventory group response should contain an error message

  Scenario: Update with invalid admin role
    Given a user inventory group exists for company "HostCompany", personnel ID 3, and inventory group "ELECTRONICS"
    When I send a PUT request to "/iam/users/3/companies/HostCompany/inventory-groups/ELECTRONICS" with inventory group admin role "Invalid Role"
    Then the user inventory group response status code should be 500
    And the user inventory group response should contain a validation error message

  Scenario: Access API without authentication
    When I send an unauthenticated GET request to "/iam/users/3/companies/HostCompany/inventory-groups/ELECTRONICS" for user inventory group
    Then the user inventory group response status code should be 401
    And the user inventory group response should contain an authentication error message
