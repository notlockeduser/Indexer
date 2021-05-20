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

    static void searchIndex(String line, HashMap<String, ArrayList<String>> dictionary) {
        line = line.replaceAll("[^A-Za-z0-9']", " ")
                .toLowerCase();
        String[] tokens = line.split("\\s*(\\s|-)\\s*");

        ArrayList<String> array = null, arrayToken = null;
        ArrayList<String> arrayTemp = new ArrayList<String>();

        array = dictionary.get(tokens[0]);

        for (String token : tokens) {
            if (dictionary.containsKey(token)) {
                arrayToken = dictionary.get(token);
                for (String path : arrayToken)
                    if (array.contains(path))
                        arrayTemp.add(path);

                array.clear();
                array.addAll(arrayTemp);
                arrayTemp.clear();
            }
        }
        for (String path : array)
            System.out.println(path);
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

                if (arrayFiles != null)
                    for (File file : arrayFiles) {
                        String path = dir.getParent() + "\\" + dir.getName() + "\\" + file.getName();

                        int nameInt = Integer.parseInt(file.getName()
                                .replaceAll("_+\\d+.txt", ""));

                        if (nameInt >= startIndex[j] && nameInt < endIndex[j]) {

                            try (BufferedReader bufReader = new BufferedReader(new FileReader(file))) {
                                String line;
                                while ((line = bufReader.readLine()) != null) {
                                    line = line.replaceAll("<br /><br />", " ")
                                            .replaceAll("[^A-Za-z0-9']", " ")
                                            .toLowerCase();

                                    String[] tokens = line.split("\\s*(\\s|-)\\s*");

                                    for (String token : tokens) {
                                        dictionary.putIfAbsent(token, new ArrayList<String>());
                                        if (!dictionary.get(token).contains(path))
                                            dictionary.get(token).add(path);
                                    }
                                }
                            } catch (IOException E) {
                                System.out.println("File read error");
                            }
                        }
                    }
            }
        }

        searchIndex("The", dictionary);

        System.out.println("debugger"); // check dictionary by debugger
    }
}