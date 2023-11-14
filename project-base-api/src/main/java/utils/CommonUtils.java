package utils;

import com.google.gson.JsonObject;
import net.serenitybdd.core.Serenity;

import java.io.*;
import java.util.Map;


public class CommonUtils {
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

}
