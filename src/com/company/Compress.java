package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Compress {

    static String code = "";
    static Node root;
    final static int pseudoEOF = -1;
    static StringBuilder compressionCode = new StringBuilder();

    //create a hashmap that stores each character's ascii code and its frequency in the file
    static HashMap<Integer, Integer> frequency = new HashMap<>();

    //create a hashmap that stores each character's ascii code and its huffman code
    static HashMap<Integer, String> codes = new HashMap<>();

    //create a priority queue that stores all nodes sorted in ascending frequency order
    static PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

    static int nOfFiles;


    //this method takes in a filename as input and compresses the file
    public static void compressFile(ArrayList<String> sourceFilenames, String destFilename) {

        ArrayList<File> sourceFiles = new ArrayList<>();

        for(int i=0; i<sourceFilenames.size(); i++)
        {
            sourceFiles.add(new File(sourceFilenames.get(i)));

        }
//        File sourceFile = new File(sourceFilename);
        File destFile = new File(destFilename);

        //calculate frequency of each character in input file and add it to hashmap
        populateFrequencyHashmap(sourceFiles);

        //add all nodes into priority queue
        populatePriorityQueue();

        //create the huffman tree and print it
        root = createHuffmanTree();
//        printTree(root);
        traverseInOrder(root);

        //compress the file
        compress(sourceFiles, destFile);

//        if(sourceFile.length() < destFile.length())
//        {
//            System.out.println("Compression using Huffman encoding will result in a larger file.");
//        }



    }

    //this method takes in a file name
    //reads the file character by character and populates the frequency hashmap and the priority queue
    public static void populateFrequencyHashmap(ArrayList<File> sourceFiles) {
        try{

            FileReader fileReader = null;
            BufferedReader bufferedReader;
            frequency.put(pseudoEOF, 0);

            for(int i=0; i<sourceFiles.size(); i++)
            {
                fileReader = new FileReader(sourceFiles.get(i));
                bufferedReader = new BufferedReader(fileReader);

                int c;

                //read from file character by character, incrementing each character's frequency when it is encountered
                while((c = bufferedReader.read()) != -1)
                {
//                    System.out.println((char)c);

                    if(frequency.get(c) == null)
                        frequency.put(c, 1);
                    else
                        frequency.put(c, frequency.get(c)+1);
                }

                frequency.put(pseudoEOF, frequency.get(-1)+1);

                bufferedReader.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println("frequency of each character: ");

//        for(HashMap.Entry entry : frequency.entrySet())
//        {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
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

//        System.out.println("Printing inside createHuffmanTree: ");
        for(int i=1; i<size; i++)
        {
            Node node = new Node();
            x = priorityQueue.poll();
            y = priorityQueue.poll();
            node.left = x;
            node.right = y;
//            System.out.println((char)x.character +": " + x.freq + "\t" + (char)y.character +": " + y.freq);
            node.freq = x.freq + y.freq;
            priorityQueue.add(node);
        }
        return priorityQueue.poll();
    }

    //this method does the actual compression of the input file
    public static void compress(ArrayList<File> inputFile, File compressedFile) {
        int c;

        try {

            FileWriter fileWriter = new FileWriter(compressedFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //write huffman table to file header
            writeFileHeader(bufferedWriter);

            for(int i = 0; i < inputFile.size(); i++) {
                FileReader fileReader = new FileReader(inputFile.get(i));
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                //read from input file and save the binary string into compressionCode
                while ((c = bufferedReader.read()) != -1) {
//                    System.out.println(c);
//                    System.out.println(codes.get(c));
                    compressionCode.append(codes.get(c));
//                    System.out.println(compressionCode.toString());
//                    compressionCode = compressionCode + codes.get(c);
                }

                //add the pseudo-eof's binary equivalent
                compressionCode.append(codes.get(-1));
//                compressionCode += codes.get(-1);

            }

//            System.out.println("Compression Code: ");
//            System.out.println(compressionCode);

            String bits = "";
            String currentByte;
            int character;
            int j=0;

            //convert every 8 bits to their decimal equivalent and write to compressed file
            for(int i=1; i<=compressionCode.length(); i++)
            {
                bits+=compressionCode.charAt(i-1);
                if(i%8==0)
                {
                    j=i;
//                    System.out.println("bits: " + bits);
                    character = convertBitsToDecimal(bits);
                    bufferedWriter.append((char)character);
                    bits = "";
                }
            }

            //right pad the leftover bits with 0
            String leftoverBits = compressionCode.substring(j);
            currentByte = String.format("%-8s", leftoverBits).replace(' ', '0');

            //convert to decimal equivalent and write to compressed file
            character = convertBitsToDecimal(currentByte);
            bufferedWriter.append((char)character);

            bufferedWriter.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static int convertBitsToDecimal(String currentByte) {

        int ascii = 0;
        for(int i=0; i<currentByte.length(); i++)
        {
            if(currentByte.charAt(i)=='1')
                ascii += 1 * Math.pow(2, 7-i);

        }

        return ascii;

    }


    //writes our code table to the beginning of our compressed file
    private static void writeFileHeader(BufferedWriter bufferedWriter) {
        try {
            //write to compressed file the number of character in our hashmap
            bufferedWriter.append(String.valueOf(codes.size())).append("\n");
            bufferedWriter.append(String.valueOf(nOfFiles)).append("\n");

            System.out.println("code for each character");
            System.out.println("Byte \t\t Code \t\t New Code");
            //write down each character and its code into the compressed file
            codes.forEach((key, value) -> {
                try {
                    int k = key;
                    char c = (char)k;
                    bufferedWriter.append(String.valueOf(key)).append(": ");
                    bufferedWriter.append(value).append("\n");
                    System.out.println(key + " \t\t "+Integer.toBinaryString(k) + " \t\t " + value);
                    //System.out.println(value);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //populates the codes hashmap that stores each character and its huffman code
    public static void traverseInOrder (Node rootNode){
        //if this node is a leaf node, add the character and its corresponding code to the hashmap
        if(rootNode.getLeft() == null && rootNode.getRight() == null){
//            System.out.println((char)rootNode.character + ": " + code);
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
