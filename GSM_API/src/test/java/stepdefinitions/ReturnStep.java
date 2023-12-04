package stepdefinitions;

import constants.CommonConstant;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import smartosc.base.scrennplay.actions.ActionCommon;
import smartosc.base.scrennplay.rest.ReturnPage;

import javax.swing.*;

public class ReturnStep extends ReturnPage{
    @Steps
    ActionCommon actionCommon;
//    public static String orderNumber = Serenity.sessionVariableCalled(CommonConstant.ORDER_NUMBER);
    public static String returnNumber = Serenity.sessionVariableCalled(CommonConstant.RETURN_NUMBER);
    public static String productType = Serenity.sessionVariableCalled(CommonConstant.PRODUCT_TYPE);
    @When("Create Return Request With Order Number")
        public void createReturnRequest(){
        ReturnPage.getAttributeMetaData();
        if (productType.contains("omd")){
            ReturnPage.createReturnRequestForOMD();
        } else {
            ReturnPage.createReturnRequestForOMV();
        }
        }

        @And("Change RMA Status To {string} by API")
        public void changeRMAStatus(String status){

//        String returnNumber = Serenity.sessionVariableCalled(CommonConstant.RETURN_NUMBER);
            returnNumber = "RETURN_11010001101";
        switch (status){
            case "Return Processing":
                changeRMAStatusTo("AWAITING_RETURN", "AWAITING_RETURN", returnNumber);
                break;
            case "Return Completed":
                changeRMAStatusTo("CLOSED", "INVO", returnNumber);
                break;
        }
        }
        @And("Update RMA Order From Return Request To Authorized")
        public void updateReturnRequestToAuthorized(){
            String status = "Return Authorized";
            String orderNumber = "11010004605";
            actionCommon.clickElement("//li[@id='menu-magento-sales-sales']");
            actionCommon.clickElement("//li[@data-ui-id='menu-magento-rma-sales-magento-rma-rma']");
            actionCommon.clickElement("//div[@class='data-grid-filters-action-wrap']//button[contains(text(), \"Filters\")]");
            ActionCommon.pause(3000);
            actionCommon.typeText("//input[@name='order_increment_id']", orderNumber);
            actionCommon.clickElement("//div[@class='admin__footer-main-actions']//span[contains(text(), \"Apply Filters\")]");
            ActionCommon.pause(3000);
            actionCommon.clickElement("//a[@class='action-menu-item' and text()=\"View\"]");

            actionCommon.clickElement("//li[@id='rma_info_tabs_items_section']");
            String requested = actionCommon.getText("//tr//td[contains(@class,' col-qty col-qty_requested')]");
            actionCommon.typeText("//tr//td[contains(@class, 'col-qty_authorized')]//input", requested);
            actionCommon.selectDropListByText("//tr//td[contains(@class, 'col-status')]//select", status);
            actionCommon.clickElement("//button[@id='save']");
        }

}
