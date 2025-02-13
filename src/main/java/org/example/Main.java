package org.example;

import com.jcraft.jsch.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if(args.length!=4){
        System.out.println("Wrong Arguments");
        return;
        }
        ConnectionController controller;
        try {
            controller = new ConnectionController(args[0],args[1],args[2],args[3]);
        } catch (JSchException e) {
            System.out.println("Connection Error");
            return;
        }

        controller.Disconnect();
    }
}