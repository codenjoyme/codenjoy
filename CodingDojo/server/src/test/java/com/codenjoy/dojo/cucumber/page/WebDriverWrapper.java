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


import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.List;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static java.util.stream.Collectors.toList;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class WebDriverWrapper {

    private WebDriver driver;
    private final Server server;

    @PostConstruct
    public void init() {
        // don't forget to specify `webdriver.chrome.driver` system property
        driver = new ChromeDriver();
        System.out.println("Started here: " + server.endpoint());
    }

    public void open(String url) {
        driver.get(server.endpoint() + url);
    }

    public WebElement element(String selector) {
        return driver.findElement(By.cssSelector(selector));
    }

    public void select(String selector, String text) {
        new Select(element(selector))
                .selectByVisibleText(text);
    }

    public void click(String selector) {
        element(selector).click();
    }

    public String url() {
        String absoluteUrl = driver.getCurrentUrl();
        String endpoint = server.endpoint();
        int position = absoluteUrl.indexOf(endpoint);
        if (position == 0) {
            return absoluteUrl.substring(endpoint.length());
        } else {
            return absoluteUrl;
        }
    }

    public void text(String selector, String text) {
        WebElement element = element(selector);
        element.clear();
        element.sendKeys(text);
    }

    public String get(String selector, String attribute) {
        return element(selector).getAttribute(attribute);
    }

    public void closeBrowser() {
        driver.close();
    }

    public boolean exists(String selector) {
        try {
            element(selector);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public List<String> options(String selector) {
        return new Select(element(selector)).getOptions().stream()
                .map(option -> option.getText())
                .collect(toList());
    }

    public List<WebElement> elementsBy(By by) {
        return driver.findElements(by);
    }

    public WebElement elementBy(By by) {
        return driver.findElement(by);
    }
}
