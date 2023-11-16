package smartosc.base.scrennplay.rest;

import constants.CommonConstant;
import constants.SessionVariable;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.json.simple.JSONArray;
import smartosc.base.scrennplay.tasks.Start;
import utils.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class PBPPage {

    private final String country;
    public static final String GRAPHQL_CART = CommonConstant.FilePath.DATA_TEST + File.separator;

    public PBPPage(String country) {
        this.country = country;
    }

    public static void getProduct(String productId) {
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        Map<String, String> input = new HashMap<>();
        input.put("product_id", productId);

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "[GSM] Get Product PBP before selected Delivery Coverage.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

    public static void getProductByIdAndLevel(String productId, String levelCountry){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        Map<String, String> input = new HashMap<>();
        input.put("product_id", productId);
        input.put("address_level_4", levelCountry);

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "[GSM]Get Product PBP With Delivery Coverage.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

    public static void getProductShippingRule(DataTable dataTable){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        List<Map<String, String>> listInput = dataTable.asMaps(String.class, String.class);
        Map<String, String> input = new HashMap<>();
        input.put("product_id", listInput.get(0).get("product_id"));
        input.put("carrier_code", listInput.get(0).get("carrier_code"));
        input.put("method_code", listInput.get(0).get("method_code"));
        input.put("rule_id", listInput.get(0).get("rule_id"));
        input.put("delivery_date", listInput.get(0).get("delivery_date"));

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "[GSM]Get Product PBP With Selected Shipping Rule.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();

        List<String> payment = Serenity.sessionVariableCalled(CommonConstant.PAYMENT_SHIPPING);
        System.out.println("list: "+payment);
    }

    public static void addToCartWithRuleAndLevel(DataTable dataTable){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        List<Map<String, String>> listInput = dataTable.asMaps(String.class, String.class);
        Map<String, String> input = new HashMap<>();
        input.put("CartID", Serenity.sessionVariableCalled(CommonConstant.CART_ID));
        input.put("address_level_4", listInput.get(0).get("address_level_4"));
        input.put("product_id", listInput.get(0).get("product_id"));
        input.put("carrier_code", listInput.get(0).get("carrier_code"));
        input.put("method_code", listInput.get(0).get("method_code"));
        input.put("rule_id", listInput.get(0).get("rule_id"));
        input.put("delivery_date", listInput.get(0).get("delivery_date"));

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "[GSM] Add  To Cart PBP with selected shipping rule.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

}
