@gms
Feature: Global Shipping Method

  Scenario Outline: practice
    Given Create create empty cart <country>
    When Set get <email> on cart
    And Get product in PBP
    Examples:
      | country | email                |
      | de      | anhntv1@smartosc.com |
