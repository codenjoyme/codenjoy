package com.globallogic.sapperthehero.input;

import com.globallogic.sapperthehero.output.Printer;

import java.util.Scanner;

public class ConsoleReader implements Reader {
    private Printer printer;

    @Override
    public int read(String message) {
        while (true) {
            try {
                printer.print(message);
                int input = Integer.parseInt(new Scanner(System.in).nextLine());
                if (input < 1) {
                    throw new Exception();
                }
                return input;
            } catch (Exception e) {
                System.out.println("Error");
            }
        }
    }

    @Override
    public void setPrinter(Printer printer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
