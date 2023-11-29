package stepdefinitions;

import com.ibm.icu.text.BidiTransform;
import constants.CommonConstant;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import smartosc.base.scrennplay.actions.ActionCommon;
import smartosc.base.scrennplay.component.OrderComponent;
import smartosc.base.scrennplay.pageObject.OrderPage;
import smartosc.base.scrennplay.rest.ReturnPage;
import utils.CommonUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OderStep extends OrderPage{
    @Steps
    ActionCommon actionCommon;

    @When("Go To Order And View Order")


    public void viewOrder() {
        actionCommon.clickElement(OrderComponent.SALE_CATALOG);
        actionCommon.clickElement(OrderComponent.ODER_SUB_CATEGORY);
        actionCommon.typeText(OrderComponent.ORDER_NUMBER, Serenity.sessionVariableCalled(CommonConstant.ORDER_NUMBER));

        ActionCommon.pause(3000);
        actionCommon.clickElement(OrderComponent.SEARCH_ORDER);
        ActionCommon.pause(3000);
        actionCommon.clickElement(OrderComponent.ORDER);
        actionCommon.clickElement(OrderComponent.INVOICE);
        actionCommon.clickElement(OrderComponent.SUBMIT_INVOICE);

        String itemID = actionCommon.getText(OrderComponent.ITEM_ID);
        Serenity.setSessionVariable(CommonConstant.ITEM_ID).to(itemID);
        String orderUid = CommonUtils.getOrderUid(CommonUtils.getCurrentURL());
        Serenity.setSessionVariable(CommonConstant.ORDER_UID).to(orderUid);
        System.out.println("Order UID " + orderUid);
    }

    @And("Change Order Status to {string} By API")
    public void changeOrderStatusByApi(String status){
        String orderNumber = Serenity.sessionVariableCalled(CommonConstant.ORDER_NUMBER);
        switch(status){
            case "Preparing for Delivery":
                changeOrderStatusTo("AWAITING_SHIPPING", "OPEN", orderNumber);
                break;
            case "Picking for Delivery":
                changeOrderStatusTo("AWAITING_SHIPPING", "PICK", orderNumber);
                break;
            case "On Delivery":
                changeOrderStatusTo("PRE-BILLING_ACCEPTANCE", "DELY", orderNumber);
                break;
            case "Delivery Completed":
                changeOrderStatusTo("CLOSED", "INVO", orderNumber);
                break;
        }
        ActionCommon.pause(60000);
        ThucydidesWebDriverSupport.getDriver().navigate().refresh();
    }
}
