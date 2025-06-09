Feature: Customer Inventory Group API
  As an API consumer
  I want to interact with the Customer Inventory Group API
  So that I can manage inventory group configurations

  Background:
    Given the Customer Inventory Group API is running and accessible
    And I have valid authentication credentials for customer inventory group

  Scenario: Retrieve existing customer inventory group
    Given a customer inventory group exists for company "HostCompany", inventory group "ELECTRONICS", and stocking method "FIFO"
    When I send a GET request to "/iam/inventory-groups/HostCompany/ELECTRONICS/FIFO" for customer inventory group
    Then the customer inventory group response status code should be 200
    And the response should contain valid customer inventory group data
    And the customer inventory group company ID should be "HostCompany"
    And the inventory group should be "ELECTRONICS"
    And the stocking method should be "FIFO"

  Scenario: Retrieve non-existent customer inventory group
    When I send a GET request to "/iam/inventory-groups/NONEXISTENT/INVALID/NONE" for customer inventory group
    Then the customer inventory group response status code should be 404
    And the customer inventory group response should contain an error message

  Scenario: Update customer inventory group
    Given a customer inventory group exists for company "HostCompany", inventory group "ELECTRONICS", and stocking method "FIFO"
    When I send a PUT request to "/iam/inventory-groups/HostCompany/ELECTRONICS/FIFO" with updated data
    Then the customer inventory group response status code should be 200
    And the response should contain the updated customer inventory group

  Scenario: Update non-existent customer inventory group
    When I send a PUT request to "/iam/inventory-groups/NONEXISTENT/INVALID/NONE" with updated data
    Then the customer inventory group response status code should be 404
    And the customer inventory group response should contain an error message

  Scenario: Access API without authentication
    When I send an unauthenticated GET request to "/iam/inventory-groups/HostCompany/ELECTRONICS/FIFO" for customer inventory group
    Then the customer inventory group response status code should be 401
    And the customer inventory group response should contain an authentication error message
