package com.company;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        menu();


        Compress.compressFile("test.txt", "compressed.txt");
        Decompress.decompressFile("compressed.txt", "decompressed.txt");
    }

    private static void menu() {
        Scanner scanner = new Scanner(System.in);
        String input, sourceFilename, destFilename;
        File sourceFile = null;

        while(true)
        {
            System.out.println("\n\n\nWhat would you like to do?");
            System.out.println("1) Compress a file");
            System.out.println("2) Decompress a file");
            System.out.println("3) Exit");
            System.out.println();

            input = scanner.next();

            switch(input)
            {
                case "3":
                    System.exit(0);
                case "1":
                    //open file chooser
                    //get text file name
                    //call compressfile
                    sourceFile = selectFile();

                    if(sourceFile==null) {
                        System.out.println("Please select a file to compress.");
                        menu();
                    }
                    else
                    {
                        System.out.println(sourceFile.getAbsolutePath());

                        sourceFilename = sourceFile.getAbsolutePath();

                        String[] tokens = sourceFilename.split( "/");
                        int length = tokens.length;
                        destFilename = sourceFile.getParent() + "/" + "comp_" + tokens[length-1];
                        System.out.println(destFilename);

                        Compress.compressFile(sourceFilename, destFilename);

                    }

                    break;
                case "2":
                    //open file chooser
                    //get text file name
                    //call decompressfile
                    sourceFile = selectFile();

                    if(sourceFile==null) {
                        System.out.println("Please select a file to compress.");
                        menu();
                    }
                    else
                    {
                        System.out.println(sourceFile.getAbsolutePath());
                        sourceFilename = sourceFile.getAbsolutePath();
                        System.out.println(sourceFilename);

                        // /home/alya/desktop/comp_test.txt
                        destFilename = sourceFilename.replace("comp_", "");
                        System.out.println(destFilename);

                        Decompress.decompressFile(sourceFilename, destFilename);

                    }

                    break;
                default:
                    menu();
            }
        }
    }

    private static File selectFile() {
        JFrame jFrame = new JFrame();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = jFileChooser.showOpenDialog(jFrame);

        if (result == JFileChooser.APPROVE_OPTION) {
            // user selects a file
            File selectedFile = jFileChooser.getSelectedFile();


//            System.out.println(selectedFile.getParent());
//            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            return selectedFile;
        }
        return null;

    }


}
