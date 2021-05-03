package com.codenjoy.dojo.cucumber.page;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

@UtilityClass
public class PageUtils {

    public static By xpath(String url, String... parameters) {
        return By.xpath(String.format(url, parameters));
    }

}
