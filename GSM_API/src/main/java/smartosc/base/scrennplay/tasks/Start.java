package smartosc.base.scrennplay.tasks;

import constants.CommonConstant;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

public class Start {

    public static Performable openBrowser(String url) {
        return Task.where("Open url " + url,
                Open.url(url)
        );
    }
    private static final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    public static String getProperty(String key) {
        try {
            return EnvironmentSpecificConfiguration.from(environmentVariables).getProperty(key);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getHomePageStaging() {
        return getHomePage();
    }

    public static Performable openAddToCartPageStaging(String country, String product_url_key) {
        return Task.where("{0} open homepage stg",
                Open.url(getHomePageStaging() + country + "/" + product_url_key + ".html")
        );
    }

    public static Performable openPDPPage(String country, String path) {
        return Task.where("{0} open homepage stg",
                Open.url(getHomePage() + country + "/" + path )
        );
    }

    public static String getHomePage() {
        Serenity.setSessionVariable(CommonConstant.Domain.DOMAIN).to(getProperty("home.page"));
        return getProperty("home.page");
    }

    public static String getSSOPage() {
        return getProperty("sso.page");
    }

    public static String getAdminPage() {
        return getProperty("admin.page");
    }

    public static String getEnv() {
        return getProperty("env");
    }

    public static String getSku() {
        return getProperty("sku");
    }

    public static String getBaseUri() {
        return getProperty("api.domain");
    }

    public static String getMWUri(){
        return getProperty("mw.domain");
    }

    public static String getUserDb() {
        return getProperty("userDb");
    }

    public static String getAdminUser() {
        return getProperty("accAdmin");
    }

    public static String getAdminPass() {
        return getProperty("passAdmin");
    }

    public static String getPassDb() {
        return getProperty("passDb");
    }

    public static String getPortDb() {
        return getProperty("portDb");
    }

    public static String getPremqdb_stg() {
        return getProperty("mqdb");
    }

    public static String getHostMagentoCloud() {
        return getProperty("host");
    }

    public static String getUserMagentoCloud() {
        return getProperty("user");
    }

    public static Performable openUrl(String path) {
        return Task.where("{0} open url by href:  " + path,
                Open.url(path)
        );
    }

    public static String getMySqlHostname() {
        return getProperty("mysql_hostname");
    }

    public static String getSchema() {
        return getProperty("schema");
    }

    public static Performable openWebsiteLoginSSO() {
        return Task.where("{0} open login SSO page",
                Open.url(getSSOPage())
        );
    }

    public static String getPathOrderExport() {
        return getProperty("path.order-export");
    }

    public static String getOneViewToken() {
        return getProperty("oneview-token");
    }

    public static String getValueConfig(String field) {
        return getProperty(field);
    }
    public static Performable openWebsiteGP2(String country) {
        return Task.where("{0} open login GP2 page",
                Open.url("https://www.lg.com/" + country + "/")
        );
    }
    public static Performable openProduction(String country, String category) {
        return Task.where("{0} open Production of " + country + " and category " + category,
                Open.url(getHomePage() + country + "/" + category )
        );
    }

}
