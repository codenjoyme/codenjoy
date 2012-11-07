package net.tetris.online.web.controller;

import java.io.IOException;
import java.io.Writer;

/**
 * User: serhiy.zelenin
 * Date: 11/7/12
 * Time: 3:04 PM
 */
public interface JsonSerializer {
    <T> void  serialize(Writer w, T value) throws IOException;
}
