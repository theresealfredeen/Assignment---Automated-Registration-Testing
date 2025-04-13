package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyStepdefs {

    private WebDriver driver;
    private WebDriverWait wait;
    private String testMail = "Tinatesttant" + System.currentTimeMillis() + "@mailnesia.com";
    private String password = "Test1234!";

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void fillInFields(String firstName, String lastName, String password, String confirmPassword) {
        driver.findElement(By.cssSelector("input#dp")).sendKeys("08/01/1999");
        driver.findElement(By.cssSelector("input#member_firstname")).sendKeys(firstName);

        if (lastName != null) {
            driver.findElement(By.cssSelector("input#member_lastname")).sendKeys(lastName);
        }
        driver.findElement(By.cssSelector("input#member_emailaddress")).sendKeys(testMail);
        driver.findElement(By.cssSelector("input#member_confirmemailaddress")).sendKeys(testMail);
        driver.findElement(By.cssSelector("#signupunlicenced_password")).sendKeys(password);
        driver.findElement(By.cssSelector("#signupunlicenced_confirmpassword")).sendKeys(confirmPassword);
    }

    private void acceptAllRequiredConsents() {
        clickConsentCheckbox("sign_up_25");
        clickConsentCheckbox("sign_up_26");
        clickConsentCheckbox("fanmembersignup_agreetocodeofethicsandconduct");
    }

    private void clickConsentCheckbox(String checkboxId) {
        WebElement label = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='" + checkboxId + "']")));
        label.click();
        WebElement checkbox = driver.findElement(By.id(checkboxId));

        if (!checkbox.isSelected()) {
            label.click();
        }
    }

    private void clickRegisterButton() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn"))).click();
    }

    private void checkErrorMessage(String expectedMessage) {
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".field-validation-error")));
        String actual = error.getText();
        System.out.println("Errormessage: " + actual);
        assertEquals(expectedMessage, actual);
    }

    @Given("I am on basketballengland.co.uk")
    public void iAmOnBasketballenglandCoUk() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://membership.basketballengland.co.uk/NewSupporterAccount");
    }

    @Given("I am on basketballengland.co.uk on {string}")
    public void iAmOnBasketballenglandCoUkOn(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        }
        if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        }
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://membership.basketballengland.co.uk/NewSupporterAccount");
    }

    @When("I fill in the correct member details")
    public void iFillInTheCorrectMemberDetails() {
        fillInFields("Tina", "Testtant", password, password);
        acceptAllRequiredConsents();
        clickRegisterButton();
    }

    @When("I fill in member details without lastname")
    public void iFillInMemberDetailsWithoutLastname() {
        fillInFields("Tina", null, password, password);
        acceptAllRequiredConsents();
        clickRegisterButton();
    }

    @When("I fill in member details with mismatched passwords")
    public void iFillInMemberDetailsWithMismatchedPasswords() {
        fillInFields("Tina", "Testtant", password, "wrong123!");
        acceptAllRequiredConsents();
        clickRegisterButton();
    }

    @When("I fill in member details without accepting terms")
    public void iFillInMemberDetailsWithoutAcceptingTerms() {
        fillInFields("Tina", "Testtant", password, password);
        clickConsentCheckbox("sign_up_26");
        clickConsentCheckbox("fanmembersignup_agreetocodeofethicsandconduct");
        clickRegisterButton();
    }

    @Then("I get a missing last name error")
    public void iGetAMissingLastNameError() {
        checkErrorMessage("Last Name is required");
    }

    @Then("I get a password mismatch error")
    public void iGetAPasswordMismatchError() {
        checkErrorMessage("Password did not match");
    }

    @Then("I get a terms and conditions error")
    public void iGetATermsAndConditionsError() {
        checkErrorMessage("You must confirm that you have read and accepted our Terms and Conditions");
    }

    @Then("I successfully become a member")
    public void iSuccessfullyBecomeAMember() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2.gray")));
        String actual = driver.findElement(By.cssSelector("h2.gray")).getText();
        String expected = "THANK YOU FOR CREATING AN ACCOUNT WITH BASKETBALL ENGLAND";
        assertEquals(expected, actual);
    }

}
