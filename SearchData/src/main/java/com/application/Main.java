package com.application;

import java.awt.*;
import java.util.*;
import java.util.List;

class Node {
  int first;
  int second;

  public Node() {}

  public Node(int first, int second) {
    this.first = first;
    this.second = second;
  }

  public int getFirst() {
    return first;
  }

  public void setFirst(int first) {
    this.first = first;
  }

  public int getSecond() {
    return second;
  }

  public void setSecond(int second) {
    this.second = second;
  }
}

/** @author PG */
public class Main {

  public static void compute(List<Node> nodList) {
    Collections.sort(
        nodList,
        new Comparator<Node>() {
          @Override
          public int compare(Node o1, Node o2) {
            return o1.first - o2.first; }});
    Set<Node> nodes = new HashSet<>();
    Node node = nodList.get(0);
    for (int i = 1; i < nodList.size(); i++) {
      if (nodList.get(i).first < node.second) {
        node.second = nodList.get(i).second; } else {
        nodes.add(node);
        node = new Node(nodList.get(i).first, nodList.get(i).second); } }
    nodes.add(node);
    Iterator<Node> iterator=nodes.iterator();
    System.out.print("[");
    while(iterator.hasNext()){
        Node node1=iterator.next();
        System.out.print("["+node1.first+","+node1.second+"]");
        if(iterator.hasNext())System.out.print(",");
    }
    System.out.print("]");
  }

  public static void main(String[] args) {}

  /*public static int compute(String value) {
    int[] num = new int[26];
    int all = 1;
    for (int i = 0; i < value.length(); i++) {
      num[value.charAt(i) - 'A']++;
      all = all * (i + 1);
    }
    int sum = 1;
    for (int i = 0; i < 26; i++) {
      int a = 1;
      for (int j = 1; j <= num[i]; j++) {
        a = a * j;
      }
      sum = sum * a;
    }
    return all / sum;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String value = scanner.nextLine();
    System.out.println(compute(value));
  }*/
}
