package smartosc.base.scrennplay.actions;


//import config.ConfigResource;
import constants.CommonConstant;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.conditions.Check;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.questions.Absence;
import net.serenitybdd.screenplay.questions.Presence;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import smartosc.base.scrennplay.tasks.Start;
//import smartosc.lgeobs.screenplay.tasks.Start;
//import smartosc.lgeobs.screenplay.ui.frontend.CheckoutPageComponent;
//import smartosc.lgeobs.screenplay.ui.magentoadmin.MagentoAdminComponent;
//import utils.enums.Condition;

import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;

public class ActionCommon extends Start {

    int timeWait = 60;

    public void waitElement(String xpath) {
        Target targetLocators = Target.the(xpath).locatedBy(xpath);
        waitElement(targetLocators);
    }

    public void waitElementClickable(String xpath) {
        Target targetLocators = Target.the(xpath).locatedBy(xpath);
        waitElementClickableByTarget(targetLocators);
    }

    public void waitElementClickableByTarget(Target targetLocators) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isClickable()).forNoMoreThan(CommonConstant.TIME_OUT).seconds());
    }

    public void clickElement(String xpath) {
        Target targetLocators = Target.the(xpath).locatedBy(xpath);
        clickElement(targetLocators);
    }

    public void typeText(String xpath, String value) {
        Target targetLocators = Target.the(xpath).locatedBy(xpath);
        typeText(targetLocators, value);
    }

    public void typeTextAndEnter(String xpath, String value) {
        Target targetLocators = Target.the(xpath).locatedBy(xpath);
        typeTextAndEnter(targetLocators, value);
    }

    public void typeTextAndEnter(Target target, String value) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(target, isVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds(),
                Enter.theValue(value).into(target).thenHit(Keys.ENTER));
    }

    public Target getTarget(String name, String xpath) {
        return Target.the(name).locatedBy(xpath);
    }

    public void waitPresentElement(String xpath) {
        Target targetLocators = Target.the(xpath).locatedBy(xpath);
        waitPresentElement(targetLocators);
    }

    public void waitPresentElement(Target targetLocators) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isPresent()).forNoMoreThan(CommonConstant.TIME_OUT).seconds());
    }

    public void clickElement(Target targetLocators) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds(),
                Click.on(targetLocators));
    }

    public void clickElement(WebElement element) {
        element.click();
    }

    /**
     * Click element sau đó check element tiếp theo đã hiển thị chưa, nếu chưa thì lặp lại click và check cho đến khi Timeout.
     *
     * @param locatorClick locator là xpath or Target của element muốn click.
     * @param locatorCheck locator là xpath or Target của element muốn check hiển thị sau khi click.
     */
    public void clickAndCheckElementDisplayed(Object locatorClick, Object locatorCheck) {
        Target targetClick;
        Target targetCheck;
        if (locatorClick instanceof String) {
            targetClick = Target.the("Locator click").locatedBy(locatorClick.toString());
        } else {
            targetClick = (Target) locatorClick;
        }
        if (locatorCheck instanceof String) {
            targetCheck = Target.the("Locator check").locatedBy(locatorCheck.toString());
        } else {
            targetCheck = (Target) locatorCheck;
        }

        int timeout = CommonConstant.TIME_OUT;

        for (int time = 1; time <= timeout; time++) {
            clickElement(targetClick);

            if (checkExistElement(targetCheck)) {
                checkElementDisplayed(targetCheck);
                break;
            }
        }
    }

    public void selectDropListByText(Target targetLocators, String value) {
        selectDroplistByText(targetLocators, value);
    }

    public void scrollElement(String xpath) {
        Target targetLocators = Target.the(xpath).locatedBy(xpath);
        theActorInTheSpotlight().attemptsTo(
                Scroll.to(targetLocators));
    }

    public void scrollElement(Target targetLocators) {
        theActorInTheSpotlight().attemptsTo(WaitUntil.the(targetLocators, isPresent()).forNoMoreThan(timeWait).seconds(), Scroll.to(targetLocators));
        pause(1000);
    }

    public void scrollElement(WebElement element) {
        pause(1000);
        theActorInTheSpotlight().attemptsTo(Scroll.to(element));
        theActorInTheSpotlight().attemptsTo(Ensure.that(element.isDisplayed()).isTrue());
    }

    public void selectDroplistByText(Target targetLocators, String value) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds(),
                SelectFromOptions.byVisibleText(value).from(targetLocators));
    }

    public void selectDroplistByIndex(Target targetLocators, int index) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isVisible()).forNoMoreThan(timeWait).seconds(),
                SelectFromOptions.byIndex(index).from(targetLocators));
    }

    public WebElement existElement(Target targetLocators) {
        try {
            pause(1000);
            return getDriver().findElement(By.xpath(targetLocators.getCssOrXPathSelector()));
        } catch (Exception e) {
            return null;
        }
    }

    public static void pause(int time) {
        try {
            if (time > 1000) {
                for (int i = 0; i < time / 1000; i++) {
                    System.out.println("Sleep " + (i + 1) + "s");
                    Thread.sleep(1000);
                }
            } else {
                Thread.sleep(time);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void pause(int time, String message) {
        try {
            if (time > 1000) {
                for (int i = 0; i < time / 1000; i++) {
                    System.out.println(message + " " + (i + 1) + "/" + time / 1000);
                    Thread.sleep(1000);
                }
            } else {
                Thread.sleep(time);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitElement(Target targetLocators) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds());
    }

    public List<WebElement> getListElements(Target targetLocators) {
        return ThucydidesWebDriverSupport.getDriver().findElements(By.xpath(targetLocators.getCssOrXPathSelector()));
    }

    public List<WebElement> getListElements(String xpath) {
        Target targetLocators = Target.the(xpath).locatedBy(xpath);
        return getListElements(targetLocators);
    }

    public void typeText(Target targetLocators, String value) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds(),
                Clear.field(targetLocators),
                Enter.theValue(value).into(targetLocators));
    }

    public void selectMulti(String[] listItems, Target targetLocators) {
        WebElement element = Serenity.getDriver().findElement(By.xpath(targetLocators.getCssOrXPathSelector()));
        Select dropdown = new Select(element);
        List<WebElement> options = dropdown.getOptions();
        Actions builder = new Actions(Serenity.getDriver());
        boolean isMultiple = dropdown.isMultiple();
        if (isMultiple) {
            dropdown.deselectAll();
        }
        builder.keyDown(Keys.CONTROL);
        for (String textOption : listItems) {
            for (WebElement option : options) {
                final String optionText = option.getText().trim();
                if (optionText.equalsIgnoreCase(textOption)) {
                    if (isMultiple) {
                        if (!option.isSelected()) {
                            builder.click(option);
                        }
                    } else {
                        option.click();
                    }
                    break;
                }
            }
        }
        builder.keyUp(Keys.CONTROL).build().perform();
    }

    public void retryInputText(Target targetLocators, String value) {
        String text = getTextByTargetLocators(targetLocators);
        if (!text.equals(value)) {
            typeText(targetLocators, value);
        }
    }

    public String getDecoder(String passwordTemp) {
        byte[] decodedBytes = Base64.getDecoder().decode(passwordTemp);
        return new String(decodedBytes);
    }

    public static String getEncode(String passwordTemp) {
        byte[] encodeBytes = Base64.getEncoder().encode(passwordTemp.getBytes());
        return new String(encodeBytes);
    }

    public static void setSessionVariable(Object key, Object value) {
        Serenity.setSessionVariable(key).to(value);
        System.out.println("Set session id: " + key + " - " + value);
        Serenity.recordReportData().withTitle("Set session id: " + key).andContents((String) value);
    }

    public static String getSessionVariable(Object key) {
        try {
            System.out.print("Get session id: " + key.toString());
            String value = Serenity.sessionVariableCalled(key);
            Serenity.recordReportData().withTitle("Get session id: " + key).andContents(value);
            if (value == null) {
                System.out.println(" - null (value == null)");
                return "null";
            }
            System.out.println(" - " + value);
            return value;
        } catch (NullPointerException e) {
            System.out.println(" - null (value == NullPointerException)");
            return "null";
        }
    }

    public String getTextByTargetLocators(Target targetLocators) {
        waitElementTime(targetLocators);
        if (null == existElement(targetLocators) || !getElement(targetLocators).isDisplayed()) {
            return "";
        }
        scrollElementIntoMiddle(targetLocators);
        theActorInTheSpotlight().attemptsTo(Ensure.that(targetLocators).isDisplayed());
        String text = Text.of(targetLocators).answeredBy(theActorInTheSpotlight());
        Serenity.recordReportData().withTitle("Text of " + targetLocators.getName()).andContents(text);
        return text;
    }


    public String getTextByElementLocators(Object targetOrXpathLocators) {
        Target target;
        if (targetOrXpathLocators instanceof String) {
            target = Target.the(targetOrXpathLocators.toString()).locatedBy(targetOrXpathLocators.toString());
        } else {
            target = (Target) targetOrXpathLocators;
        }
        return getTextByTargetLocators(target);
    }

    public void waitNotVisible(Target targetLocators) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isNotVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds());
    }

    public void waitVisible(Target targetLocators) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds());
    }

    public void checkElementDisplayed(Target targetLocators) {
        waitElement(targetLocators);
        theActorInTheSpotlight().attemptsTo(Ensure.that(targetLocators).isDisplayed());
    }

    public void checkElementNotDisplayed(Target targetLocators) {
        waitNotVisible(targetLocators);
        theActorInTheSpotlight().attemptsTo(Ensure.that(targetLocators).isNotDisplayed());
    }

    public void checkElementNotEnable(Target targetLocators) {
        waitVisible(targetLocators);
        theActorInTheSpotlight().attemptsTo(Ensure.that(targetLocators).isDisabled());
    }

    public String getValueByAttribute(Target targetLocators, String attribute) {
        waitPresentElement(targetLocators.getCssOrXPathSelector());
        return Serenity.getDriver().findElement(By.xpath(targetLocators.getCssOrXPathSelector())).getAttribute(attribute);
    }

    public void openNewTabAndSwitchToNewTab(String indexTab) {
        ((JavascriptExecutor) Serenity.getDriver()).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<>(Serenity.getDriver().getWindowHandles());
        Serenity.getDriver().switchTo().window(tabs.get(Integer.parseInt(indexTab) - 1));
        getDriver().manage().window().maximize();
    }

    public void closeCurrentTab() {
        ((JavascriptExecutor) Serenity.getDriver()).executeScript("window.close()");
        ArrayList<String> tabs = new ArrayList<>(Serenity.getDriver().getWindowHandles());
        Serenity.getDriver().switchTo().window(tabs.get(0));
    }

    public void switchTabByIndex(String indexTab) {
        ArrayList<String> tabs = new ArrayList<>(Serenity.getDriver().getWindowHandles());
        Serenity.getDriver().switchTo().window(tabs.get(Integer.parseInt(indexTab) - 1));
        pause(1000);
    }

    public void doubleClick(Target targetLocator) {
        Actions act = new Actions(Serenity.getDriver());
        act.moveToElement(Serenity.getDriver().findElement(By.xpath(targetLocator.getCssOrXPathSelector()))).doubleClick().build().perform();
    }

    public void switchToIframeById(String id) {
        Serenity.getDriver().switchTo().frame(Serenity.getDriver().findElement(By.id(id)));
        pause(500);
    }

    public void switchToIframeByXpath(String xpath) {
        Serenity.getDriver().switchTo().frame(getDriver().findElement(By.xpath(xpath)));
        pause(500);
    }

    public void switchToIframeByTarget(Target target) {
        switchToIframeByXpath(target.getCssOrXPathSelector());
    }

    public void switchToDefaultContent() {
        Serenity.getDriver().switchTo().defaultContent();
    }

    public static void writeText(String pathFile, String text, boolean isRewrite) {
        try (FileWriter fw = new FileWriter(pathFile, isRewrite);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getTextFile(String path) {
        List<String> lineText = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();

            while (line != null) {
                lineText.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineText;
    }

    public String getNumeric(String text) {
        return text.replaceAll("\\D", "");
    }

    public String randomText() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public String randomNumber(Integer number) {
        return RandomStringUtils.randomNumeric(number);
    }

    public String convertTimestampToDate(long timestamp, String formatDate) {
        Timestamp stamp = new Timestamp(timestamp);
        Date date = new Date(stamp.getTime());
        String newstring = new SimpleDateFormat(formatDate).format(date);
        System.out.println(newstring); // 2011-01-18
        return newstring;
    }

    public static String formatCurrentDateTimeToString() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        return currentDateTime.format(formatter);
    }

    public void waitLoadingMaskNotDisplay(Target targetLocators) {
        int time = 0;
        while (true) {
            try {
                Serenity.getDriver().findElement(By.xpath(targetLocators.getCssOrXPathSelector()));
                System.out.println("Loading mask display: " + time + "s");
            } catch (Exception e) {
                break;
            }
            pause(1000);
            time++;
            if (time == CommonConstant.TIME_OUT) {
                break;
            }
        }
    }

    public WebElement getElement(Target targetLocators) {
        return Serenity.getDriver().findElement(By.xpath(targetLocators.getCssOrXPathSelector()));
    }

    public void scrollElementByJs(Target targetLocators) {
        ((JavascriptExecutor) Serenity.getDriver()).executeScript("arguments[0].scrollIntoView(true);", getElement(targetLocators));
        pause(500);
    }

    public void scrollElementByJs(String xpath) {
        Target targetLocators = Target.the(xpath).locatedBy(xpath);
        scrollElementByJs(targetLocators);
    }

    public void scrollElementByJs(WebElement webElement) {
        ((JavascriptExecutor) Serenity.getDriver()).executeScript("arguments[0].scrollIntoView(true);", webElement);
        pause(500);
    }

    public void selectDropListByValue(Target targetLocators, String value) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds(),
                SelectFromOptions.byValue(value).from(targetLocators));
    }

    public void hoverElement(String element) {
        theActorInTheSpotlight().attemptsTo(HoverOverBy.over(By.xpath(element)));
    }

    public void waitElementDisplayIn60Seconds(Target targetLocators) {
        int time = 1;
        int implicitWait = 3;
        int timeout = 60 / implicitWait;
        while (true) {
            try {
                Serenity.getDriver().findElement(By.xpath(targetLocators.getCssOrXPathSelector()));
                System.out.println(targetLocators.getCssOrXPathSelector() + " display.");
                break;
            } catch (Exception e) {
                System.out.println("Wait " + targetLocators.getName() + " display: " + (time * implicitWait) + "/" + timeout);
            }
            time++;
            if (time * implicitWait >= timeout) { // 12 * 5 = 60s
                break;
            }
        }
    }

    public void waitElementNotDisplay(Target targetLocators) {
        int time = 1;
        int timeout = CommonConstant.TIME_OUT;
        while (true) {
            try {
                Serenity.getDriver().findElement(By.xpath(targetLocators.getCssOrXPathSelector()));
                System.out.println(targetLocators.getName() + " display " + time + "/" + timeout);
                pause(1000);
            } catch (Exception e) {
                System.out.println(targetLocators.getName() + " not display.");
                break;
            }
            time++;
            if (time >= timeout) {
                break;
            }
        }
    }

    public void clickElementByJs(Target targetLocators) {
        clickElementByJs(targetLocators.getCssOrXPathSelector());
    }

    public void clickElementByJs(String xpath) {
        WebElement element = Serenity.getDriver().findElement(By.xpath(xpath));
        JavascriptExecutor executor = (JavascriptExecutor) Serenity.getDriver();
        executor.executeScript("arguments[0].click();", element);
    }

    public void selectDropListByIndex(Target targetLocators, int index) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds(),
                SelectFromOptions.byIndex(index).from(targetLocators));
    }

    public WebElement existElement(String xpath) {
        try {
            pause(500);
            return getDriver().findElement(By.xpath(xpath));
        } catch (Exception e) {
            return null;
        }
    }

    public void scrollElementIntoMiddle(Target targetLocators) {
        WebElement element = getDriver().findElement(By.xpath(targetLocators.getCssOrXPathSelector()));
        scrollElementIntoMiddle(element);
        System.out.println("ScrollElementIntoMiddle: " + targetLocators.getName());
    }

    public void scrollElementIntoMiddle(WebElement element) {
        String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";
        ((JavascriptExecutor) getDriver()).executeScript(scrollElementIntoMiddle, element);
        pause(1000);
    }

    public void scrollElementIntoMiddle(String xpath) {
        WebElement element = getDriver().findElement(By.xpath(xpath));
        scrollElementIntoMiddle(element);
        System.out.println("ScrollElementIntoMiddle: " + xpath);
    }

    public void waitElementTime(Target targetLocators) {
        int count = 1;
        while (count < CommonConstant.TIME_OUT / 12) {
            try {
                WebElement element = getDriver().findElement(By.xpath(targetLocators.getCssOrXPathSelector()));
                element.isDisplayed();
                break;
            } catch (Exception e) {
                pause(1000);
                System.out.println("Wait element: " + count * 5 + "s");
                count++;
            }
        }
    }

    public String getTextByXpathLocators(String xpath) {
//        waitElementAndCheckDisplay(xpath);
        return getDriver().findElement(By.xpath(xpath)).getText();
    }

    public String getValueByXpathLocators(String xpath) {
        return getDriver().findElement(By.xpath(xpath)).getAttribute("value");
    }

    public void scrollElementHorizontally(Target targetLocators) {
        WebElement element = getDriver().findElement(By.xpath(targetLocators.getCssOrXPathSelector()));

        // Scroll to div's most right:
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollLeft = arguments[0].offsetWidth", element);
    }

    public String getValueByAttribute(String xpathExpression, String attribute) {
        waitPresentElement(xpathExpression);
        return Serenity.getDriver().findElement(By.xpath(xpathExpression)).getAttribute(attribute);
    }

    public void selectDropListByText(String xpathExpression, String value) {
        Target targetLocators = Target.the(xpathExpression).locatedBy(xpathExpression);
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds(),
                SelectFromOptions.byVisibleText(value).from(targetLocators));
    }

    public void clickElementByJs(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) Serenity.getDriver();
        executor.executeScript("arguments[0].click();", element);
    }

    public String convertDate(String dateString, String formatDateString, String formatExpectDate) {
        SimpleDateFormat dt = new SimpleDateFormat(formatDateString);
        Date date = null;
        try {
            date = dt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dt1 = new SimpleDateFormat(formatExpectDate);
        return dt1.format(date);
    }

    public int differenceBetweenTwoDates(String date1, String date2, String formatDateString) {
        SimpleDateFormat dt = new SimpleDateFormat(formatDateString);
        Date date1st = null;
        Date date2nd = null;
        int result = 0;
        try {
            date1st = dt.parse(date1);
            date2nd = dt.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diffInMillies = date1st.getTime() - date2nd.getTime();
        if (diffInMillies > 0)
            result = 1;
        else if (diffInMillies < 0)
            result = -1;
        return result;
    }

    public void waitElementAndCheckDisplay(String xpath) {
        Target targetLocators = Target.the(xpath).locatedBy(xpath);
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds());
        theActorInTheSpotlight().attemptsTo(Ensure.that(targetLocators).isDisplayed());
    }

    public void waitElementInvisibleAndCheckNotDisplay(String xpath) {
        Target targetLocators = Target.the(xpath).locatedBy(xpath);
        waitElementInvisibleAndCheckNotDisplayByTarget(targetLocators);
    }

    public void waitElementInvisibleAndCheckNotDisplayByTarget(Target target) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(target, isNotVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds());
        theActorInTheSpotlight().attemptsTo(Ensure.that(target).isNotDisplayed());
    }

    public void typeTextByWebDriver(String xpathExpression, String value) {
        waitElement(xpathExpression);
        getDriver().findElement(By.xpath(xpathExpression)).sendKeys(value);
    }

    public String getCurrentDate(String formatDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatDate);
        return formatter.format(new Date());
    }

    public String addDate(String beforeDate, String formatDate, int days) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(beforeDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DATE, days);  // number of days to add
        return sdf.format(calendar.getTime());
    }

    public String addHours(String beforeDate, String formatDate, int hours) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(beforeDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return sdf.format(calendar.getTime());
    }

    public String getDayFromDate(String date) {
        LocalDate currentDate = LocalDate.parse(date);
        int day = currentDate.getDayOfMonth();
        return String.valueOf(day);
    }

    public String getDayOfWeek(String date) {
        LocalDate dateOfWeek = LocalDate.parse(date);
        return dateOfWeek.getDayOfWeek().toString();
    }

    public WebElement existElement(WebElement element) {
        try {
            pause(1000);
            return element;
        } catch (Exception e) {
            return null;
        }
    }

    public void waitElementAndCheckDisplay(Target targetLocators) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isVisible()).forNoMoreThan(CommonConstant.TIME_OUT).seconds());
        theActorInTheSpotlight().attemptsTo(Ensure.that(targetLocators).isDisplayed());
    }

    public void waitElementAndCheckClick(Target targetLocators) {
        theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(targetLocators, isClickable()).forNoMoreThan(CommonConstant.TIME_OUT).seconds());
        theActorInTheSpotlight().attemptsTo(Ensure.that(targetLocators).isDisplayed());
    }

    public void assertDataEqualInReport(String expected, String actual, String fieldName) {
        boolean isEqual;
        if (expected == null && actual == null) {
            isEqual = true;
        } else if (expected != null) {
            isEqual = expected.equals(actual);
        } else {
            isEqual = false;
        }
        if (isEqual) {
            Serenity.recordReportData().withTitle(fieldName).andContents(" : Expected = " + expected + ", Actual = " + actual);
            theActorInTheSpotlight().attemptsTo(Ensure.that(expected).isEqualTo(expected));
        } else {
            Serenity.recordReportData().withTitle(" ===> FAILED ===" + fieldName).andContents(": Expected = " + expected + ", Actual = " + actual);
        }
    }

    public void printAndReportData(String textDescription, Object text) {
        System.out.println(textDescription + " = " + text);
        Serenity.recordReportData().withTitle(textDescription + " = " + text).andContents(text.toString());
    }

    public static int getPlaceRoundPriceApi(String price) {
        int places;
        int indexOfPlaces = price.indexOf(".");
        if (-1 == indexOfPlaces) {
            return 0;
        }
        // 1799.423
        places = price.substring(indexOfPlaces + 1).length(); // 423.length
        return places;
    }

    public static double getRound(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

//    public static void comparePriceApi(Condition condition, double actual, double expected) {
//        int places = getPlaceRoundPriceApi(String.valueOf(actual));
//        System.out.println("ComparePriceApi: (actual - expected: " + actual + " - " + expected + ")");
//        switch (condition) {
//            case EQUALS:
//                theActorInTheSpotlight().attemptsTo(Ensure.that(actual).isEqualTo(getRound(expected, places)));
//                break;
//            case NOT_EQUALS:
//                theActorInTheSpotlight().attemptsTo(Ensure.that(actual).isNotEqualTo(getRound(expected, places)));
//                break;
//            case GREATER_THAN:
//                theActorInTheSpotlight().attemptsTo(Ensure.that(actual).isGreaterThan(getRound(expected, places)));
//                break;
//            case LESS_THAN:
//                theActorInTheSpotlight().attemptsTo(Ensure.that(actual).isLessThan(getRound(expected, places)));
//                break;
//            default:
//                break;
//        }
//    }

    public String getDateOf(String country, Integer plusDay) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (country.equalsIgnoreCase("pl"))
            df.setTimeZone(TimeZone.getTimeZone("Poland"));
        else if (country.equalsIgnoreCase("uk"))
            df.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        else if (country.equalsIgnoreCase("it"))
            df.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, plusDay);
        date = c.getTime();

        System.out.println("Current date and time in " + country.toLowerCase() + ": " + df.format(date));
        return df.format(date);
    }

    public String getHourAndMinutesOfDate(String date) {
        String hourAndMinutes = "";
        Pattern pattern = Pattern.compile("(..:..)");
        Matcher matcher = pattern.matcher(date);
        while (matcher.find()) {
            hourAndMinutes = matcher.group(1);
        }
        return hourAndMinutes;
    }

    public String getNumericPriceWithout00(String text) {
        if (text.contains(".00"))
            text = text.replace(".00", "");
        text = text.replaceAll("\\D", "");
        return text;
    }

    public String getText(Target targetLocators) {
        waitElement(targetLocators);
        scrollElementIntoMiddle(targetLocators);
        theActorInTheSpotlight().attemptsTo(Ensure.that(targetLocators).isDisplayed());
        String text = theActorInTheSpotlight().asksFor(Text.of(targetLocators));
        Serenity.recordReportData().withTitle("Text of " + targetLocators.getName()).andContents(text);
        return text;
    }

    public void compareDataPrice(String typeCompare, double key1, double expected, double compare) {
        if (compare == 0) {
            Serenity.recordReportData().withTitle(typeCompare + " : " + key1 + " is equals to expected " + expected + " : PASSED").andContents("PASSED");
        } else {
            Serenity.recordReportData().withTitle(typeCompare + " : " + key1 + " is not equals to expected " + expected + " : FAILED").andContents("FAILED");
        }
    }

    public String getSkuQuery(String sku, String country) {
        String[] skuWithPrefix = sku.split("\\.");
        if (country.equals("sa") || country.equals("sa_en")) {
            return "SA." + skuWithPrefix[0] + "." + skuWithPrefix[1];
        } else if (country.equals("ca_en") || country.equals("ca_fr")) {
            return "CA." + skuWithPrefix[0] + "." + skuWithPrefix[1];
        } else {
            return country.toUpperCase(Locale.ROOT) + "." + skuWithPrefix[0] + "." + skuWithPrefix[1];
        }
    }

    public String getSku(String sku) {
        String[] skuWithPrefix = sku.split("\\.");
        return skuWithPrefix[0] + "." + skuWithPrefix[1];
    }

    public void checkCheckBoxSelected(Target target) {
        WebElement element = getDriver().findElement(By.xpath(target.getCssOrXPathSelector()));
        if (element.isSelected()) {
            element.click();
        }
    }

    public String removeSpecialCharacter(String text) {
        return text.replaceAll("^\"|\"$", "");
    }

    public void waitAndClickElementToBeClickable(Target target) {
        WebDriverWait wait = new WebDriverWait(Serenity.getDriver(), Duration.ofSeconds(CommonConstant.TIME_OUT));
        new Actions(Serenity.getDriver()).moveToElement(wait.until(ExpectedConditions.elementToBeClickable(By.xpath(target.getCssOrXPathSelector()))))
                .click().build().perform();
    }

    public boolean elementIsExit(Target target) {
        waitElement(target);
        return getDriver().findElements(By.xpath(target.getCssOrXPathSelector())).size() != 0;
    }

    public boolean checkBoxIsSelected(Target target) {
        return getDriver().findElement(By.xpath(target.getCssOrXPathSelector())).isSelected();
    }

    public List<String> getTextOfList(Target targetLocators) {
        waitElement(targetLocators);
        scrollElementIntoMiddle(targetLocators);
        theActorInTheSpotlight().attemptsTo(Ensure.that(targetLocators).isDisplayed());
        return (List<String>) theActorInTheSpotlight().asksFor(Text.ofEach(targetLocators));
    }

    public List<String> getListValueByAttribute(Target targetLocators, String attribute) {
        waitPresentElement(targetLocators.getCssOrXPathSelector());
        List<String> text = Collections.singletonList(Serenity.getDriver().findElement(By.xpath(targetLocators.getCssOrXPathSelector())).getAttribute(attribute));
        return text;
    }

    public void verifyEqual(Target target, String expect) {
        theActorInTheSpotlight().attemptsTo(Ensure.that(getText(target)).isEqualTo(expect));
    }

    public void verifyEqual(String actual, String expect) {
        theActorInTheSpotlight().attemptsTo(Ensure.that(actual).isEqualTo(expect));
    }

    public String getSelectedOptionDropList(String xpath) {
        Select select = new Select(getDriver().findElement(By.xpath(xpath)));
        WebElement option = select.getFirstSelectedOption();
        return option.getText();
    }

