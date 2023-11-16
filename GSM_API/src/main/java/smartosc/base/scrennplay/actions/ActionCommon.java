package smartosc.base.scrennplay.actions;

import net.serenitybdd.core.Serenity;
import smartosc.base.scrennplay.tasks.Start;

public class ActionCommon extends Start {
    public static void setSessionVariable(Object key, Object value) {
        Serenity.setSessionVariable(key).to(value);
        System.out.println("Set session id: " + key + " - " + value);
        Serenity.recordReportData().withTitle("Set session id: " + key).andContents((String) value);
    }

    public static String getSessionVariable(Object key) {
        try {
            System.out.print("Get session id: " + key.toString());
            String value = Serenity.sessionVariableCalled(key);
            Serenity.recordReportData().withTitle("Get session id: " + key).andContents(value);
            if (value == null) {
                System.out.println(" - null (value == null)");
                return "null";
            }
            System.out.println(" - " + value);
            return value;
        } catch (NullPointerException e) {
            System.out.println(" - null (value == NullPointerException)");
            return "null";
        }
    }
}
