package constants;

import org.bouncycastle.pqc.jcajce.provider.sphincs.Sphincs256KeyFactorySpi;

import java.io.File;

public class CommonConstant {
    public static final int TIME_OUT = 60;
    public static final class FilePath {
        public static final String DATA_TEST = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "data";
    }
    public static final class Domain {
        public static final String DOMAIN = "Domain";
        public static final String CHECKOUT_PAGE = "/shop/checkout/#shipping";

    }
    public static final String GUEST_CART_ID = "Guest_Cart_Id";
    public static final String CUSTOMER_CART_ID = "Customer_Cart_Id";
    public static final String CART_ID = "Cart_Id";
    public static final String END_POINT = "/graphql";

    public static final String PAYMENT_SHIPPING = "Payment-Shipping";
    public static final String RULE_ID = "Rule-ID";
    public static final String PRODUCT_ID = "ProductID";

    public static final String INSTALLATION = "Installation";
    public static final String HAULAWAY = "haulaway";
    public static final String DELIVERY_DATE = "DeliveryDate";
    public static final String ORDER_NUMBER = "OrderNumber";
    public static final String ORDER_STATUS = "OrderStatus";
    public static final String ITEM_ID = "ItemID";
    public static final String RETURN_TYPE = "ReturnType";
    public static final String RETURN_FOR_OMD =  "ReasonForOmd";
    public static final String RETURN_FOR_OMV =  "reason_for_omv";
    public static final String CONDITION =  "Condition";
    public static final String RESOLUTION  = "Resolution";

    public static final String ORDER_UID = "OrderUid";
}
