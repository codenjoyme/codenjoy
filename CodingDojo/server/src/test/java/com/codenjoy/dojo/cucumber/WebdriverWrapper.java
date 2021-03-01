package com.codenjoy.dojo.cucumber;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class WebdriverWrapper {

    private WebDriver driver;

    @Autowired
    private Server server;

    @PostConstruct
    public void init() {
        driver = new HtmlUnitDriver(true);
    }

    public void open(String url) {
        driver.get(server.endpoint() + url);
    }
}
