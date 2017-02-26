package com.codenjoy.console;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by indigo on 2017-02-26.
 */
public class RestartCodenjoyServer {

    private static Logger logger = LoggerFactory.getLogger(RestartCodenjoyServer.class);

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

    public boolean login() {
        try {
            driver.get(SERVER_URL + "jcp/");

            submitLoginForm(login, password);

            if (!isMainPage()) {
                logger.warn("Login failure!");
                return false;
            }

            logger.debug("Login success!");
            return true;
        } catch (Exception e) {
            logger.error("Error when login", e);
            return false;
        }
    }

    public void restart() {
        doIt("Force Restart");
    }

    public void start() {
        doIt("Start");
    }

    public void stop() {
        doIt("Stop");
    }

    private void doIt(String buttonName) {
        try {
            logger.debug("Try to " + buttonName + "...");

            click(buttonName);

            logger.debug(buttonName + " in progress...");
        } catch (Exception e) {
            logger.error("Error when " + buttonName, e);
        }
    }

    public void logout() {
        try {
            logger.debug("Try to logout..");

            clickLogout();

            if (!isLoginPage()) {
                logger.warn("Logout failure!");
                return;
            }

            logger.debug("Logout success..");
        } catch (Exception e) {
            logger.error("Error when logout", e);
        }
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

    private void click(String buttonName) {
        driver.findElement(By.xpath("/html/body//form[@id='restart-form']//input[@type='submit' and @value='" + buttonName + "']")).click();
    }

    private void submitLoginForm(String login, String password) {
        driver.findElement(By.id("LoginForm_username")).sendKeys(login);
        driver.findElement(By.id("LoginForm_password")).sendKeys(password);
        driver.findElement(By.xpath("/html/body//form[@id='login-form']//input[@type='submit' and @value='Login']")).click();
    }
}
