package com.globallogic.training.oleksii.morozov.sapperthehero.boardsaver;

class BoardSaver implements Saver {

    private static final String FILE_PATH = "boards/";

    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

}
