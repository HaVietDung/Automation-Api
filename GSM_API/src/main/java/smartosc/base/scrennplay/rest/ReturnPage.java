package smartosc.base.scrennplay.rest;
import constants.CommonConstant;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.SerenityReports;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.openqa.selenium.WebDriver;
import smartosc.base.scrennplay.actions.ActionCommon;
import smartosc.base.scrennplay.tasks.Start;
import utils.CommonUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.serenitybdd.core.Serenity.getDriver;
import static net.serenitybdd.core.Serenity.ignoredStep;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
public class ReturnPage {
    public static final String GRAPHQL = CommonConstant.FilePath.DATA_TEST + File.separator;


    public static void getAttributeMetaData(){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL + "Get CustomAttributeMetadata.graphql");
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);

        String returnTypeString = response.getBody().jsonPath().getString("data.customAttributeMetadata.items[0].attribute_options[0].value");
        String retunrType = CommonUtils.enCodeToBase64(returnTypeString);
        Serenity.setSessionVariable(CommonConstant.RETURN_TYPE).to(retunrType);

        String resolutionString = response.getBody().jsonPath().getString("data.customAttributeMetadata.items[0].attribute_options[0].value");
        String resolution = CommonUtils.enCodeToBase64(resolutionString);
        Serenity.setSessionVariable(CommonConstant.RESOLUTION).to(resolution);

        String conditionString = response.getBody().jsonPath().getString("data.customAttributeMetadata.items[4].attribute_options[0].value");
        String condition = CommonUtils.enCodeToBase64(conditionString);
        Serenity.setSessionVariable(CommonConstant.CONDITION).to(condition);

        String reasonForOMDString = response.getBody().jsonPath().getString("data.customAttributeMetadata.items[1].attribute_options[0].value");
        String reasonForOMD = CommonUtils.enCodeToBase64(reasonForOMDString);
        Serenity.setSessionVariable(CommonConstant.RETURN_FOR_OMD).to(reasonForOMD);

        String resonForOMVString = response.getBody().jsonPath().getString("data.customAttributeMetadata.items[2].attribute_options[0].value");
        String resonForOMV = CommonUtils.enCodeToBase64(resonForOMVString);
        Serenity.setSessionVariable(CommonConstant.RETURN_FOR_OMV).to(resonForOMV);



        response.prettyPrint();
    }

    public static void createReturnRequestForOMV(){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        String returnType = Serenity.sessionVariableCalled(CommonConstant.RETURN_TYPE);
        String resolution = Serenity.sessionVariableCalled(CommonConstant.RESOLUTION);
        String condition = Serenity.sessionVariableCalled(CommonConstant.CONDITION);
        String reasonForOMV = Serenity.sessionVariableCalled(CommonConstant.RETURN_FOR_OMV);
        String orderItemUid = CommonUtils.enCodeToBase64(Serenity.sessionVariableCalled(CommonConstant.ITEM_ID));

        Map<String, String>  input = new HashMap<>();
        input.put("ORDER_UID", "");
        input.put("ORDER_ITEM_ID", orderItemUid);
        input.put("REASON_OMV", reasonForOMV);
        input.put("RETURN_TYPE", returnType);
        input.put("CONDITION", condition);
        input.put("RESOLUTION", resolution);

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL + "Add new RMA RequestForOMV.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

    public static void createReturnRequestForOMD(){
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));

        String returnType = Serenity.sessionVariableCalled(CommonConstant.RETURN_TYPE);
        String resolution = Serenity.sessionVariableCalled(CommonConstant.RESOLUTION);
        String condition = Serenity.sessionVariableCalled(CommonConstant.CONDITION);
        String reasonForOMD = Serenity.sessionVariableCalled(CommonConstant.RETURN_FOR_OMD);
        String orderItemUid = CommonUtils.enCodeToBase64(Serenity.sessionVariableCalled(CommonConstant.ITEM_ID));

        Map<String, String>  input = new HashMap<>();
        input.put("ORDER_UID", "");
        input.put("ORDER_ITEM_ID", orderItemUid);
        input.put("REASON_OMD", reasonForOMD);
        input.put("RETURN_TYPE", returnType);
        input.put("CONDITION", condition);
        input.put("RESOLUTION", resolution);

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL + "Add new RMA RequestForOMD.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }
}
