# mvn verify -Dtestsuite=DemoTestSuite -Dtags="@guest" -Denvironment=sit_stg2

@guest
Feature: Demo Guest place order with API

  Scenario Outline: demo
    Given Guest create empty cart <country>
    When Cart - Add product to cart with sku ="<sku>"
    And Open view cart
    And Get Available_payment_methods
    And Set Shipping Address_Billing
      | firstname | lastname | company | street                         | city   | region | postcode | country_code | telephone  |
      | dung      | ha       | sosc    | [107 Sydney, Autralian Street] | London | London | E1W 1BQ  | GB           | 9526452222 |

    And Set Shipping Method On Cart
      | carrier_code | method_code |
      | freeshiping  | freeshiping |

    And  Set Guest "<email>" on cart
    And Set Payment Method On Cart by "<code>"
    And Place Oder

    Examples:
      | country | sku                   | email                | code    |
      | uk      | 34WL50S-B.AEK.EEUK.UK | autotest@yopmail.com | checkmo |