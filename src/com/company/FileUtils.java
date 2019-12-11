package com.company;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class FileUtils {

    //create a hashmap that stores each character's ascii code and its frequency in the file
    static HashMap<Integer, Integer> frequency = new HashMap<>();

    //create a priority queue that stores all hashmap entries, ordered in ascending frequency order
    static PriorityQueue<HashMap.Entry<Integer, Integer>> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));


    //this method takes in a file name
    //reads the file character by character and populates the frequency hashmap and the priority queue
    public static void calculateFrequencies(String filename) {
        try {
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int c;

            //read from file character by character, incrementing each character's frequency when it is encountered
            while((c = bufferedReader.read()) != -1)
            {
                if(frequency.get(c) == null)
                    frequency.put(c, 1);
                else
                    frequency.put(c, frequency.get(c)+1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //add all hashmap entries into priority queue
        priorityQueue.addAll(frequency.entrySet());

        //remove all elements from queue to show that they are removed in order of the smallest frequency
        while(!priorityQueue.isEmpty())
        {
            System.out.println(priorityQueue.poll());
        }
    }
}
