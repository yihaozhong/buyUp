package org.example;

public class main {
    public static void main(String[] args) {

        // 1. cat new cat
        Cat cat = new Cat();
        //self property
        System.out.println(cat.age);
        //self abstract class public property
        System.out.println(cat.name);

        cat.sleep();

        // 2. animal new cat
        Animal animal = new Cat(); // new Cat() is an instance

        animal.sleep(); // we can access the sleep(), but not age

        // abstract method can be access by abstract refer

        System.out.println(((Cat)animal).age);

        // 3. interface
        Climb climb = new Cat(); // a cat instance that has Climb intervace scope
        System.out.println((climb.climbTree()));

        Swim swim = new Cat();
        System.out.println(swim.swim());
    }
}
