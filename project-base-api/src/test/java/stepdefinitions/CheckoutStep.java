package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import smartosc.base.scrennplay.rest.CheckoutPage;

public class CheckoutStep {
    @And("Set Shipping Method On Cart")
    public void setShippingMethod(DataTable dataTable) {
        CheckoutPage.setShippingMethod(dataTable);
    }

    @And("Set Shipping Address_Billing")
    public void enterInfor(DataTable dataTable) {
        CheckoutPage.enterInfor(dataTable);
    }

    @And("Set Payment Method On Cart by {string}")
    public void PaymentMethod(String code) {
        CheckoutPage.setPaymentMethod(code);
    }

    @And("Set Guest {string} on cart")
    public void setGuestEmail(String email) {
        CheckoutPage.setGuestEmail(email);
    }
    @And("Place Oder")
    public void placeOder() {
        CheckoutPage.placeOder();
    }
}
