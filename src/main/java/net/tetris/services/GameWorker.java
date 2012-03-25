package net.tetris.services;

public interface GameWorker {

    void nextStep();

    void control(String command);
}
