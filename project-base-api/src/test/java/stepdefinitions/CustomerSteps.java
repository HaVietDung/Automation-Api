package stepdefinitions;

import constants.SessionVariable;
import io.cucumber.java.en.Given;
import net.serenitybdd.screenplay.Actor;
import smartosc.base.scrennplay.actions.ActionCommon;
import smartosc.base.scrennplay.rest.Customer;

public class CustomerSteps {

    @Given("{actor} generate customer token with email {word} and pass {word} in country {word}")
    public void generateCustomerToken(Actor actor, String email, String password, String country){
        ActionCommon.setSessionVariable(SessionVariable.Common.COUNTRY, country);
        ActionCommon.setSessionVariable(SessionVariable.Customer.EMAIL, email);
        ActionCommon.setSessionVariable(SessionVariable.Customer.GROUP_CUSTOMER, actor.getName());
        actor.attemptsTo(Customer.getToken(email, password));
    }

}
