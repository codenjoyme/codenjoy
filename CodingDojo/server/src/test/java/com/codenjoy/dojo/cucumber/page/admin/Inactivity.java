package com.codenjoy.dojo.cucumber.page.admin;

import com.codenjoy.dojo.cucumber.page.Page;
import com.codenjoy.dojo.cucumber.page.WebDriverWrapper;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import static com.codenjoy.dojo.cucumber.utils.PageUtils.xpath;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static org.junit.Assert.assertEquals;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class Inactivity {

    public static final By KICK_ENABLED = xpath("//input[@name='inactivity.kickEnabled']");
    public static final By TIMEOUT_INPUT = xpath("//input[@name='inactivity.inactivityTimeout']");
    public static final By SAVE_BUTTON = xpath("//table[@id='inactivity']//input[@value='Save']");

    public static final By PLAYER_INACTIVE_TICKS = xpath("//span[@class='input-ticks-inactive']");
    public static final Function<String, By> PLAYER_INACTIVE_TICK = email -> xpath("//tr[@player='%s']//span[@class='input-ticks-inactive']", email);

    private final Page page;
    private final WebDriverWrapper web;

    public List<WebElement> playersInactiveTicks() {
        return web.elementsBy(PLAYER_INACTIVE_TICKS);
    }

    public WebElement playerInactiveTicks(String email) {
        return web.elementBy(PLAYER_INACTIVE_TICK.apply(email));
    }

    public void kickEnabled(boolean enabled) {
        web.setChecked(KICK_ENABLED, enabled);
    }

    public boolean kickEnabled() {
        return web.elementBy(KICK_ENABLED).isEnabled();
    }

    public String timeout() {
        return web.elementBy(TIMEOUT_INPUT).getAttribute("value");
    }

    public void timeout(int ticks) {
        web.text(TIMEOUT_INPUT, String.valueOf(ticks));
    }

    public void submit() {
        web.elementBy(SAVE_BUTTON).click();
    }

    @Override
    public String toString() {
        return new LinkedHashMap<String, Object>(){{
            put("kickEnabled", kickEnabled());
            put("timeout", timeout());
        }}.toString();
    }

    public void assertPlayerKicked(String email, boolean isKicked) {
        assertEquals(isKicked, !playerInactiveTicks(email).isDisplayed());
    }
}
