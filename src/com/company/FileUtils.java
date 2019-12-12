package com.company;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class FileUtils {

    static String code = "";
    static Node root;

    //create a hashmap that stores each character's ascii code and its frequency in the file
    static HashMap<Integer, Integer> frequency = new HashMap<>();

    //create a priority queue that stores all hashmap entries, ordered in ascending frequency order
//    static PriorityQueue<HashMap.Entry<Integer, Integer>> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
    static PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

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

        //add nodes from hashmap into priority queue
        frequency.forEach((key, value) -> {
            Node node = new Node((int) key, (int) value);
            priorityQueue.add(node);
        });


        //create the huffman tree and print it
        Node root = createHuffmanTree();
        printTree(root);
    }

    public static Node createHuffmanTree() {

        int size = priorityQueue.size();
        System.out.println(size);
        Node x, y;

        for(int i=1; i<size; i++)
        {
            Node node = new Node();
            x = priorityQueue.poll();
            y = priorityQueue.poll();
            node.left = x;
            node.right = y;
            System.out.println(i +"\t" + (char)x.character +": " + x.freq + "\t" + (char)y.character +": " + y.freq);
            node.freq = x.freq + y.freq;
            priorityQueue.add(node);
        }
        root = priorityQueue.peek();
        return priorityQueue.poll();
    }

    public static void traverseInorder (Node rootNode){
        if(rootNode.getLeft() == null && rootNode.getRight() == null){
            System.out.println((char)rootNode.character + ": " + code);
        }
        else{
            code+="0";
            traverseInorder(rootNode.getLeft());
            code = code.substring(0,code.length()-1);
            code+="1";
            //visit
            traverseInorder(rootNode.getRight());
            code = code.substring(0,code.length()-1);
        }
    }

    public static void printTree (Node rootNode){
        if(rootNode != null){
            printTree(rootNode.getLeft());
            System.out.println((char)rootNode.character + ": " + rootNode.freq);
            printTree(rootNode.getRight());
        }
    }
}
