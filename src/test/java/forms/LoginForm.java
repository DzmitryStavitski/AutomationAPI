package forms;

import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

public class LoginForm extends Form {

    private final ITextBox emailField = getElementFactory().getTextBox(By.xpath("//input[@name='email']"), "Email");
    private final ITextBox passwordField = getElementFactory().getTextBox(By.xpath("//input[@name='pass']"), "Password");
    private final IButton loginButton = getElementFactory().getButton(By.xpath("//button[@id='index_login_button']"), "Login");


    public LoginForm() {
        super(By.id("index_login"), "login form");
    }

    public void login(String email, String password) {
        emailField.clearAndType(email);
        passwordField.clearAndType(password);
        loginButton.click();
    }
}
