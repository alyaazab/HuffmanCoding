package com.company;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        menu();

        /*ArrayList<String> fileNames = new ArrayList<>();
        listFileForFolder(new File("mycats"), fileNames);*/

        //System.out.println(fileNames);


        /*ArrayList<String> destFilenames = new ArrayList<>();
        destFilenames.add("dbrownie.txt");
        destFilenames.add("dginger.txt");
        destFilenames.add("dpuppy.txt");*/


        //Compress.compressFile(fileNames, "compressed.txt");
        //Decompress.decompressFile("compressed.txt", destFilenames);

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
            System.out.println("1) Compress a file/folder");
            System.out.println("2) Decompress a file/folder");
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
                        long fileLength=1;


                        ArrayList<String> filenames = new ArrayList<>();
                        if(sourceFile.isDirectory()){
                            listFileForFolder(sourceFile,filenames);
                            destFilename = sourceFile.getParent() + "/compfolder_" + tokens[length-1];
                        } else {
                            destFilename = sourceFile.getParent() + "/compfile_" + tokens[length-1];
                            fileLength=sourceFile.length();
                            filenames.add(sourceFilename);
                        }

                        System.out.println(destFilename);
                        System.out.println("folder size: " + filenames.size());

                        Compress.nOfFiles=filenames.size();

                        long startTime = System.currentTimeMillis();

                        Compress.compressFile(filenames, destFilename);

                        System.out.println("execution time: " + (System.currentTimeMillis() - startTime));

                        File file = new File(sourceFile.getParent() + "/compfile_" + tokens[length-1]);
                        long compLength = file.length();
                        System.out.println("compression ratio: " + ((double)compLength/fileLength));
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
                        String filename = sourceFile.getName();
                        System.out.println(filename);
                        String [] tokens = filename.split("_");

                        ArrayList<String> filenames = new ArrayList<>();

                        if (tokens[0].equals("compfolder")){
                            String parent = sourceFile.getParent();

                            Timestamp ts = new Timestamp(new Date().getTime());

                            File file = new File(parent + "/" + tokens[1]+ts);
                            System.out.println(file.getAbsolutePath());

                            if(file.mkdir()){
                                System.out.println("directory created successfully");
                            }else {
                                System.out.println("error in creating specified directory");
                                System.exit(-1);
                            }
                            filenames.add(file.getAbsolutePath());
                        } else {
                            filenames.add(sourceFile.getParent()+"/decomp_"+tokens[1]);
                        }

                        /*System.out.println(sourceFile.getAbsolutePath());
                        sourceFilename = sourceFile.getAbsolutePath();
                        System.out.println(sourceFilename);

                        // /home/alya/desktop/comp_test.txt
                        destFilename = sourceFilename.replace("comp_", "");
                        System.out.println(destFilename);
*/

                        long startTime = System.currentTimeMillis();

                        Decompress.decompressFile(sourceFile.getAbsolutePath(), filenames);

                        System.out.println("execution time: " + (System.currentTimeMillis() - startTime));


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
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = jFileChooser.showOpenDialog(jFrame);

        if (result == JFileChooser.APPROVE_OPTION) {
            // user selects a file
            return jFileChooser.getSelectedFile();
        }
        return null;
    }
}
