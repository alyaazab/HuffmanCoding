package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        menu();
        Compress.compressFile("test.txt");
        System.out.println("----");
        Decompress.decompressFile("compressed.txt");
    }

    private static void menu() {
        Scanner scanner = new Scanner(System.in);
        int input;

        while(true)
        {
            System.out.println("What would you like to do?");
            System.out.println("1) Compress a file");
            System.out.println("2) Decompress a file");
            System.out.println("3) Exit");
            System.out.println();

            input = Integer.parseInt(scanner.next());

            switch(input)
            {
                case 1:
                    //open file chooser
                    //get text file name
                    //call compressfile
                    break;
                case 2:
                    //open file chooser
                    //get text file name
                    //call decompressfile
                    break;
                case 3:
                    System.exit(0);
                default:
                    menu();
            }


        }
    }


}
