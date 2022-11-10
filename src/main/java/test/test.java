package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @description:
 * @time: 24/10/2022 0:43
 */
public class test {
    public static void main(String[] args) {
        List<String> User = new ArrayList<String>();
        User.add("a");
        User.add("b");
        User.add("c");
        User.add("d");
        User.add("e");
//使用Collections.shuffle实现乱序排序
        Collections.shuffle(User);
        System.out.println(User);
    }
}
