# mvn verify -Dtestsuite=DemoTestSuite -Dtags="@demo" -Denvironment=gp1

@demo
Feature: Demo Guest place order with API

  Scenario Outline: demo
    Given Guest create empty cart <country>
    When Cart - Add product to cart with sku ="<sku>"

    Examples:
      | country | sku                   |
      | uk      | 34WL50S-B.AEK.EEUK.UK |