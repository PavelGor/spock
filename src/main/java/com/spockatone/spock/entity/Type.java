package com.spockatone.spock.entity;

public enum Type {
    SUCCESS("S"), FAILED("F"), WIN("W"), LOSE("L");

    private final String id;

    Type(String id) {
        this.id = id;
    }

    public static Type getById(String id) {
        for (Type type : values()) {
            if (type.id.equalsIgnoreCase(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No type with id " + id + " found");
    }


    @Override
    public String toString() {
        switch (this) {
            case SUCCESS:
                return "SUCCESS";
            case WIN:
                return "WIN";
            case LOSE:
                return "LOSE";
            case FAILED:
                return "FAILED";
            default:
                throw new IllegalArgumentException();
        }
    }
}