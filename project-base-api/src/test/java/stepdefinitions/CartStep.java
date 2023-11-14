package stepdefinitions;

import constants.SessionVariable;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import smartosc.base.scrennplay.actions.ActionCommon;
import smartosc.base.scrennplay.rest.CartPage;


public class CartStep {

    @Given("{actor} create empty cart {word}")
    public void createEmptyCart(Actor actor, String country) {
        if (country.equals("default")) {
            country = "";
        }
        actor.attemptsTo(CartPage.createEmptyCart(country, actor.getName().toLowerCase()));
        ActionCommon.setSessionVariable(SessionVariable.Common.COUNTRY, country);
    }

    @When("Cart - Add product to cart with sku ={string}")
    public void addProductToCart(String sku){
        CartPage.addProductToCart(sku);
    }

    @And("Open view cart")
    public void viewCart(){
        CartPage.viewCart();
    }

    @And("Get Available_payment_methods")
    public void paymentMethod() {
        CartPage.paymentMEthod();
    }

}
