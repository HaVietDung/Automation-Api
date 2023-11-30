package stepdefinitions;

import constants.CommonConstant;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import smartosc.base.scrennplay.rest.ReturnPage;

public class ReturnStep extends ReturnPage{
    @When("Create Return Request With Order Number")
        public void createReturnRequest(){
        ReturnPage.getAttributeMetaData();
        ReturnPage.createReturnRequestForOMD();
        }

        @And("Change RMA Status To {string} by API")
        public void changeRMAStatus(String status){

        String returnNumber = Serenity.sessionVariableCalled(CommonConstant.RETURN_NUMBER);
        switch (status){
            case "Return Processing":
                changeRMAStatusTo("AWAITING_RETURN", "AWAITING_RETURN", returnNumber);
                break;
            case "Return Completed":
                changeRMAStatusTo("CLOSED", "INVO", returnNumber);
                break;
        }
        }

}
