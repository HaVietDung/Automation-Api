package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import smartosc.base.scrennplay.rest.PBPPage;

public class PBPStep  {
    @And("Get Product")
    public void getProduct(DataTable dataTable){
        PBPPage.getProduct(dataTable);
    }

    @And("Get Product Shipping Rule")
        public void getProductShippingRule(DataTable dataTable){
            PBPPage.getProductShippingRule(dataTable);
        }
    @And("Add To Cart")
        public void addTOCartWithRuleAndLevel(DataTable dataTable){
            PBPPage.addToCart(dataTable);
    }

}