//    public void waitAllLoadingMaskCheckoutPageNotDisplay() {
//        int time = 1;
//        while (true) {
//            String url = Serenity.getDriver().getCurrentUrl();
//            if (url.contains(Start.getAdminPage()) && null == existElement(MagentoAdminComponent.LOADING_ICON_PLEASE_WAIT)) {
//                System.out.println("Loading mask admin not display.");
//                break;
//            }
//            if (!url.contains(Start.getAdminPage()) && null == existElement(CheckoutPageComponent.ICON_LG_LOADING) && null == existElement(CheckoutPageComponent.LOADING_LOGO_LG)) {
//                System.out.println("Loading mask AEM not display.");
//                break;
//            }
//            System.out.println("Loading mask display: " + time + "s");
//            pause(1000);
//            time++;
//            if (time == CommonConstant.TIME_OUT) {
//                break;
//            }
//        }
//    }

    public void clickBackButtonOnBrowser() {
        Serenity.getDriver().navigate().back();
        pause(2000);
    }

    public void refreshPage() {
        Serenity.getDriver().navigate().refresh();
        pause(5000);
    }

    public boolean checkWebElementIsDisplayed(String xpath) {
        return getDriver().findElements(By.xpath(xpath)).size() > 0
                && getDriver().findElement(By.xpath(xpath)).isDisplayed();
    }

    public void typeTextByJs(String xpath, String text) {
        WebElement element = Serenity.getDriver().findElement(By.xpath(xpath));
        JavascriptExecutor executor = (JavascriptExecutor) Serenity.getDriver();
        executor.executeScript("arguments[0].focus();", element);
        executor.executeScript("arguments[0].value='" + text + "';", element);
        String textElement = (String) executor.executeScript("return arguments[0].value", element);
        theActorInTheSpotlight().attemptsTo(Ensure.that(textElement).isEqualTo(text));
    }

