package com.codenjoy.dojo.cucumber.page;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Closeable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class WebDriverWrapper implements Closeable {

    public static final String CHROME_WEB_DRIVER = "webdriver.chrome.driver";

    private WebDriver driver;
    private final Server server;

    @PostConstruct
    public void init() {
        if (System.getProperty(CHROME_WEB_DRIVER) == null) {
            System.setProperty(CHROME_WEB_DRIVER, determineChromeWebDriverLocation());
        }
        driver = new ChromeDriver();
        log.info("Started here: {}", server.path());
    }

    private static String determineChromeWebDriverLocation() {
        String windowsLocation = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe";
        String macOsXLocation = "/Applications/chromedriver";
        String unixOsLocation = "/usr/local/bin/chromedriver";

        if (new File(windowsLocation).exists()) {
            return windowsLocation;
        }
        if (new File(macOsXLocation).exists()) {
            return macOsXLocation;
        }
        if (new File(unixOsLocation).exists()) {
            return unixOsLocation;
        }

        throw new IllegalStateException("unable to determine a location of `webdriver.chrome.driver`");
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public void open(String url) {
        driver.get(server.path() + url);
    }

    public WebElement element(String css) {
        return driver.findElement(By.cssSelector(css));
    }

    public void select(String css, String text) {
        new Select(element(css))
                .selectByVisibleText(text);
    }

    public void click(String css) {
        element(css).click();
    }

    public String url() {
        return server.relative(driver.getCurrentUrl());
    }

    public void text(String css, String text) {
        text(element(css), text);
    }

    public void text(By by, String text) {
        text(elementBy(by), text);
    }

    private void text(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    public String get(String css, String attribute) {
        return element(css).getAttribute(attribute);
    }

    @Override
    public void close() {
        driver.close();
    }

    public boolean exists(String css) {
        try {
            element(css);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public List<String> options(String css) {
        return new Select(element(css)).getOptions().stream()
                // option.getText() returns '' for hidden select/options
                .map(option -> option.getAttribute("value"))
                .sorted()
                .collect(toList());
    }

    public List<WebElement> elementsBy(By by) {
        return driver.findElements(by);
    }

    public WebElement elementBy(By by) {
        return driver.findElement(by);
    }

    public void setChecked(By by, boolean enabled) {
        WebElement checkbox = elementBy(by);
        if (enabled ^ checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public WebDriver driver() {
        return driver;
    }
}
