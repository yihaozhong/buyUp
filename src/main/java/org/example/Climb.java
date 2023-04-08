package org.example;

public interface Climb {
    default String climbTree() {
        return "Animal can climb";
    };
}
