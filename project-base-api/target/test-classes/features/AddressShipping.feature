@address
Feature: Address Shipping

  Scenario Outline: Address Shipping
    When setShipping
      | firstName | lastName | company | street   | city  | region | postcode | telephone  | country |
      | nam       | nam      | osc     | phamhung | hanoi | hanoi  | 123      | 0835678910 | vietnam |
    Examples:
      |  |