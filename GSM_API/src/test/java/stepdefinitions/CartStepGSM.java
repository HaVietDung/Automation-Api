package stepdefinitions;

import constants.SessionVariable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import smartosc.base.scrennplay.actions.ActionCommon;
import smartosc.base.scrennplay.rest.CartPageGSM;

public class CartStepGSM {
    @Given("{actor} create empty cart {word}")
    public void createEmptyCart(Actor actor, String country) {
        if (country.equals("default")) {
            country = "";
        }
        actor.attemptsTo(CartPageGSM.createEmptyCart(country));
        ActionCommon.setSessionVariable(SessionVariable.Common.COUNTRY, country);
    }

    @When("Set Guest {string} on cart")
        public void setGuestEmail(String email){
            CartPageGSM.setGuestEmail(email);
        }
}
