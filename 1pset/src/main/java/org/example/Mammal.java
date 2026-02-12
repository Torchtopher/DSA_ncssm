package org.example;

public class Mammal{
    private double weight;
    private int age;

    public Mammal( double w ){
        if( w < 0 ) w = 100;
        this.weight = w;
    }

    public int eat( int kg ){
        int consume = (int)(Math.random() * kg);
        weight += consume;
        return consume;
    }

    public boolean getOlder( int years ){
        age += years;
        if( age > 10 ) age = 10;
        return age == 10;
    }

    public int milk(){
        int m = (int) Math.floor(weight/4 );
        weight -= m;
        return m;
    }

    public double getWeight(){
        return this.weight;
    }
}