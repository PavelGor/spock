package com.spockatone.spock.entity;

public enum Type {
    SUCCESS("S"), FAILED("F"), WIN("W"), LOSE("L");

    private final String id;

    Type(String id) {
        this.id = id;
    }

    public static Type getById(String id) {
        for(Type type : values()){
            if (type.id.equalsIgnoreCase(id)){
                return type;
            }
        }
        throw new IllegalArgumentException("No type with id " + id + " found");
    }



    @Override
    public String toString() {
        return Type.this.toString();
    }
}