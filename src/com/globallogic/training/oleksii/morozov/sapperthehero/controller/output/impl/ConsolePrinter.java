package com.globallogic.training.oleksii.morozov.sapperthehero.controller.output.impl;


import com.globallogic.training.oleksii.morozov.sapperthehero.controller.output.Printer;


public class ConsolePrinter implements Printer {

    @Override
    public void print(String toString) {
        System.out.println(toString);
    }
}
