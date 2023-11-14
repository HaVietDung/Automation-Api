package smartosc.base.scrennplay.rest;


import com.sun.javafx.collections.MappingChange;
import constants.CommonConstant;
import constants.SessionVariable;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ca.Cal;
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

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class CheckoutPage implements Task {
    private final String typeAcc;
    private final String country;
    public static final String GRAPHQL_CART = CommonConstant.FilePath.DATA_TEST + File.separator;

    public CheckoutPage(String typeAcc, String country) {
        this.typeAcc = typeAcc;
        this.country = country;
    }

    public static void enterInfor(DataTable dataTable) {
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        List<Map<String, String>> inputInfo = dataTable.asMaps(String.class, String.class);
        Map<String, String> input = new HashMap<>();
        input.put("cart_id", Serenity.sessionVariableCalled(CommonConstant.CART_ID));
        input.put("firstname", inputInfo.get(0).get("firstname"));
        input.put("lastname", inputInfo.get(0).get("lastname"));
        input.put("company", inputInfo.get(0).get("company"));
        input.put("city", inputInfo.get(0).get("city"));
        input.put("region", inputInfo.get(0).get("region"));
        input.put("postcode", inputInfo.get(0).get("postcode"));
        input.put("country_code", inputInfo.get(0).get("country_code"));
        input.put("telephone", inputInfo.get(0).get("telephone"));
        input.put("street", inputInfo.get(0).get("street"));

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "setShipingAddress_Billing.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();

    }

    public static void setGuestEmail(String email) {
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        Map<String, String> input = new HashMap<>();
        input.put("email", email);

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "setShipingAddress_Billing.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

    public static void setShippingMethod(DataTable dataTable){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        List<Map<String, String>> inputCode = dataTable.asMaps(String.class, String.class);
        Map<String, String> input = new HashMap<>();
        input.put("carrier_code", inputCode.get(0).get("carrier_code"));
        input.put("method_code", inputCode.get(0).get("method_code"));

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "shippingMethodOnCart.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

    public static void setPaymentMethod(String code) {
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        Map<String, String> input = new HashMap<>();
        input.put("code", code);
        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "shippingMethodOnCart.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

    public static void placeOder() {
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        Map<String,String> input = new HashMap<>();
        input.put("CartID", Serenity.sessionVariableCalled(CommonConstant.CART_ID));
        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "placeOder.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }


    @Override
    public <T extends Actor> void performAs(T actor) {
        Serenity.setSessionVariable(SessionVariable.Common.COUNTRY).to(country);
        actor.whoCan(CallAnApi.at(Start.getBaseUri()));
        String graphQL;
        String cartId, guestCartId, customerCartId;

        if (typeAcc.equals("guest")) {
            graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "createEmptyCart.graphql");
        } else {
            graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "createEmptyCartCustomer.graphql");
        }

        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        if (typeAcc.equals("guest")) {
            cartId = guestCartId = response.getBody().jsonPath().getString("data.createEmptyCart");
            Serenity.setSessionVariable(CommonConstant.GUEST_CART_ID).to(guestCartId);
        } else {
            cartId = customerCartId = response.getBody().jsonPath().getString("data.customerCart.id");
            Serenity.setSessionVariable(CommonConstant.CUSTOMER_CART_ID).to(customerCartId);
        }
        Serenity.setSessionVariable(CommonConstant.CART_ID).to(cartId);
    }

}
