package com.codenjoy.dojo.integration;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Created by indigo on 2017-02-26.
 */
public class RestartCodenjoyServer {

    public static final String SERVER_URL = "http://uc2.nodecluster.net/";

    public static void main(String[] args) {
        HtmlUnitDriver driver = new HtmlUnitDriver(true);

        if (login(driver)) {
            restart(driver);
            logout(driver);
        }
    }

    private static boolean login(HtmlUnitDriver driver) {
        driver.get(SERVER_URL + "jcp/");

        submitLoginForm(driver, "login", "password");

        if (!isMainPage(driver)) {
            System.out.println("Login failure!");
            return false;
        }

        System.out.println("Login success!");
        return true;
    }

    private static void restart(HtmlUnitDriver driver) {
        System.out.println("Try to restart...");

        driver.findElement(By.xpath("/html/body//form[@id='restart-form']//input[@type='submit' and @name='Restart']")).click();

        System.out.println("Restarting...");
    }

    private static void logout(HtmlUnitDriver driver) {
        System.out.println("Try to logout..");

        clickLogout(driver);

        if (!isLoginPage(driver)) {
            System.out.println("Logout failure!");
            return;
        }

        System.out.println("Logout success..");
    }

    private static boolean isLoginPage(HtmlUnitDriver driver) {
        return driver.getCurrentUrl().contains("uc2.nodecluster.net/jcp/");
    }

    private static boolean isMainPage(HtmlUnitDriver driver) {
        return driver.getCurrentUrl().contains("uc2.nodecluster.net/jcp/my/appserver");
    }

    private static void clickLogout(HtmlUnitDriver driver) {
        driver.get(SERVER_URL + "/jcp/site/logout");
    }

    private static void submitLoginForm(HtmlUnitDriver driver, String login, String password) {
        driver.findElement(By.id("LoginForm_username")).sendKeys(login);
        driver.findElement(By.id("LoginForm_password")).sendKeys(password);
        driver.findElement(By.xpath("/html/body//form[@id='login-form']//input[@type='submit' and @value='Login']")).click();
    }
}
