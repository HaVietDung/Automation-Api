package utils;

import smartosc.base.scrennplay.actions.ActionCommon;

import java.util.List;

public class CollectitonAPI {
    public static List<String> graphqlRequest;

    public static void createCollection(String pathCollection) {
        int maxStep = graphqlRequest.size();
        StringBuilder stringCollection = new StringBuilder();

        for (int step = 0; step < maxStep; step++) {
            String graphqlText = graphqlRequest.get(step);
            stringCollection.append(step + 1);
            stringCollection.append(": ");
            stringCollection.append(graphqlText);
            stringCollection.append("\n");
        }
        ActionCommon.writeText(pathCollection, stringCollection.toString(), false);
    }
}
