# mvn verify -Dtestsuite=DemoTestSuite -Dtags="@global-shipping-method" -Denvironment=gp1

Feature: Demo Guest place order with API

#  49NANO867NA.AEUD.EEDG.DE
#  Auto7966073.OMD.ACCA.CA
#  K0E 0B2
  @global-shipping-method
  Scenario Outline: global shipping method
    Given Guest create empty cart <country>
    When Set Guest "<email>" on cart
    And Get Product
      | product_id               | postCode |
      | 49NANO867NA.AEUD.EEDG.DE |          |

    And Add To Cart
      | product_id               | delivery_date | postCode |
      | 49NANO867NA.AEUD.EEDG.DE |               |          |
    And Set Shipping Address And Billing
      | firstname | lastname | company      | street             | city       | region | postcode | country_code | telephone  |
      | John      | Doe      | Company Name | [123xx ABC STreet] | Minnetonka | MS     | T7P 0T6  | DE           | 9526452222 |
    And Get Cart with Selected Shipping Rule
    And Set Payment Method "<paymentMethod>"
    And Place Oder
    Examples:
      | country | email                | paymentMethod  |
      | de      | dunghv1@smartosc.com | cashondelivery |