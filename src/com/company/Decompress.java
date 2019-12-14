package com.company;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Decompress {


    private static Node root = new Node();
    private static HashMap<Integer, String> codes = new HashMap<>();

    //this method takes in a filename and decompresses the file
    public static void decompressFile(String sourceFilename, String destFilename) {
        String line;
        String[] tokens;

        File inputFile = new File(sourceFilename);
        ;
        File decompressedFile = new File(destFilename);

        try {
            Scanner scanner = new Scanner(inputFile);

            FileWriter fileWriter = new FileWriter(decompressedFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //read in the number of entries of the huffman table
            int n = scanner.nextInt();
            scanner.nextLine();

            //read in each entry of the huffman table and add the character and its code into a hashmap
            for(int i=0; i<n; i++)
            {
                line = scanner.nextLine();
                tokens = line.split(": ");

                codes.put(Integer.parseInt(tokens[0]), tokens[1]);
            }

            //build the huffman tree
            buildHuffmanTree();

            //convert the compressed file contents into a binary string
            String compressedFileBinary = readCompressedFileContents(scanner);

            //decompress the file contents
            decompress(bufferedWriter, compressedFileBinary);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //this method does the actual decompression
    private static void decompress(BufferedWriter bufferedWriter, String compressedFileBinary) {
        //go through the code and decompress
        Node tempNode = root;

        try {
            for (int i = 0; i < compressedFileBinary.length(); i++) {
                if (compressedFileBinary.charAt(i) == '0') {
                    tempNode = tempNode.left;
                } else {
                    tempNode = tempNode.right;
                }

                if (tempNode.right == null && tempNode.left == null) {
                    System.out.print((char) tempNode.character);
                    bufferedWriter.append((char) tempNode.character);
                    tempNode = root;
                }
            }

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //helper method that reads compressed file contents and converts them to a binary string
    private static String readCompressedFileContents(Scanner scanner) {
        //holds compressed content
        String compressedFileText = "";

        while(scanner.hasNext())
        {
            compressedFileText = scanner.next();
            System.out.println(compressedFileText);
        }

        System.out.println(compressedFileText.length());

        //holds compressed content as binary string
        String compressedFileBinary ="";

        //convert each character to its ascii code and remove the first bit
        for(int i=0; i<compressedFileText.length(); i++)
            compressedFileBinary += String.format("%7s", Integer.toBinaryString(compressedFileText.charAt(i))).replace(' ', '0');

        System.out.println(compressedFileBinary);

        return compressedFileBinary;

    }


    //build huffman tree from the huffman code table in the file header
    public static void buildHuffmanTree() {
        Node tempNode;
        String value;

        //for each entry in the hashmap
        for(HashMap.Entry entry : codes.entrySet())
        {
            value = entry.getValue().toString();
            tempNode = root;

            //for each bit of the character's huffman code
            for(int i=0; i<value.length(); i++)
            {
                //if the bit is a 0, traverse left
                if(value.charAt(i)=='0')
                {
                    //if there is no left node, create one
                    if(tempNode.left == null)
                        tempNode.left = new Node();
                    tempNode = tempNode.left;

                }
                else
                {
                    //if there is no right node, create one
                    if(tempNode.right == null)
                        tempNode.right = new Node();
                    tempNode = tempNode.right;
                }

                //if this is the last bit of the code, print leaf node's character
                if(i == value.length()-1)
                {
                    tempNode.character = (int) entry.getKey();
                    System.out.println("char: "+tempNode.character);
                }
            }
        }

//        Compress.traverseInOrder(root);
    }


}
