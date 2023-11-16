package smartosc.base.scrennplay.rest;

import constants.CommonConstant;
import constants.SessionVariable;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import smartosc.base.scrennplay.tasks.Start;
import utils.CommonUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class CheckoutPage {
    private final String country;
    public static final String GRAPHQL_CART = CommonConstant.FilePath.DATA_TEST + File.separator;

    public CheckoutPage(String typeAcc, String country) {
        this.country = country;
    }

    public static void setShippingAddress(DataTable dataTable){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));

        List<Map<String, String>> listInput = dataTable.asMaps(String.class, String.class);
        Map<String, String> input = new HashMap<>();
        input.put("cartId", Serenity.sessionVariableCalled(CommonConstant.CART_ID));
        input.put("firstname", listInput.get(0).get("firstname"));
        input.put("lastname", listInput.get(0).get("lastname"));
        input.put("company", listInput.get(0).get("company"));
        input.put("street", listInput.get(0).get("street"));
        input.put("city", listInput.get(0).get("city"));
        input.put("region", listInput.get(0).get("region"));
        input.put("postcode", listInput.get(0).get("postcode"));
        input.put("country_code", listInput.get(0).get("country_code"));
        input.put("telephone", listInput.get(0).get("telephone"));

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "setShippingAddress.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

    public static void setBillingAddress(DataTable dataTable){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));

        List<Map<String, String>> listInput = dataTable.asMaps(String.class, String.class);
        Map<String, String> input = new HashMap<>();
        input.put("cartId", Serenity.sessionVariableCalled(CommonConstant.CART_ID));
        input.put("firstname", listInput.get(0).get("firstname"));
        input.put("lastname", listInput.get(0).get("lastname"));
        input.put("company", listInput.get(0).get("company"));
        input.put("street", listInput.get(0).get("street"));
        input.put("city", listInput.get(0).get("city"));
        input.put("region", listInput.get(0).get("region"));
        input.put("postcode", listInput.get(0).get("postcode"));
        input.put("country_code", listInput.get(0).get("country_code"));
        input.put("telephone", listInput.get(0).get("telephone"));

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "setBillingAddress.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

    public static void getCartWithSelectShippingRule(){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));

        Map<String, String> input = new HashMap<>();
        input.put("CartID", Serenity.sessionVariableCalled(CommonConstant.CART_ID));

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "[GSM] Get Cart with Selected Shipping Rule.graphql");
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }
    public static void setPaymentMethod(String code){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));

        Map<String, String> input = new HashMap<>();
        input.put("CartID", Serenity.sessionVariableCalled(CommonConstant.CART_ID));
        input.put("code", code);

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "setPaymentMethodOnCart.graphql");
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }
    public static void placeOder(){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));

        Map<String, String> input = new HashMap<>();
        input.put("CartID", Serenity.sessionVariableCalled(CommonConstant.CART_ID));

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "placeOrder.graphql");
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

}
