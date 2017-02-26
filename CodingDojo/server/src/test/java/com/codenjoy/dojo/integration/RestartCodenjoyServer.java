package com.codenjoy.dojo.integration;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Created by indigo on 2017-02-26.
 */
public class RestartCodenjoyServer {

    public static final String SERVER_URL = "http://uc2.nodecluster.net/";

    private final HtmlUnitDriver driver;
    private String login;
    private String password;

    public static void main(String[] args) {
        RestartCodenjoyServer console = new RestartCodenjoyServer("login", "password");

        if (console.login()) {
            console.restart();
            console.logout();
        }
    }

    public RestartCodenjoyServer(String login, String password) {
        this.login = login;
        this.password = password;
        driver = new HtmlUnitDriver(true);
    }

    private boolean login() {
        driver.get(SERVER_URL + "jcp/");

        submitLoginForm(login, password);

        if (!isMainPage()) {
            System.out.println("Login failure!");
            return false;
        }

        System.out.println("Login success!");
        return true;
    }

    private void restart() {
        System.out.println("Try to restart...");

        driver.findElement(By.xpath("/html/body//form[@id='restart-form']//input[@type='submit' and @name='Restart']")).click();

        System.out.println("Restarting...");
    }

    private void logout() {
        System.out.println("Try to logout..");

        clickLogout();

        if (!isLoginPage()) {
            System.out.println("Logout failure!");
            return;
        }

        System.out.println("Logout success..");
    }

    private boolean isLoginPage() {
        return driver.getCurrentUrl().contains("uc2.nodecluster.net/jcp/");
    }

    private boolean isMainPage() {
        return driver.getCurrentUrl().contains("uc2.nodecluster.net/jcp/my/appserver");
    }

    private void clickLogout() {
        driver.get(SERVER_URL + "/jcp/site/logout");
    }

    private void submitLoginForm(String login, String password) {
        driver.findElement(By.id("LoginForm_username")).sendKeys(login);
        driver.findElement(By.id("LoginForm_password")).sendKeys(password);
        driver.findElement(By.xpath("/html/body//form[@id='login-form']//input[@type='submit' and @value='Login']")).click();
    }
}
