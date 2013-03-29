package com.javatrainee.tanks;

import java.util.Scanner;

public class Engine {

    public static void main (String []args) {
        Tanks game = new Tanks();
        Tank tank = new Tank(6, 6);
        Field field = game.getField();
        field.setTank(tank);
        Construction construction = new Construction(2,3);
        field.setConstruction(construction);
        Printer printer = new Printer (field);
        System.out.println(printer.drawField());
        while(true) {
            String userCommand = new Scanner(System.in).nextLine();
            if(userCommand.equals("w")) {tank.moveUp();}
            else if(userCommand.equals("s")) {tank.moveDown();}
            else if(userCommand.equals("a")) {tank.moveLeft();}
            else if(userCommand.equals("d")) {tank.moveRight();}
            else if(userCommand.equals(" ")){tank.fire();}
            game.tact();
            field.setTank(tank);
            System.out.println(printer.drawField());
        }
    }
}
