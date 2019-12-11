package com.company;

import java.io.*;

public class FileMethods {

    static int[] frequency = new int[128];

    public static void populateFrequencyArray(String filename) {
        File file = new File(filename);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int c = 0;

            while((c = bufferedReader.read()) != -1)
            {
//                char character = (char) c;
                System.out.println(c);
                frequency[c]++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        printFrequencyArray();

    }

    public static void printFrequencyArray(){
        for(int i=0; i<frequency.length; i++)
            System.out.println((char)i + "\t" + frequency[i]);

    }


}
