package org.example;

public class Cat extends Animal implements Climb, Swim{
    public String age;
    @Override
    public void sleep() {
        System.out.println("Cat love sleep");
    }


    @Override
    public String climbTree() {
        return "Cat can climb Tree";
    }

    @Override
    public String swim() {
        return "Cat can swim";
    }
}
