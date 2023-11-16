package smartosc.base.scrennplay.rest;

import constants.CommonConstant;
import constants.SessionVariable;
import io.restassured.config.JsonConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.ensure.Ensure;
import smartosc.base.scrennplay.actions.ActionCommon;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class RestAssuredCommon {

    public static Response getResponseGraphql(String baseUri, String bodyContent, String... token) {
        String country = ActionCommon.getSessionVariable(SessionVariable.Common.COUNTRY);
        return getResponse(baseUri, CommonConstant.END_POINT, country, bodyContent, token);
    }

    public static Response getResponse(String baseUri, String endPoint, String store, Object bodyContent, String... token) {
        String actorName = theActorInTheSpotlight().getName();
        String accessToken = "";

        if (token.length > 0) {
            accessToken = token[0];
        } else {
            if (!actorName.equalsIgnoreCase("guest")) {
                accessToken = "Bearer " + Serenity.sessionVariableCalled(SessionVariable.Customer.TOKEN);
            }
        }

        Response response = given()
                .config(RestAssuredConfig.config().jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)))
                .baseUri(baseUri).basePath(endPoint)
                .body(bodyContent)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .header("store", store)
                .when().post();

        theActorInTheSpotlight().attemptsTo(Ensure.that(response.statusCode()).isEqualTo(200));
        return response;
    }
}