//    public static String getSuffixProduct(String country) {
//        if (country.equals("ca_en") || country.equals("ca_fr"))
//            country = "ca";
//        ConfigResource configResource = new ConfigResource(country.toLowerCase());
//        String affilateCode = configResource.getValue("AFFILIATE_CODE");
//        return "." + affilateCode + "." + country.toUpperCase();
//    }

    public static Integer getDateStandard(String country) {
        int hour = 0;
        switch (country) {
            case "UK":
            case "IT": {
                hour = 0;
                break;
            }
            case "MX": {
                hour = -7;
                break;
            }
            case "CL":
            case "BR":
            case "PE": {
                hour = -5;
                break;
            }
            case "VN": {
                hour = 6;
                break;
            }
            case "DE":
            case "FR": {
                hour = 1;
                break;
            }
            case "AU": {
                hour = 9;
                break;
            }
            case "KZ": {
                hour = 5;
                break;
            }
            case "SA":
            case "SA_EN": {
                hour = 2;
                break;
            }
        }
        return hour;
    }

    public static Integer getDate(String country) {
        int hour = 0;
        switch (country) {
            case "UK":
            case "IT": {
                hour = 0;
                break;
            }
            case "MX": {
                hour = -7;
                break;
            }
            case "CL":
            case "BR":
            case "PE": {
                hour = -5;
                break;
            }
            case "VN": {
                hour = 6;
                break;
            }
            case "DE":
            case "FR":
            case "SA":
            case "SA_EN": {
                hour = -1;
                break;
            }
            case "AU": {
                hour = 9;
                break;
            }
            case "KZ": {
                hour = 5;
                break;
            }
        }
        return hour;
    }

    public static Integer getLocalTime(String country) {
        int hour = 0;
        switch (country) {
            case "UK":
            case "IT": {
                hour = -1;
                break;
            }
            case "CL":
            case "BR": {
                hour = 4;
                break;
            }
            case "MX": {
                hour = 6;
                break;
            }
            case "VN": {
                hour = -7;
                break;
            }
            case "DE":
            case "FR": {
                hour = -2;
                break;
            }
            case "AU": {
                hour = -10;
                break;
            }
            case "KZ": {
                hour = -6;
                break;
            }
            case "SA":
            case "SA_EN": {
                hour = -3;
                break;
            }
            case "PE": {
                hour = 5;
                break;
            }
        }
        return hour;
    }

    public static Integer convertLocalTimeToCountryTime(String country) {
        int hour = 0;
        switch (country) {
            case "UK":
            case "IT": {
                hour = -6;
                break;
            }
            case "CL":
            case "BR": {
                hour = -11;
                break;
            }
            case "MX": {
                hour = -13;
                break;
            }
            case "VN": {
                hour = 0;
                break;
            }
            case "DE":
            case "FR": {
                hour = -5;
                break;
            }
            case "AU": {
                hour = 3;
                break;
            }
            case "KZ": {
                hour = -1;
                break;
            }
            case "SA":
            case "SA_EN": {
                hour = -4;
                break;
            }
            case "PE": {
                hour = -8;
                break;
            }
        }
        return hour;
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    public void scrollToBottomPage() {
        pause(2000);
        for (int i = 0; i <= 8; i++) {
            theActorInTheSpotlight().attemptsTo(Enter.keyValues(Keys.PAGE_DOWN).into("//body"));
            pause(2000);
            i++;
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static boolean checkExistElement(Target target) {
        return theActorInTheSpotlight().asksFor(Presence.of(target));
    }

    public static void checkExistElementAndClick(Target target) {
        theActorInTheSpotlight().attemptsTo(
                Check.whether(theActorInTheSpotlight().asksFor(Absence.of(target)))
                        .andIfSo(Task.where(target.getName(), Click.on(target))));
    }

    public static void checkNotExistElementAndClick(Target target) {
        theActorInTheSpotlight().attemptsTo(
                Check.whether(theActorInTheSpotlight().asksFor(Absence.of(target)))
                        .otherwise(Task.where(target.getName(), Click.on(target))));
    }

    public void hoverElementByTarget(Target targetLocators) {
        WebElement element = Serenity.getDriver().findElement(By.xpath(targetLocators.getCssOrXPathSelector()));
        Actions builder = new Actions(getDriver());
        Actions hoverOverLocationSelector = builder.moveToElement(element);
        hoverOverLocationSelector.perform();
    }

    /**
     * Create directory nếu nó chưa tồn tại
     */
    public static void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdir()) {
                System.out.println("Create directory " + directoryPath + " success.");
            }
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
    }

    public List<String> getListTextByXpathLocators(String xpath) {
        return Collections.singletonList(getDriver().findElement(By.xpath(xpath)).getText());
    }
}
