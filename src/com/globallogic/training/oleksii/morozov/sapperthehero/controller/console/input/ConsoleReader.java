package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;

import java.util.Scanner;

/**
 * User: oleksii.morozov
 * Date: 10/18/12
 * Time: 3:06 PM
 */
public class ConsoleReader implements Reader {
    private Printer printer;

    public int read(String message) {
        while (true) {
            try {
                printer.print(message);
                int input = Integer.parseInt(new Scanner(System.in).nextLine());
                if (input < 1) {
                    throw new IllegalArgumentException();
                }
                return input;
            } catch (IllegalArgumentException e) {
                System.out.println("Error");
            }
        }
    }
    
    
    public String readWorld(String message) {
        while (true) {
            try {
                printer.print(message);
                String input = new Scanner(System.in).next();
                return input;
            } catch (IllegalArgumentException e) {
                System.out.println("Error");
            }
        }
    }
    
    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

}
