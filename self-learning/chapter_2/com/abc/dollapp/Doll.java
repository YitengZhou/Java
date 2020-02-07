package com.abc.dollapp;
/* 定义一个Doll类，有一个name属性和一个speak方法 */
public class Doll{
    /** name */
    private String name;
    /** method */
    public Doll(String name)
    {
        this.name=name; // set name
    }
    /** speak */
    public void speak()
    {
        System.out.println(name); //print name
    }
}