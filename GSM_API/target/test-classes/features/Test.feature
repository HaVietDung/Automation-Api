# mvn verify -Dtestsuite=DemoTestSuite -Dtags="@Test" -Denvironment=gp1

Feature: Test

  @Test
  Scenario Outline: test
    Given Auto open url <url>
    When Login Admin OBS
    Examples:
      |url|
      |https://stg2.shop.lg.com/admin|