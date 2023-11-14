package smartosc.base.scrennplay.rest;

import constants.SessionVariable;
import io.cucumber.datatable.DataTable;
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
import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class CartPage implements Task  {
    private final String typeAcc;
    private final String country;
    public static final String GRAPHQL_CART = CommonConstant.FilePath.DATA_TEST + File.separator;
    public CartPage(String country, String typeAcc) {
        this.country = country;
        this.typeAcc = typeAcc;
    }

    public static CartPage createEmptyCart(String country, String typeAcc) {
        return instrumented(CartPage.class, country, typeAcc);
    }

    public static void addProductToCart(String sku){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        Map<String, String> input = new HashMap<>();
        input.put("CartID", Serenity.sessionVariableCalled(CommonConstant.CART_ID));
        input.put("sku", sku);
        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "atcProductPbp.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

    public static void viewCart (){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        Map<String,String> input = new HashMap<>();
        input.put("CartID", Serenity.sessionVariableCalled(CommonConstant.CART_ID));
        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "viewCart.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

    public static void paymentMEthod() {
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        Map<String, String> input = new HashMap<>();
        input.put("CartID", Serenity.sessionVariableCalled(CommonConstant.CART_ID));
        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "viewCart.graphql", input);
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
