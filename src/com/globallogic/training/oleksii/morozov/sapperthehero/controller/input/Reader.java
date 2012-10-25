package com.globallogic.training.oleksii.morozov.sapperthehero.controller.input;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.output.Printer;

/**
 * User: oleksii.morozov
 * Date: 10/18/12
 * Time: 3:09 PM
 */
public interface Reader {

    int read(String message);

    void setPrinter(Printer printer);
}
