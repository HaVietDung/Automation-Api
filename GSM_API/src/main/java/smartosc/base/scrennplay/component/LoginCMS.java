package smartosc.base.scrennplay.component;

import net.serenitybdd.screenplay.targets.Target;

public class LoginCMS {
    public static final Target USER_NAME = Target.the("UserName").locatedBy("//input[@id='username']");
    public static final Target PASSWORD = Target.the("Password").locatedBy("//input[@id='login']");
    public static final Target LOGIN_BTN = Target.the("LoginBtn").locatedBy("//button[@class='action-login action-primary']");

}
