import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class Main {
    static void filesIndex(int[] N, int V, int[] startIndex, int[] endIndex) {
        for (int i = 0; i < N.length; i++) {
            startIndex[i] = N[i] / 50 * (V - 1);
            endIndex[i] = N[i] / 50 * V;
        }
    }

    public static void main(String[] args) {
        int V = 5;
        int[] startIndex = new int[5];
        int[] endIndex = new int[5];
        int[] N = {12500, 12500, 12500, 12500, 50000};
        File[] directions = {
                new File("aclImdb//test//neg"),
                new File("aclImdb//test//pos"),
                new File("aclImdb//train//neg"),
                new File("aclImdb//train//pos"),
                new File("aclImdb//train//unsup")
        };

        filesIndex(N, V, startIndex, endIndex);

    }
}