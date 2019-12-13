package com.company;

public class Value {
    private int character;
    private String code;
    private int codeLength;

    public Value(int character, String code, int codeLength) {
        this.character = character;
        this.code = code;
        this.codeLength = codeLength;
    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }
}
