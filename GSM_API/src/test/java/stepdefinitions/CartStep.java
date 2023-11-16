//package stepdefinitions;
//
//import constants.SessionVariable;
//import io.cucumber.java.en.Given;
//import net.serenitybdd.screenplay.Actor;
//import smartosc.base.scrennplay.actions.ActionCommon;
//import smartosc.base.scrennplay.rest.CartPageGSM;
//
//
//public class CartStep {
//
//    @Given("{actor} create empty cart {word}")
//    public void createEmptyCart(Actor actor, String country) {
//        if (country.equals("default")) {
//            country = "";
//        }
//        actor.attemptsTo(CartPageGSM.createEmptyCart(country, actor.getName().toLowerCase()));
//        ActionCommon.setSessionVariable(SessionVariable.Common.COUNTRY, country);
//    }
//
//
//
//}
