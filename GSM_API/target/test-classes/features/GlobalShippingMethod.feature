# mvn verify -Dtestsuite=DemoTestSuite -Dtags="@gsm" -Denvironment=gp1
# mvn verify -Dtestsuite=DemoTestSuite -Dtags="@gsm_levelCountry" -Denvironment=gp1
# mvn verify -Dtestsuite=DemoTestSuite -Dtags="@global-shipping-method" -Denvironment=gp1

Feature: Demo Guest place order with API

  @gsm
  Scenario Outline: demo
    Given Guest create empty cart <country>
    When Set Guest "<email>" on cart
    And Get Product by "<productID>"

    And Add To Cart With Rule and Level Country
      | product_id               | address_level_4 | carrier_code    | method_code     | rule_id | delivery_date |
      | 49NANO867NA.AEUD.EEDG.DE |                 | global_shipping | global_shipping | 1078    |               |

    And Set Shipping Address
      | firstname | lastname | company      | street             | city       | region | postcode | country_code | telephone  |
      | John      | Doe      | Company Name | [123xx ABC STreet] | Minnetonka | MS     | T7P 0T6  | US           | 9526452222 |

    And Set Billing
      | firstname | lastname | company      | street             | city       | region | postcode | country_code | telephone  |
      | John      | Doe      | Company Name | [123xx ABC STreet] | Minnetonka | MS     | T7P 0T6  | US           | 9526452222 |

    And Set Payment Method "<code>"

    And Place Oder

    Examples:
      | country | email                | productID                | code           |
      | de      | anhntv1@smartosc.com | 49NANO867NA.AEUD.EEDG.DE | cashondelivery |


  @gsm_levelCountry
  Scenario Outline: demo1
    Given Guest create empty cart <country>
    When Set Guest "<email>" on cart
    And Get Product
      | product_id              | postCode |
      | Auto7966073.OMD.ACCA.CA | K0E 0B2  |
    And Add To Cart With Rule and Level Country
      | product_id              | address_level_4 | carrier_code    | method_code     | rule_id | delivery_date |
      | 49NANO867NA.AEUD.EEDG.DE | K0E 0B2         | global_shipping | global_shipping | 35      | 2023-11-18    |
    And Set Shipping Address
      | firstname | lastname | company      | street             | city       | region | postcode | country_code | telephone  |
      | John      | Doe      | Company Name | [123xx ABC STreet] | Minnetonka | MS     | T7P 0T6  | US           | 9526452222 |

    And Set Billing
      | firstname | lastname | company      | street             | city       | region | postcode | country_code | telephone  |
      | John      | Doe      | Company Name | [123xx ABC STreet] | Minnetonka | MS     | T7P 0T6  | US           | 9526452222 |

    And Get Cart with Selected Shipping Rule

    And Set Payment Method "<code>"

    And Place Oder
    Examples:
      | country | email                | productID               | countryLevel | code           |
      | ca_en   | anhntv1@smartosc.com | Auto7966073.OMD.ACCA.CA | K0E 0B2      | cashondelivery |


  @global-shipping-method
  Scenario Outline: global shipping method
    Given Guest create empty cart <country>
    When Set Guest "<email>" on cart
    And Get Product
      | product_id               | postCode |
      | Auto7966073.OMD.ACCA.CA |     K0E 0B2     |

    And Add To Cart
      | product_id               | delivery_date | postCode |
      | Auto7966073.OMD.ACCA.CA |   2023-11-22            |        K0E 0B2        |
    And Set Shipping Address And Billing
      | firstname | lastname | company      | street             | city       | region | postcode | country_code | telephone  |
      | John      | Doe      | Company Name | [123xx ABC STreet] | Minnetonka | MS     | T7P 0T6  | DE           | 9526452222 |
    And Get Cart with Selected Shipping Rule
    And Set Payment Method "<paymentMethod>"
    And Place Oder
    Examples:
      | country | email                | paymentMethod  |
      | ca_en      | dunghv1@smartosc.com | cashondelivery |