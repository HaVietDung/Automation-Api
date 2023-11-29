package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import smartosc.base.scrennplay.rest.ReturnPage;

public class ReturnStep {
    @When("Create Return Request With Order Number")
        public void createReturnRequest(){
        ReturnPage.createReturnRequestForOMD();
        }
}
