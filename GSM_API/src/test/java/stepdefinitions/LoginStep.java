package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Steps;
import smartosc.base.scrennplay.actions.ActionCommon;
import smartosc.base.scrennplay.component.LoginCMS;
import smartosc.base.scrennplay.tasks.Start;



public class LoginStep {

    @Steps
    ActionCommon actionCommon;
    @Given("{actor} open url {word}")
    public void openUrl(Actor actor, String url) {
        actor.wasAbleTo(Start.openUrl(url));
    }
    @When("Login Admin OBS")
    public void loginOBS(){
        actionCommon.typeText(LoginCMS.USER_NAME, "longnv3");
        actionCommon.typeText(LoginCMS.PASSWORD, "admin@123");
        actionCommon.clickElement(LoginCMS.LOGIN_BTN);
    }
}

