@gsm
Feature: Demo Guest place order with API

  Scenario Outline: demo
    Given Guest create empty cart <country>
    When Set Guest "<email>" on cart
    And Get Product by "<productID>"

    And Add To Cart With Rule and Level Country
      | product_id               | address_level_4 | carrier_code    | method_code     | rule_id | delivery_date |
      | 49NANO867NA.AEUD.EEDG.DE | K0E 0B2         | global_shipping | global_shipping | 1078    | 2023-11-18    |

    And Set Shipping Address
      | firstname | lastname | company      | street             | city       | region | postcode | country_code | telephone  |
      | John      | Doe      | Company Name | [123xx ABC STreet] | Minnetonka | MS     | T7P 0T6  | US           | 9526452222 |

    And Set Billing
      | firstname | lastname | company      | street             | city       | region | postcode | country_code | telephone  |
      | John      | Doe      | Company Name | [123xx ABC STreet] | Minnetonka | MS     | T7P 0T6  | US           | 9526452222 |

    And Set Payment Method "<code>"

    And Place Oder

    Examples:
      | country | email                | productID                |code|
      | de      | anhntv1@smartosc.com | 49NANO867NA.AEUD.EEDG.DE |cashondelivery|



  @gms_levelCountry
  Scenario Outline: demo1
    Given Guest create empty cart <country>
    When Set Guest "<email>" on cart
    And Get Product by "<productID>" and "<countryLevel>"
    And Add To Cart With Rule and Level Country
      | product_id              | address_level_4 | carrier_code    | method_code     | rule_id | delivery_date |
      | Auto7966073.OMD.ACCA.CA | K0E 0B2         | global_shipping | global_shipping | 35      | 2023-11-18    |
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


