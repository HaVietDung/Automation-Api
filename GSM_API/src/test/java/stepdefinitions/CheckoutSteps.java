package stepdefinitions;

import constants.CommonConstant;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.conditions.Check;
import smartosc.base.scrennplay.rest.CartPageGSM;
import smartosc.base.scrennplay.rest.CheckoutPage;
import smartosc.base.scrennplay.rest.PBPPage;

import java.io.File;

public class CheckoutSteps {
    @And("Set Shipping Address")
    public void setShippingAddress(DataTable dataTable){
        CheckoutPage.setShippingAddress(dataTable);
    }

    @And("Set Billing")
    public void setBilling(DataTable dataTable){
        CheckoutPage.setBillingAddress(dataTable);
    }

    @And("Get Cart with Selected Shipping Rule")
    public void getCartwithSelectedShippingRule(){
        CheckoutPage.getCartWithSelectShippingRule();
    }

    @And("Set Payment Method {string}")
        public void setPaymentMethod(String code){
        CheckoutPage.setPaymentMethod(code);
    }
    @And("Place Oder")
    public void placeOder(){
        CheckoutPage.placeOder();
    }

}
