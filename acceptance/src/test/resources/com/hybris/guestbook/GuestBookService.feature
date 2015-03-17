Feature: Access to spring directly

  Scenario: Save and load guest directly
    Given that I have access to the guest service
    When Arran Bartish is saved
    Then Arran Bartish can be loaded from the database

  Scenario: Ensure database is cleaned
    Given Arran Bartish has been saved
    When I do a database clean
    Then the database will be empty