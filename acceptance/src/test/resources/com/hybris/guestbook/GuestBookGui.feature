Feature: Guest book GUI

  Scenario: sign the guest book
    Given a guest can sign the guest book
    When Arran Bartish signs the guest book
    Then Arran Bartish will be in the guest book


  Scenario: view everyone
    Given that I have access to the guest service
    And 1st I sign Arran Bartish
    And 2nd I sign James Smith
    And 3nd I sign John Doe
    When I view the guestbook
    Then row 1 will have Arran Bartish
    Then row 2 will have James Smith
    Then row 3 will have John Doe