package com.globallogic.sapperthehero.controller.output.impl;


import com.globallogic.sapperthehero.controller.output.Printer;

/**
 * Created with IntelliJ IDEA.
 * User: oleksii.morozov
 * Date: 10/18/12
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConsolePrinter implements Printer {

    @Override
    public void print(String toString) {
        System.out.print(toString);
    }
}
