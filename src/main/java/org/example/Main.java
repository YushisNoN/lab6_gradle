package org.example;

import utils.files.FileReader;
import utils.kernel.Kernel;

import java.io.FileNotFoundException;
import java.io.IOException;
public class Main {
    public static void main(String[] args) throws IOException {
        Kernel kernel = new Kernel();
        kernel.setCommands();
        if (args.length == 1) {
            try {
                FileReader.setFileName(args[0]);
            } catch (FileNotFoundException exception) {
                System.out.println(exception.getMessage());
            }
        }
        kernel.runProgram();
    }
}
/*
TODO: remove_greater, add_if_max, add_if_min, update_id
 */

