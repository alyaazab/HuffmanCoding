package com.company;

public class Value implements Comparable<Value>{
    private Integer character;
    private String code;
    private Integer codeLength;

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

    @Override
    public int compareTo(Value o) {
        if(!this.codeLength.equals(o.codeLength)){
            return this.codeLength.compareTo(o.codeLength);
        }
        return this.character .compareTo(o.character);
    }
}
