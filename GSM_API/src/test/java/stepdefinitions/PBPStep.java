package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import smartosc.base.scrennplay.rest.PBPPage;

public class PBPStep  {
    @And("Get Product by {string}")
    public void getProduct(String prductID){
        PBPPage.getProduct(prductID);
    }

    @And("Get Product by {string} and {string}")
    public void getProductByIdAndLevel(String idProduct, String levelCountry){
        PBPPage.getProductByIdAndLevel(idProduct, levelCountry);
    }
    @And("Get Product Shipping Rule")
        public void getProductShippingRule(DataTable dataTable){
            PBPPage.getProductShippingRule(dataTable);
        }
    @And("Add To Cart With Rule and Level Country")
        public void addTOCartWithRuleAndLevel(DataTable dataTable){
            PBPPage.addToCartWithRuleAndLevel(dataTable);

    }

}
