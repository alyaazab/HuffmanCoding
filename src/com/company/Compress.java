package com.company;

import java.io.*;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Compress {

    static String code = "";
    static Node root;
    final static int pseudoEOF = -1;
    static String compressionCode = "";

    //create a hashmap that stores each character's ascii code and its frequency in the file
    static HashMap<Integer, Integer> frequency = new HashMap<>();

    //create a hashmap that stores each character's ascii code and its huffman code
    static HashMap<Integer, String> codes = new HashMap<>();

    //create a priority queue that stores all nodes sorted in ascending frequency order
    static PriorityQueue<Node> priorityQueue = new PriorityQueue<>();


    //this method takes in a filename as input and compresses the file
    public static void compressFile(String sourceFilename, String destFilename) {
        //calculate frequency of each character in input file and add it to hashmap
        populateFrequencyHashmap(sourceFilename);

        //add all nodes into priority queue
        populatePriorityQueue();

        //create the huffman tree and print it
        root = createHuffmanTree();
        printTree(root);
        traverseInOrder(root);
        compress(sourceFilename, destFilename);
    }

    //this method takes in a file name
    //reads the file character by character and populates the frequency hashmap and the priority queue
    public static void populateFrequencyHashmap(String filename) {
        try {
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int c;

            //read from file character by character, incrementing each character's frequency when it is encountered
            while((c = bufferedReader.read()) != -1)
            {
                System.out.println((char)c);

                if(frequency.get(c) == null)
                    frequency.put(c, 1);
                else
                    frequency.put(c, frequency.get(c)+1);
            }
            frequency.put(pseudoEOF, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("frequency of each character: ");

        for(HashMap.Entry entry : frequency.entrySet())
        {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    //this method adds a node for each character into the priority queue
    private static void populatePriorityQueue() {
        //add nodes from hashmap into priority queue
        frequency.forEach((key, value) -> {
            Node node = new Node((int) key, (int) value);
            priorityQueue.add(node);
        });
    }

    //this method creates a huffman tree and returns the root node of the tree
    public static Node createHuffmanTree() {
        int size = priorityQueue.size();
        Node x, y;

        System.out.println("Printing inside createHuffmanTree: ");
        for(int i=1; i<size; i++)
        {
            Node node = new Node();
            x = priorityQueue.poll();
            y = priorityQueue.poll();
            node.left = x;
            node.right = y;
            System.out.println((char)x.character +": " + x.freq + "\t" + (char)y.character +": " + y.freq);
            node.freq = x.freq + y.freq;
            priorityQueue.add(node);
        }
        return priorityQueue.poll();
    }

    //this method does the actual compression of the input file
    public static void compress(String sourceFilename, String destFilename) {
        int c;

        File inputFile = new File(sourceFilename);
        File compressedFile = new File(destFilename);

        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            FileOutputStream outputStream = new FileOutputStream(compressedFile, true);
            FileWriter fileWriter = new FileWriter(compressedFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            writeFileHeader(bufferedWriter);

            while ((c = bufferedReader.read()) != -1) {
                compressionCode = compressionCode + codes.get(c);
            }

            compressionCode += codes.get(-1);

            for(int i=1; i<=compressionCode.length(); i++)
            {
                System.out.print(compressionCode.charAt(i-1));
                if(i%7==0)
                    System.out.println();
            }

            System.out.println("compression code new");
            System.out.println(compressionCode);

            String bits = "";
            String currentByte = "";
            byte ascii;
            int j=0;

            for(int i=1; i<=compressionCode.length(); i++)
            {
                bits+=compressionCode.charAt(i-1);
                if(i%7==0)
                {
                    j=i;
                    currentByte = "0" + bits;
                    ascii = convertBitsToASCII(currentByte);
                    outputStream.write(ascii);
                    bits = "";
                }
            }

            String leftoverBits = compressionCode.substring(j);


            System.out.println("LEFTOVER BITS");
            System.out.println(leftoverBits);

            currentByte = "0" + leftoverBits;
            currentByte = String.format("%-8s", currentByte).replace(' ', '0');
            ascii = convertBitsToASCII(currentByte);
            outputStream.write(ascii);

            outputStream.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static byte convertBitsToASCII(String currentByte) {

        byte ascii = 0;
        for(int i=0; i<currentByte.length(); i++)
        {
            if(currentByte.charAt(i)=='1')
                ascii += 1 * Math.pow(2, 7-i);

        }

        System.out.println("ascii character:");
        System.out.println(ascii);
        return ascii;

    }


    //writes our code table to the beginning of our compressed file
    private static void writeFileHeader(BufferedWriter bufferedWriter) {
        try {
            //write to compressed file the number of character in our hashmap
            bufferedWriter.append(String.valueOf(codes.size())).append("\n");

            System.out.println("code for each character");
            //write down each character and its code into the compressed file
            codes.forEach((key, value) -> {
                try {
                    bufferedWriter.append(String.valueOf(key)).append(": ");
                    bufferedWriter.append(value).append("\n");
                    System.out.print(key +": ");
                    System.out.println(value);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //populates the codes hashmap that stores each character and its huffman code
    public static void traverseInOrder (Node rootNode){
        //if this node is a leaf node, add the character and its corresponding code to the hashmap
        if(rootNode.getLeft() == null && rootNode.getRight() == null){
            System.out.println((char)rootNode.character + ": " + code);
            codes.put(rootNode.character, code);
//            System.out.println("hm: "+ codes.get(rootNode.character));
        }
        else{
            code+="0";
            traverseInOrder(rootNode.getLeft());
            code = code.substring(0,code.length()-1);
            code+="1";
            //visit
            traverseInOrder(rootNode.getRight());
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
