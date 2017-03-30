package com.codenjoy.dojo.client;

import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;

import java.util.Scanner;

/**
 * Created by indigo on 2017-03-30.
 */
public class OneCommandSolver<T extends ClientBoard> implements Solver<T> {

    private String command;

    public OneCommandSolver(String command) {
        this.command = command;
    }
    
    private boolean processed = false;
    
    @Override
    public String get(T board) {
        if (processed) {
            System.exit(0);
            return StringUtils.EMPTY;
        } else {
            processed = true;
        }
        System.out.printf("Are you sure you want to run the command '%s' (y/n)?\n", command);
        String answer = new Scanner(System.in).next();
        if (answer.equals("y")) {
            System.out.printf("Sending '%s' to the server\n", command);
            return command;
        } else {
            System.exit(0);
            return StringUtils.EMPTY;
        }
    }
}
