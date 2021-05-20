import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static ArrayList<String> arrayStopWords = new ArrayList<String>();

    static void searchIndex(String line, HashMap<String, ArrayList<String>> dictionary) {
        // clearing unnecessary characters
        line = line.replaceAll("[^A-Za-z0-9']", " ")
                .toLowerCase();
        // break into lexemes
        String[] tokens = line.split("\\s*(\\s)\\s*");

        ArrayList<String> array = null, arrayToken = null;
        ArrayList<String> arrayTemp = new ArrayList<String>();

        // by combining, go through each token and save only those files where they are repeated (and skip the stop word)
        array = dictionary.get(tokens[0]);
        for (String token : tokens) {
            if (arrayStopWords.contains(token)) continue;
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
        if (array != null)
            for (String path : array)
                System.out.println(path);
    }

    static void loadStopWords(File file) {
        try (BufferedReader bufReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufReader.readLine()) != null) {
                arrayStopWords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void listFilesForFolder(File folder, HashMap<String, ArrayList<String>> dictionary) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory())
                listFilesForFolder(file, dictionary);
            else
                indexer(folder, file, dictionary);
        }
    }

    private static void indexer(File folder, File file, HashMap<String, ArrayList<String>> dictionary) {
        // the path of the given file
        String path = folder.getParent() + "\\" + folder.getName() + "\\" + file.getName();
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
                // fill the dictionary without repeat and remove the stop word
                for (String token : tokens) {
                    if (arrayStopWords.contains(token)) continue;
                    dictionary.putIfAbsent(token, new ArrayList<String>());
                    if (!dictionary.get(token).contains(path))
                        dictionary.get(token).add(path);
                }
            }
        } catch (IOException E) {
            System.out.println("File read error");
        }
    }


    public static void main(String[] args) throws IOException {
        // input data
        final File fileStopWords = new File("stop-words.txt");
        final File folder = new File("aclImdb");

        HashMap<String, ArrayList<String>> dictionary = new HashMap<>();

        // method for loading stop words from a special file
        loadStopWords(fileStopWords);
        listFilesForFolder(folder, dictionary);

        searchIndex("last first man", dictionary);

        // check dictionary by debugger
        System.out.println("debugger");
    }
}