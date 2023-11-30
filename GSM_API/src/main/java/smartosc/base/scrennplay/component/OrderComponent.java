package smartosc.base.scrennplay.component;

import net.serenitybdd.screenplay.targets.Target;

public class OrderComponent {
    public static final  Target SALE_CATALOG = Target.the("SaleCatalog").locatedBy("//li[@id='menu-magento-sales-sales']");
    public static final  Target ODER_SUB_CATEGORY = Target.the("Oder").locatedBy("//li[@data-ui-id='menu-magento-sales-sales-order']");

    public static final Target ORDER_NUMBER = Target.the("OrederNumber").locatedBy("//input[@id='fulltext']");
    public static final Target SEARCH_ORDER = Target.the("SearchOreder").locatedBy("(//div[@class='data-grid-search-control-wrap']//button[@class='action-submit' and @aria-label='Search'])[1]");
    public static final Target ORDER = Target.the("Order").locatedBy("//a[@class='action-menu-item' and text()='View']");
    public static final Target INVOICE = Target.the("Invoice").locatedBy("//button[@id='order_invoice']");
    public static final Target SUBMIT_INVOICE = Target.the("SubmitInvoice").locatedBy("//button[@title='Submit Invoice']");
    public static final Target ORDER_STATUS = Target.the("OrderStatus").locatedBy("//span[@id='order_status']");
    public static final Target ITEM_ID = Target.the("ItemID").locatedBy("(//td[@class='col-status'])[2]");
    public static final Target PRODUCT_TYPE = Target.the("ProductType").locatedBy("(//td[@class='col-status'])[5]");
}
