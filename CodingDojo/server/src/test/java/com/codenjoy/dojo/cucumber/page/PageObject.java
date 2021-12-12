package com.codenjoy.dojo.cucumber.page;

import com.codenjoy.dojo.web.controller.admin.AdminPostActions;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PageObject {

    // application services
    @Autowired
    protected AdminPostActions actions;

    // page objects
    @Autowired
    protected Page page;

    @Autowired
    protected WebDriverWrapper web;

    @Autowired
    protected Server server;

    @Autowired
    protected Storage storage;

    @Autowired
    protected WebsocketClients clients;
}
