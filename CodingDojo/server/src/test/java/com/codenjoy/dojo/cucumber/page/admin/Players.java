package com.codenjoy.dojo.cucumber.page.admin;

import com.codenjoy.dojo.cucumber.page.Page;
import com.codenjoy.dojo.cucumber.page.Server;
import com.codenjoy.dojo.cucumber.page.WebDriverWrapper;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.codenjoy.dojo.cucumber.utils.PageUtils.xpath;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class Players {

    // selectors
    public static final By ALL_PLAYERS = xpath("//table[@id='savePlayersGame']");
    public static final BiFunction<String, String, By> FOUND_PLAYER_BY = (key, value) -> xpath(".//input[@class='%s'][@value='%s']/../..", key, value);
    public static final Function<String, By> UPDATE_PLAYER_FIELD = key -> xpath(".//input[@class='%s']", key);
    public static final By BOARD_LINK = xpath(".//td[17]/a"); // TODO to use attribute in the td instead of index
    public static final By SAVE = xpath("./../button");

    public static final By LOAD_ALL_LINK = xpath("//a[text() = 'LoadAll']");

    // page objects
    private final Page page;
    private final WebDriverWrapper web;
    private final Server server;

    public String playerBoardLink(String selectorKey, String selectorValue) {
        WebElement player = player(selectorKey, selectorValue);
        WebElement link = player.findElement(BOARD_LINK);
        return server.relative(link.getAttribute("href"));
    }

    private WebElement player(String selectorKey, String selectorValue) {
        WebElement table = web.elementBy(ALL_PLAYERS);
        return table.findElement(FOUND_PLAYER_BY.apply(selectorKey, selectorValue));
    }

    public void updatePlayer(String selectorKey, String selectorValue, String key, String value) {
        WebElement player = player(selectorKey, selectorValue);
        WebElement field = player.findElement(UPDATE_PLAYER_FIELD.apply(key));
        field.clear();
        field.sendKeys(value);
        WebElement save = field.findElement(SAVE);
        save.click();
    }

    public WebElement loadAllLink() {
        return web.elementBy(LOAD_ALL_LINK);
    }

}