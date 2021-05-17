import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

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

        HashMap<String, ArrayList<String>> dictionary = new HashMap<>();

        filesIndex(N, V, startIndex, endIndex);


        for (int j = 0; j < directions.length; j++) {
            File dir = directions[j];
            if (dir.isDirectory()) {
                File[] arrayFiles = dir.listFiles();
                for (int i = 0; i < arrayFiles.length; i++) {

                    int nameInt = Integer
                            .parseInt(arrayFiles[i]
                                    .getName()
                                    .replaceAll("_+\\d+.txt", ""));

                    if (nameInt >= startIndex[j] && nameInt < endIndex[j]) {

                        try (BufferedReader bufReader = new BufferedReader(new FileReader(arrayFiles[i]))) {
                            String line;
                            while ((line = bufReader.readLine()) != null) {
                                line = line.replaceAll("<br /><br />", "");

                                String[] words = line.split("\\s*(\\s|,|!|_|\\.)\\s*");

                                for (String word : words) {
                                    System.out.println(word);
                                }
                            }
                        } catch (IOException exc) {
                            System.out.println("Ошибка чтения файла!");
                        }
                    }
                }
            }
        }
        System.out.println("debugger");
    }
}