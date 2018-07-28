package com.spockatone.spock.entity;

public enum Status {
    WAITING("W"), ACTIVE("A"), FINISHED("F");

    private final String id;

    Status(String id) {
        this.id = id;
    }

    public static Status getById(String id) {
        for(Status status : values()){
            if (status.id.equalsIgnoreCase(id)){
                return status;
            }
        }
        throw new IllegalArgumentException("No status with id " + id + " found");
    }
}