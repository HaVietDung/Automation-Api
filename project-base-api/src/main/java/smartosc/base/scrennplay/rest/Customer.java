package smartosc.base.scrennplay.rest;

import constants.CommonConstant;
import constants.SessionVariable;
import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.hamcrest.Matchers;
import smartosc.base.scrennplay.actions.ActionCommon;
import smartosc.base.scrennplay.tasks.Start;
import utils.CommonUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Customer implements Task {
    private final String email;
    private  String password;
    public static final String GRAPHQL_CART = CommonConstant.FilePath.DATA_TEST + File.separator;


    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Customer getToken(String email, String password) {
        return instrumented(Customer.class, email, password);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.whoCan(CallAnApi.at(Start.getBaseUri()));
        Map<String, String> input = new HashMap<>();
        input.put("email", email);
        input.put("pass", password);
        String graphQL = CommonUtils.getBodyOfRequest( GRAPHQL_CART+ "generateCustomerToken.graphql", input);
        Response responseString = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        String token = responseString.getBody().jsonPath().getString("data.generateCustomerToken.token");
        ActionCommon.setSessionVariable(SessionVariable.Customer.TOKEN, token);
    }
}
