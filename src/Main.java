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
        // clearing unnecessary characters
        line = line.replaceAll("[^A-Za-z0-9']", " ")
                .toLowerCase();
        // break into lexemes
        String[] tokens = line.split("\\s*(\\s)\\s*");

        ArrayList<String> array = null, arrayToken = null;
        ArrayList<String> arrayTemp = new ArrayList<String>();

        // by combining, go through each token and save only those files where they are repeated
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
        // output
        for (String path : array)
            System.out.println(path);
    }

    public static void main(String[] args) {
        // input data
        int V = 5;
        int[] N = {12500, 12500, 12500, 12500, 50000};
        int[] startIndex = new int[N.length];
        int[] endIndex = new int[N.length];
        File[] directions = {
                new File("aclImdb//test//neg"),
                new File("aclImdb//test//pos"),
                new File("aclImdb//train//neg"),
                new File("aclImdb//train//pos"),
                new File("aclImdb//train//unsup")
        };

        HashMap<String, ArrayList<String>> dictionary = new HashMap<>();

        // method for calculating the index for a variant
        filesIndex(N, V, startIndex, endIndex);

        // go through the folders from directions
        for (int i = 0; i < directions.length; i++) {
            File dir = directions[i];
            if (dir.isDirectory()) {
                // make an array of files stored in these directories
                File[] arrayFiles = dir.listFiles();

                if (arrayFiles != null)
                    for (File file : arrayFiles) {
                        // the path of the given file
                        String path = dir.getParent() + "\\" + dir.getName() + "\\" + file.getName();

                        // file name without extra characters (index)
                        int nameInt = Integer.parseInt(file.getName()
                                .replaceAll("_+\\d+.txt", ""));

                        // check the file if this file is suitable for us by index
                        if (nameInt >= startIndex[i] && nameInt < endIndex[i]) {
                            // read the file line by line
                            try (BufferedReader bufReader = new BufferedReader(new FileReader(file))) {
                                String line;
                                while ((line = bufReader.readLine()) != null) {
                                    // clearing unnecessary characters
                                    line = line.replaceAll("<br /><br />", " ")
                                            .replaceAll("[^A-Za-z0-9']", " ")
                                            .toLowerCase();
                                    // break into lexemes
                                    String[] tokens = line.split("\\s*(\\s)\\s*");

                                    // fill the dictionary without repeat
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

        // check dictionary by debugger
        System.out.println("debugger");
    }
}