package com.company;

import java.util.Comparator;

public class Node implements Comparable {
    Node left;
    Node right;
    int freq;
    int character;

    public Node(){

    }

    public Node(int character, int freq) {
        this.character = character;
        this.freq = freq;
        this.right = null;
        this.left = null;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    @Override
    public int compareTo(Object o) {
        return this.freq - ((Node)(o)).freq;
    }
}
