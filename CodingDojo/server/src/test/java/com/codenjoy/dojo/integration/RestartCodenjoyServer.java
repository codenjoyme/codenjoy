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
        driver.get(SERVER_URL + "jcp/");

        driver.findElement(By.id("LoginForm_username")).sendKeys("login");
        driver.findElement(By.id("LoginForm_password")).sendKeys("password");
        driver.findElement(By.xpath("/html/body//form[@id='login-form']//input[@type='submit' and @value='Login']")).click();

        if (!driver.getCurrentUrl().contains("uc2.nodecluster.net/jcp/my/appserver")) {
            System.out.println("Login failure!");
            return;
        }

        System.out.println("Login success!");

        System.out.println("Try to restart...");

        driver.findElement(By.xpath("/html/body//form[@id='restart-form']//input[@type='submit' and @name='Restart']")).click();

        System.out.println("Try to logout..");

        driver.get(SERVER_URL + "/jcp/site/logout");

        if (!driver.getCurrentUrl().contains("uc2.nodecluster.net/jcp/")) {
            System.out.println("Logout failure!");
            return;
        }

        System.out.println("Logout success..");
    }
}
