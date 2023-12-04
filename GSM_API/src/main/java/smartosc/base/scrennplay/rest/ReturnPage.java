package smartosc.base.scrennplay.rest;

import constants.CommonConstant;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import smartosc.base.scrennplay.tasks.Start;
import utils.CommonUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class ReturnPage {
    public static final String GRAPHQL = CommonConstant.FilePath.DATA_TEST + File.separator;
    public static String productType = Serenity.sessionVariableCalled(CommonConstant.PRODUCT_TYPE);
    public static void getAttributeMetaData() {

        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL + "Get CustomAttributeMetadata.graphql");
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();

        String returnTypeString = response.getBody().jsonPath().getString("data.customAttributeMetadata.items[0].attribute_options[0].value");
        String returnType = Base64.getEncoder().encodeToString(returnTypeString.getBytes());
        Serenity.setSessionVariable(CommonConstant.RETURN_TYPE).to(returnType);

        String resolutionString = response.getBody().jsonPath().getString("data.customAttributeMetadata.items[3].attribute_options[0].value");
        String resolution = Base64.getEncoder().encodeToString(resolutionString.getBytes());
        Serenity.setSessionVariable(CommonConstant.RESOLUTION).to(resolution);

        String conditionString = response.getBody().jsonPath().getString("data.customAttributeMetadata.items[4].attribute_options[0].value");
        String condition = Base64.getEncoder().encodeToString(conditionString.getBytes());
        Serenity.setSessionVariable(CommonConstant.CONDITION).to(condition);

        System.out.println("Product Type: " + productType);
        if (productType.contains("omd")){
            String reasonForOMDString = response.getBody().jsonPath().getString("data.customAttributeMetadata.items[1].attribute_options[0].value");
            String reasonForOMD = Base64.getEncoder().encodeToString(reasonForOMDString.getBytes());
            Serenity.setSessionVariable(CommonConstant.RETURN_FOR_OMD).to(reasonForOMD);
        } else {
            String resonForOMVString = response.getBody().jsonPath().getString("data.customAttributeMetadata.items[2].attribute_options[0].value");
            String reasonForOMV = Base64.getEncoder().encodeToString(resonForOMVString.getBytes());
            Serenity.setSessionVariable(CommonConstant.RETURN_FOR_OMV).to(reasonForOMV);
        }
    }

    public static void createReturnRequestForOMV() {
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));
        String returnType = Serenity.sessionVariableCalled(CommonConstant.RETURN_TYPE);
        String resolution = Serenity.sessionVariableCalled(CommonConstant.RESOLUTION);
        String condition = Serenity.sessionVariableCalled(CommonConstant.CONDITION);
        String reasonForOMV = Serenity.sessionVariableCalled(CommonConstant.RETURN_FOR_OMV);
        String orderItemUid = CommonUtils.enCodeToBase64(Serenity.sessionVariableCalled(CommonConstant.ITEM_ID));
        String orderUid = Serenity.sessionVariableCalled(CommonConstant.ORDER_UID);

        Map<String, String> input = new HashMap<>();
        input.put("ORDER_UID", orderUid);
        input.put("ORDER_ITEM_ID", orderItemUid);
        input.put("REASON_OMV", reasonForOMV);
        input.put("RETURN_TYPE", returnType);
        input.put("CONDITION", condition);
        input.put("RESOLUTION", resolution);

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL + "Add new RMA RequestForOMV.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();
    }

    public static void createReturnRequestForOMD() {
        theActorInTheSpotlight().whoCan(CallAnApi.at(Start.getBaseUri()));

        String returnType = Serenity.sessionVariableCalled(CommonConstant.RETURN_TYPE);
        String resolution = Serenity.sessionVariableCalled(CommonConstant.RESOLUTION);
        String condition = Serenity.sessionVariableCalled(CommonConstant.CONDITION);
        String reasonForOMD = Serenity.sessionVariableCalled(CommonConstant.RETURN_FOR_OMD);
        String orderItemUid = CommonUtils.enCodeToBase64(Serenity.sessionVariableCalled(CommonConstant.ITEM_ID));
        String orderUid = Serenity.sessionVariableCalled(CommonConstant.ORDER_UID);

        Map<String, String> input = new HashMap<>();
        input.put("ORDER_UID", orderUid);
        input.put("ORDER_ITEM_ID", orderItemUid);
        input.put("REASON_OMD", reasonForOMD);
        input.put("RETURN_TYPE", returnType);
        input.put("CONDITION", condition);
        input.put("RESOLUTION", resolution);

        String graphQL = CommonUtils.getBodyOfRequest(GRAPHQL + "Add new RMA RequestForOMD.graphql", input);
        Response response = RestAssuredCommon.getResponseGraphql(Start.getBaseUri(), graphQL);
        response.prettyPrint();

        String returnNumber = response.getBody().jsonPath().getString("data.requestReturn.return.number");
        Serenity.setSessionVariable(CommonConstant.RETURN_NUMBER).to(returnNumber);
    }
    public static JSONObject updateRMAStatus(String returnNumber, String itemId, String lineStatusCode, String lineStatusCode2){
        try {
            String omdProductPath = System.getProperty("user.dir") + "/src/test/resources/data/Update RMA status OMD.json";
            FileReader reader = new FileReader(omdProductPath);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            JSONObject MessageBody = (JSONObject) jsonObject.get("MessageBody");
            JSONArray ContentList = (JSONArray) MessageBody.get("ContentList");
            JSONObject Content = (JSONObject) ContentList.get(0);

            Content.put("ORIG_SYS_DOCUMENT_REF", returnNumber);
            Content.put("ORIG_SYS_LINE_REF", itemId);
            Content.put("LINE_STATUS_CODE", lineStatusCode);
            Content.put("LINE_STATUS_CODE_2", lineStatusCode2);

            return jsonObject;

        } catch (IOException | ParseException | NullPointerException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public void changeRMAStatusTo(String lineStatusCode, String lineStatusCode2, String returnNumber){
        String  itemID = Serenity.sessionVariableCalled(CommonConstant.ITEM_ID);
//        String itemID = "RETURN_17121898";
        JSONObject jsonObject =updateRMAStatus(returnNumber, itemID, lineStatusCode, lineStatusCode2);
        assert jsonObject != null;
        Serenity.recordReportData().withTitle("Request: ").andContents(jsonObject.toString());
        Response response = RestAssuredCommon.getResponse("https://stg2.shop.lg.com" , "/ecss/webmethod/call", "de", jsonObject);
        Assert.assertEquals(200, response.getStatusCode());
        Serenity.recordReportData().withTitle(lineStatusCode).andContents(response.prettyPrint());
        String tmp = response.getBody().asString();
        System.out.println(tmp);
    }
}
