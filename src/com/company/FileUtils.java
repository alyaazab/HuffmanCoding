package com.company;

import java.io.*;
import java.nio.Buffer;
import java.util.*;

public class FileUtils {

    static String code = "";
    static Node root;

    //holds huffman code of all characters from input file
    static String fileCode = "";

    //create a hashmap that stores each character's ascii code and its frequency in the file
    static HashMap<Integer, Integer> frequency = new HashMap<>();

    static HashMap<Integer, Value> codes = new HashMap<>();

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
        traverseInOrder(root);
        compressFile(filename);
        decompressFile("compressed.txt");
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

    public static void compressFile(String filename) {

        String compressionCode = "";
        int c;
        String currentByte;
        byte outputByte;

        try {
            File inputFile = new File(filename);
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);


            File compressedFile = new File("compressed.txt");
            OutputStream outputStream = new FileOutputStream(compressedFile);

            for(int i=0; i<128; i++)
            {
                if(codes.get(i) == null)
                    outputStream.write(0);
                else
                    outputStream.write(codes.get(i).getCodeLength());

                outputStream.write(44);
            }

            //read from input file character by character
            while((c = bufferedReader.read()) != -1)
            {
                //for testing purposes: holds entire code to be written to file
                compressionCode = compressionCode + codes.get(c).getCode();

                //concatenate current code to fileCode
                fileCode+= codes.get(c).getCode();

                //if we have reached a length of 7
                if(fileCode.length()>=7)
                {
                    //extract the first 7 bits and add a 0 at the beginning
                    currentByte = "0" + fileCode.substring(0,7);
                    System.out.println(currentByte);

                    //convert string code to a byte
                    outputByte = Byte.parseByte(currentByte, 2);
                    System.out.println(outputByte);

                    //write this byte to output file
                    outputStream.write(outputByte);

                    //delete the first 7 bits from fileCode
                    fileCode = fileCode.substring(7);
                }
            }

            //if there are any remaining bits, write them to file
            if(fileCode.length()>0)
            {
                outputByte = Byte.parseByte(fileCode, 2);
                System.out.println(outputByte);
                outputStream.write(outputByte);
            }
            outputStream.close();

            System.out.println(compressionCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void writeFileHeader() {


    }


    public static void generateCodebook(){

    }


    public static void decompressFile(String filename) {

        //holds all bits from input file
        String decompressionCode = "";
        String currentByte;

        try {
            File inputFile = new File(filename);
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            System.out.println("Decompressing...");

            int c;
            while((c=bufferedReader.read()) != -1)
            {
                //print ascii value of this byte
                System.out.println(c);
                //get the least significant 7 bits of this byte
                currentByte = String.format("%7s", Integer.toBinaryString(c)).replace(' ', '0');
                System.out.println(currentByte);
                //add these 7 bits to our code string
                decompressionCode = decompressionCode + currentByte;
            }

            System.out.println(decompressionCode);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }





    public static void traverseInOrder (Node rootNode){
        if(rootNode.getLeft() == null && rootNode.getRight() == null){
            System.out.println((char)rootNode.character + ": " + code);
            codes.put(rootNode.character, new Value(rootNode.character, code, code.length()));
            System.out.println("hm: "+ codes.get(rootNode.character).getCode());

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
