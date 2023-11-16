package smartosc.base.scrennplay.rest;

import constants.SessionVariable;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import smartosc.base.scrennplay.tasks.Start;
import utils.CommonUtils;
import constants.CommonConstant;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class CartPageGSM implements Task {

    private final String country;
    public static final String GRAPHQL_CART = CommonConstant.FilePath.DATA_TEST + File.separator;

    public CartPageGSM(String country) {
        this.country = country;
    }

    public static CartPageGSM createEmptyCart(String country) {
        return instrumented(CartPageGSM.class, country);
    }

    public static void setGuestEmail(String email) {
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        Map<String, String> input = new HashMap<>();
        input.put("CartID", Serenity.sessionVariableCalled(CommonConstant.CART_ID));
        input.put("email", email);

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "setGuestEmailOnCart.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Serenity.setSessionVariable(SessionVariable.Common.COUNTRY).to(country);

        actor.whoCan(CallAnApi.at(Start.getBaseUri()));

        String graphQL;
        String cartId;

        graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "createEmptyCart.graphql");
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        cartId = response.getBody().jsonPath().getString("data.createEmptyCart");
        Serenity.setSessionVariable(CommonConstant.CART_ID).to(cartId);
    }

}
