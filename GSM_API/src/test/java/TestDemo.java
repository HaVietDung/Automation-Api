import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDemo {
    public static void main(String[] args) {
        String inputString = "https://stg2.shop.lg.com/admin/sales/order/view/order_id/13081075/key/cc85fdb87252b395534382c0cf2be185d25a56a0605a6522daec485bcdc17f5c/";
        String regex = "/order_id/(\\d+)/";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.find()) {
            String orderId = matcher.group(1);
            System.out.println("Extracted Order ID: " + orderId);
        } else {
            System.out.println("Order ID not found in the input string.");
        }
    }
}
