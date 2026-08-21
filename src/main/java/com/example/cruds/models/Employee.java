package com.example.cruds.models;


import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

class Node{
    int data;
    Node next;

    Node(int data,Node next){
        this.data = data;
        this.next = next;
    }

    Node(){

    }

}

public class Employee {
    public static void main(String[] args) {
        Node node = new Node(1,null);

        List<Integer> li = new ArrayList<>();

        li.add(1);
        li.add(2);

        for(int i=0;i<li.size();i++){
            int ele  = li.get(i);

            Node node1 = new Node(ele,null);
            node.next = node1;
        }
    }
}
