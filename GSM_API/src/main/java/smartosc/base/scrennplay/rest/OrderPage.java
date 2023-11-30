package smartosc.base.scrennplay.rest;

import constants.CommonConstant;
import constants.SessionVariable;
import io.restassured.config.JsonConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import javassist.bytecode.stackmap.BasicBlock;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import smartosc.base.scrennplay.rest.RestAssuredCommon;
import smartosc.base.scrennplay.tasks.Start;
import utils.CommonUtils;
import org.junit.Assert;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.rest.SerenityRest.given;

public class OrderPage {
    public static final String JSON_CHANGE_ORDER_STATUS = CommonConstant.FilePath.DATA_TEST + File.separator;

    public static JSONObject updateOrderStatus(String orderNumber, String itemId, String lineStatusCode_1, String lineStatusCode_2){
        try{
            String omdProductPath = System.getProperty("user.dir") + "/src/test/resources/data/ECSS_Sales Order Response( change order status)OMD.json";
            FileReader reader = new FileReader(omdProductPath);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            JSONObject MessageBody = (JSONObject) jsonObject.get("MessageBody");
            JSONArray ContentList = (JSONArray) MessageBody.get("ContentList");
            JSONObject content = (JSONObject) ContentList.get(0);

            content.put("ORIG_SYS_DOCUMENT_REF", "ORDER_" + orderNumber);
            content.put("ORIG_SYS_LINE_REF", "ORDER_" + itemId);
            content.put("LINE_STATUS_CODE", lineStatusCode_1);
            content.put("LINE_STATUS_CODE2", lineStatusCode_2);
            System.out.println(jsonObject);
            return jsonObject;
        } catch (IOException | ParseException | NullPointerException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void changeOrderStatusTo(String lineStatusCode, String lineStatusCode2, String orderNumber){
        String  itemID = Serenity.sessionVariableCalled(CommonConstant.ITEM_ID);
        JSONObject jsonObject =updateOrderStatus(orderNumber, itemID, lineStatusCode, lineStatusCode2);
        assert jsonObject != null;
        Serenity.recordReportData().withTitle("Request: ").andContents(jsonObject.toString());
        Response response = RestAssuredCommon.getResponse("https://stg2.shop.lg.com" , "/ecss/webmethod/call", "de", jsonObject);
        Assert.assertEquals(200, response.getStatusCode());
        Serenity.recordReportData().withTitle(lineStatusCode).andContents(response.prettyPrint());
        String tmp = response.getBody().asString();
        System.out.println(tmp);
    }
}

