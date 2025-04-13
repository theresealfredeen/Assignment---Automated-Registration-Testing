Feature: Test of user registration

  Scenario: Add new user and everything works as expected
    Given I am on basketballengland.co.uk
    When I fill in the correct member details
    Then I successfully become a member

  Scenario: Add new user but lastname is missing
    Given I am on basketballengland.co.uk
    When I fill in member details without lastname
    Then I get a missing last name error

  Scenario: Add new user but password does not match
    Given I am on basketballengland.co.uk
    When I fill in member details with mismatched passwords
    Then I get a password mismatch error

  Scenario: Add new user but Terms and conditions not accepted
    Given I am on basketballengland.co.uk
    When I fill in member details without accepting terms
    Then  I get a terms and conditions error

  Scenario Outline: Successful registration on different browsers
    Given I am on basketballengland.co.uk on "<browser>"
    When I fill in the correct member details
    Then I successfully become a member

    Examples:
      | browser |
      | chrome  |
      | firefox |