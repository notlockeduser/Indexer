import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static ArrayList<String> arrayStopWords = new ArrayList<String>();
    private static ArrayList<File> arrayFiles = new ArrayList<File>();
    private static HashMap<String, ArrayList<String>> dictionary = new HashMap<>();
    private static int numberThreads = 5;

    private static void searchIndex(String line) {
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

    private static void loadStopWords(File file) {
        try (BufferedReader bufReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufReader.readLine()) != null) {
                arrayStopWords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listFilesForFolder(File folder) {
        for (File file : folder.listFiles())
            if (file.isDirectory())
                listFilesForFolder(file);
            else
                arrayFiles.add(file);
    }

    private static void doIndex(File file) {
        // the path of the given file
        String path = file.getParent() + "\\" + file.getName();
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

    private static void parallelSharing() {
        Thread[] threads = new Thread[5];
        int size = arrayFiles.size();

        for (int i = 0; i < numberThreads; i++) {
            int startIndex = size / numberThreads * i;
            int endIndex = size / numberThreads * (i + 1);

            threads[i] = new Thread(() -> {
                for (int j = startIndex; j < endIndex; j++) {
                    doIndex(arrayFiles.get(j));
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads)
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
    }

    public static void main(String[] args) throws IOException {
        // input data
        final File fileStopWords = new File("stop-words.txt");
        final File folder = new File("aclImdb");

        // method for loading stop words from a special file
        loadStopWords(fileStopWords);
        // open the root folder and recursively go through it collecting all the files
        listFilesForFolder(folder);
        // splitting a data array into streams and their subsequent indexing
        parallelSharing();

        // test method to check
        searchIndex("last first man");

        // check dictionary by debugger
        System.out.println("debugger");
    }
}