package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import smartosc.base.scrennplay.rest.CheckoutPage;

public class CheckoutSteps {
    @And("Set Shipping Address")
    public void setShippingAddress(DataTable dataTable){
        CheckoutPage.setShippingAddress(dataTable);
    }
    @And("Set Billing")
    public void setBilling(DataTable dataTable){
        CheckoutPage.setBillingAddress(dataTable);
    }

    @And("Set Shipping Address And Billing")
    public void setShippngAddressAndBilling(DataTable dataTable){
        CheckoutPage.setShippingAddressAndBilling(dataTable);
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
