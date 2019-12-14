package com.company;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Decompress {


    private static Node root = new Node();
    private static HashMap<Integer, String> codes = new HashMap<>();

    public static void decompressFile(String filename) {

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            int n = scanner.nextInt();
            System.out.println("n: " + n);
            scanner.nextLine();

            String line;
            String[] tokens;
            for(int i=0; i<n; i++)
            {
                line = scanner.nextLine();
                System.out.println("line: " + line);

                tokens = line.split(": ");

                System.out.println(tokens[0]);
                System.out.println(tokens[1]);

                codes.put(Integer.parseInt(tokens[0]), tokens[1]);
            }

            buildHuffmanTree();


            String decompressionText="";
            System.out.println("hello");
            while(scanner.hasNext())
            {
                System.out.println("hellooooo");
                decompressionText = scanner.next();
                System.out.println(decompressionText);
            }

            System.out.println(decompressionText.length());
            String decompressionCode ="";
            for(int i=0; i<decompressionText.length(); i++)
            {
                decompressionCode += String.format("%7s", Integer.toBinaryString(decompressionText.charAt(i))).replace(' ', '0');
//                decompressionCode += Integer.toBinaryString((int)decompressionText.charAt(i));
                System.out.println(String.format("%7s", Integer.toBinaryString(decompressionText.charAt(i))).replace(' ', '0'));
            }

            System.out.println(decompressionCode);

            Node tempNode = root;

            for(int i=0; i<decompressionCode.length(); i++)
            {
                if(decompressionCode.charAt(i)=='0')
                {
                    tempNode = tempNode.left;
                } else {
                    tempNode = tempNode.right;
                }

                if(tempNode.right==null && tempNode.left==null)
                {
                    System.out.print((char)tempNode.character);
                    tempNode = root;
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }






    }

    public static void buildHuffmanTree() {
        Node tempNode;
        Node newNode;

        String value;

        for(HashMap.Entry entry : codes.entrySet())
        {
            value = entry.getValue().toString();
            System.out.println("val: " + value);
            tempNode = root;

            for(int i=0; i<value.length(); i++)
            {
                System.out.println("i: "+i);

                if(value.charAt(i)=='0')
                {
                    System.out.println("value is 0");
                    if(tempNode.left == null)
                    {
                        System.out.println("my left is null, creating new left node");
                        newNode = new Node();
                        tempNode.left = newNode;
                    }
                    tempNode = tempNode.left;

                }
                else
                {
                    if(tempNode.right == null)
                    {
                        System.out.println("my right is null, creating new right node");
                        newNode = new Node();
                        tempNode.right = newNode;
                    }
                    tempNode = tempNode.right;
                }


                if(i==value.length()-1)
                {
                    tempNode.character = (int) entry.getKey();
                    System.out.println("char: "+tempNode.character);
                }
            }
        }

        FileUtils.traverseInOrder(root);
    }


}
