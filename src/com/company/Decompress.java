package com.company;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;

public class Decompress {

    static String decompressionCode = "";
    private static Node root = new Node();
    private static HashMap<Integer, String> codes = new HashMap<>();

    //this method takes in a filename and decompresses the file
    public static void decompressFile(String sourceFilename, ArrayList<String> destFilename) {
        String line;
        String[] tokens;

        File inputFile = new File(sourceFilename);

        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //read in the number of entries of the huffman table
            int n = Integer.parseInt(bufferedReader.readLine());
            System.out.println("n = " + n);

            if(n==1)
                return;
            //read in each entry of the huffman table and add the character and its code into a hashmap
            for(int i=0; i<n; i++)
            {
                line = bufferedReader.readLine();
                tokens = line.split(": ");

                codes.put(Integer.parseInt(tokens[0]), tokens[1]);
            }

            //build the huffman tree
            buildHuffmanTree();

            //convert the compressed file contents into a binary string
            String compressedFileBinary = readCompressedFileContents(bufferedReader);

            //decompress the file contents
            decompress(destFilename, compressedFileBinary);

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void decompress(ArrayList<String> destFilename, String compressedFileBinary) {
        Node tempNode = root;

        int j = 0;

        try {
            FileWriter fileWriter;
            BufferedWriter bufferedWriter;
            File file = new File(destFilename.get(0));

            if(file.isDirectory()){
                System.out.println("DIR");
                fileWriter = new FileWriter(new File(file.getAbsolutePath()+"/decomp_"+j));
            } else {
                System.out.println("FILE");
                fileWriter = new FileWriter(file);
            }
            bufferedWriter = new BufferedWriter(fileWriter);

            for (int i = 0; i < compressedFileBinary.length(); i++) {
                if (compressedFileBinary.charAt(i) == '0') {
                    tempNode = tempNode.left;
                } else {
                    tempNode = tempNode.right;
                }

                if (tempNode.right == null && tempNode.left == null) {
                    if(tempNode.character == -1)
                    {
                        j++;
                        System.out.println("NEW CAT");

                       // if(j==destFilename.size()) break;
                        bufferedWriter.close();
                        fileWriter = new FileWriter(new File(file.getAbsolutePath()+"/decomp_"+j));
//                        System.out.println("NEW CAT");
                        bufferedWriter = new BufferedWriter(fileWriter);
                        tempNode = root;
                        continue;
                    }
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
    private static String readCompressedFileContents(BufferedReader bufferedReader) {
        int c;

        String compressedFileBinary = "";
        System.out.println("CHECK THIS");
        try {
            while ((c = bufferedReader.read()) != -1)
            {
                System.out.println("TESTINGG");
                char x = (char) c;
                System.out.println(x);
                System.out.println(Integer.toBinaryString(x));
                System.out.println(c);
                System.out.println((char)c);
                System.out.println(Integer.valueOf((char)c));

                compressedFileBinary += String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
                System.out.println(Integer.toBinaryString(c));
            }


        }catch(Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("decomppp");
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
                    {
                        tempNode.left = new Node();
                    }
                    tempNode = tempNode.left;

                }
                else {
                    //if there is no right node, create one
                    if (tempNode.right == null)
                    {
                    tempNode.right = new Node();
                    }
                    tempNode = tempNode.right;
                }

                //if this is the last bit of the code, print leaf node's character
                if(i == value.length()-1)
                {
                    tempNode.character = (int) entry.getKey();
//                    System.out.println("char: "+(char)tempNode.character);
                }
            }
        }
    }
}
