# mvn verify -Dtestsuite=DemoTestSuite -Dtags=@customer -Denvironment=sit_stg2

@customer
Feature: Demo Customer place order with API

  Scenario Outline: demo
    Given Auto generate customer token with email <email> and pass <password> in country <country>
    When  Auto create empty cart <country>
    And Cart - Add product to cart with sku ="<sku>"
    And Open view cart
    And Get Available_payment_methods
    And Set Shipping Address_Billing
      | firstname | lastname | company | street                         | city   | region | postcode | country_code | telephone  |
      | dung      | ha       | sosc    | [107 Sydney, Autralian Street] | London | London | E1W 1BQ  | GB           | 9526452222 |

    And Set Shipping Method On Cart
      | carrier_code | method_code |
      | freeshiping  | freeshiping |
    And Set Payment Method On Cart by "<code>"
    And Place Oder

    Examples:
      | country | email                | password  | sku                        | code    |
      | uk      | autotest@yopmail.com | admin123@ | GBB61DSJEN.ADSQLGU.EEUK.UK | checkmo |