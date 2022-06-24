package pages;

import aquality.selenium.forms.Form;
import forms.LoginForm;
import org.openqa.selenium.By;

public class RegistrationPage extends Form {
    private final LoginForm loginForm = new LoginForm();

    public RegistrationPage() {
        super(By.xpath("//div[@class='LoginMobilePromo clear_fix']"), "Registration Page");
    }

    public LoginForm getLoginForm() {
        return loginForm;
    }
}
