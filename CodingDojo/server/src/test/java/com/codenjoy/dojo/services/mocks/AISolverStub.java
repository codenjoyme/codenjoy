package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;

public class AISolverStub implements Solver {
    @Override
    public String get(ClientBoard board) {
        return null;
    }
}