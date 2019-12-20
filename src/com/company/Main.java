package com.company;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        menu();

        ArrayList<String> fileNames = new ArrayList<>();
        listFileForFolder(new File("mycats"), fileNames);

        System.out.println(fileNames);


        ArrayList<String> destFilenames = new ArrayList<>();
        destFilenames.add("dbrownie.txt");
        destFilenames.add("dginger.txt");
        destFilenames.add("dpuppy.txt");


        Compress.compressFile(fileNames, "compressed.txt");
        Decompress.decompressFile("compressed.txt", destFilenames);

        System.out.println("comp");
        System.out.println(Compress.compressionCode);
    }

    public static void listFileForFolder(final File folder, ArrayList<String> fileNames) {
        for(final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if(fileEntry.isDirectory())
            {
                listFileForFolder(fileEntry, fileNames);
            }
            else
            {
                fileNames.add(fileEntry.getAbsolutePath());
                System.out.println(fileEntry.getName());
            }
        }
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

//                        Compress.compressFile(sourceFilename, destFilename);

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

//                        Decompress.decompressFile(sourceFilename, destFilename);

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
            return jFileChooser.getSelectedFile();
        }
        return null;
    }
}
