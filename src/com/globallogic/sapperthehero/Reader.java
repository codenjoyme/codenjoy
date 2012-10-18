package com.globallogic.sapperthehero;

/**
 * Created with IntelliJ IDEA.
 * User: oleksii.morozov
 * Date: 10/18/12
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Reader {

    int read(String message);

    void setPrinter(Printer printer);
}
