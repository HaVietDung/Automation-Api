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
import java.util.*;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class PBPPage{

    private final String country;
    public static final String GRAPHQL_CART = CommonConstant.FilePath.DATA_TEST + File.separator;

    public PBPPage(String country) {
        this.country = country;
    }



    public static void getProduct(DataTable dataTable){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));

        List<Map<String, String>>  listInnput = dataTable.asMaps(String.class, String.class);
        Map<String, String> input = new HashMap<>();

        String postCode = listInnput.get(0).get("postCode");
        input.put("Product_id", listInnput.get(0).get("product_id"));
        input.put("Address_level_4", postCode);

        if (postCode == null){
            String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "[GSM] Get Product PBP before selected Delivery Coverage.graphql", input);
            Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
            response.prettyPrint();

            List<String> listRule = response.getBody().jsonPath().getList("data.getPbpProduct.global_shipping_rules.items[0].rules");
            int listRuleSize = listRule.size();

            if (listRuleSize > 0){
                String rule_id = response.getBody().jsonPath().getString("data.getPbpProduct.global_shipping_rules.items[0].rules[0].rule_id");
                Serenity.setSessionVariable(CommonConstant.RULE_ID).to(rule_id);

                String listInstallation = response.getBody().jsonPath().getString("data.getPbpProduct.global_shipping_rules.items[0].rules[0].installation_services.services[0].service_id");
                Serenity.setSessionVariable(CommonConstant.COUNT_INSTALLATION).to(listInstallation);

                String listHaulAway = response.getBody().jsonPath().getString("data.getPbpProduct.global_shipping_rules.items[0].rules[0].haulaway_services.services[0].service_id");
                Serenity.setSessionVariable(CommonConstant.COUNT_HAULAWAY).to(listHaulAway);
            } else {
                String rule_id = "";
                Serenity.setSessionVariable(CommonConstant.RULE_ID).to(rule_id);
                String countInstallation = "";
                Serenity.setSessionVariable(CommonConstant.COUNT_INSTALLATION).to(countInstallation);
                String countHaulAway = "";
                Serenity.setSessionVariable(CommonConstant.COUNT_HAULAWAY).to(countHaulAway);
            }

        } else {
            String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "[GSM]Get Product PBP With Delivery Coverage.graphql", input);
            Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
            response.prettyPrint();

            List<String> listRule = response.getBody().jsonPath().getList("data.getPbpProduct.global_shipping_rules.items[0].rules");
            int listRuleSize = listRule.size();

            if (listRuleSize > 0){
                String rule_id = response.getBody().jsonPath().getString("data.getPbpProduct.global_shipping_rules.items[0].rules[0].rule_id");
                Serenity.setSessionVariable(CommonConstant.RULE_ID).to(rule_id);

                String listInstallation = response.getBody().jsonPath().getString("data.getPbpProduct.global_shipping_rules.items[0].rules[0].installation_services.services[0].service_id");
                Serenity.setSessionVariable(CommonConstant.COUNT_INSTALLATION).to(listInstallation);

                String listHaulAway = response.getBody().jsonPath().getString("data.getPbpProduct.global_shipping_rules.items[0].rules[0].haulaway_services.services[0].service_id");
                Serenity.setSessionVariable(CommonConstant.COUNT_HAULAWAY).to(listHaulAway);

            } else {
                String rule_id = "";
                Serenity.setSessionVariable(CommonConstant.RULE_ID).to(rule_id);
                String countInstallation = "";
                Serenity.setSessionVariable(CommonConstant.COUNT_INSTALLATION).to(countInstallation);
                String countHaulAway = "";
                Serenity.setSessionVariable(CommonConstant.COUNT_HAULAWAY).to(countHaulAway);
            }
        }

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

    }

    public static void addToCart(DataTable dataTable){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));

        List<Map<String, String>> listInput = dataTable.asMaps(String.class, String.class);
        Map<String, String> input = new HashMap<>();

        String postCode = listInput.get(0).get("postCode");

        input.put("CartID", Serenity.sessionVariableCalled(CommonConstant.CART_ID));
        input.put("Address_level_4", postCode);
        input.put("Product_id", listInput.get(0).get("product_id"));
        input.put("Rule_id", Serenity.sessionVariableCalled(CommonConstant.RULE_ID));
        input.put("Relivery_date", listInput.get(0).get("delivery_date"));
        input.put("Count_Installation", Serenity.sessionVariableCalled(CommonConstant.COUNT_INSTALLATION));
        input.put("Count_haulaway", Serenity.sessionVariableCalled(CommonConstant.COUNT_HAULAWAY));


        if (postCode == null) {
            String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "[GSM] Add  To Cart PBP with selected shipping rule no post code.graphql", input);
            Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
            response.prettyPrint();
        } else {
            String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL_CART + "[GSM] Add  To Cart PBP with selected shipping rule.graphql", input);
            Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
            response.prettyPrint();
        }

    }

}
