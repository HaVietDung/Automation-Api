package utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.serenitybdd.core.Serenity;
import org.openqa.selenium.WebDriver;
import smartosc.base.scrennplay.model.EcssResult;

import java.io.*;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.serenitybdd.core.Serenity.getDriver;


public class CommonUtils {

    public static EcssResult convertEcssJson(String object) {
        final Gson gson = new Gson();
        return gson.fromJson(object, EcssResult.class);
    }

    public static String readFile(String fileName) {
        StringBuilder contents = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contents.append(line);
                contents.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contents.toString();
    }

    public static void addGraphql(String filePath, String contentBody) {
        String content = filePath + "\n======================================= Content Body =======================================\n" + contentBody;
        content += "========================================= End Body =========================================\n";
        CollectitonAPI.graphqlRequest.add(content);
        Serenity.recordReportData().withTitle("Graphql API: " + filePath).andContents(contentBody);
    }

    public static String convertGraphQLtoJson(String graphQlQuery) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("query", graphQlQuery);
        return jsonObject.toString();
    }

    public static String getBodyOfRequest(String filePath) {
        String contentGraphql = CommonUtils.readFile(filePath);
        return CommonUtils.convertGraphQLtoJson(contentGraphql);
    }

    public static String getBodyOfRequest(String filePath, Map<String, String> input) {
        String contentGraphql = CommonUtils.readFile(filePath);
        for (Map.Entry<String, String> entry : input.entrySet()) {
            System.out.println("key - value: " + entry.getKey() + " - " + entry.getValue());
            contentGraphql = contentGraphql.replaceAll("\\{\\{" + entry.getKey() + "}}", entry.getValue() == null ? "" : entry.getValue());
        }
        Serenity.recordReportData().withTitle("Graphql API: " + filePath).andContents(contentGraphql);
        return CommonUtils.convertGraphQLtoJson(contentGraphql);
    }

    public static String getRawBodyOfRequest(String filePath, Map<String, String> input) {
        String rawBody = CommonUtils.getRawBodyOfRequest(filePath);
        for (Map.Entry<String, String> entry : input.entrySet()) {
            System.out.println("key - value: " + entry.getKey() + " - " + entry.getValue());
            rawBody = rawBody.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        CommonUtils.addGraphql(filePath, rawBody);
        return rawBody;
    }

    private static String getRawBodyOfRequest(String filePath) {
        return CommonUtils.readFile(filePath);
    }

    public static String enCodeToBase64(String input) {
        byte[] encodedBytes = Base64.getEncoder().encode(input.getBytes());
        return new String(encodedBytes);
    }

    public static String getCurrentURL() {
        WebDriver driver = getDriver();
        return driver.getCurrentUrl();
    }

    public static String getOrderUid(String url) {
        String regex = "/order_id/(\\\\d+)/";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        String orderUid = matcher.group(1);
        System.out.println("Extracted Order ID: " + orderUid);
        return orderUid;
    }
}
