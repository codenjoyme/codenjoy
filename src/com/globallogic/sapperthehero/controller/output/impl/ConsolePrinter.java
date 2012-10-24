package com.globallogic.sapperthehero.controller.output.impl;


import com.globallogic.sapperthehero.controller.output.Printer;


public class ConsolePrinter implements Printer {

    @Override
    public void print(String toString) {
        System.out.print(toString);
    }
}
