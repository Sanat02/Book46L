package kg.attractor.java;
import kg.attractor.java.lesson44.Lesson46Server;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new Lesson46Server("localhost", 8187).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
