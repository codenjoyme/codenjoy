package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.impl;


import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;


public class ConsolePrinter implements Printer {

    @Override
    public void print(String toString) {
        System.out.println(toString);
    }
}
