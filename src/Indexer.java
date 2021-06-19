import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Indexer {
    private ArrayList<String> arrayStopWords = new ArrayList<String>();
    private ArrayList<File> arrayFiles = new ArrayList<File>();
    private ConcurrentHashMap<String, List<String>> dictionary = new ConcurrentHashMap<>();
    private int numberThreads;

    public Indexer(int n, String inputPath, String stopWordsPath) {
        // time
        double startTime, finalTime;
        startTime = System.nanoTime();
        // input data
        this.numberThreads = n;
        final File fileStopWords = new File(stopWordsPath);
        final File folder = new File(inputPath);

        loadStopWords(fileStopWords);
        listFilesForFolder(folder);
        // splitting a data array into streams and their subsequent indexing
        parallelSharing();
        // time of work
        finalTime = (System.nanoTime() - startTime) / 1000000;
        System.out.println("Folder indexing time " + finalTime + " ms");
    }

    private void loadStopWords(File file) {
        try (BufferedReader bufReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufReader.readLine()) != null) {
                arrayStopWords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listFilesForFolder(File folder) {
        for (File file : folder.listFiles())
            if (file.isDirectory()) {
                listFilesForFolder(file);
            } else {
                arrayFiles.add(file);
            }
    }

    private void parallelSharing() {
        Thread[] threads = new Thread[numberThreads];
        int size = arrayFiles.size();
        // Splitting files into streams
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
        // Waiting for threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void doIndex(File file) {
        String pathFile = file.getParent() + "\\" + file.getName();

        try (BufferedReader bufReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufReader.readLine()) != null) {
                // clearing unnecessary characters and break into lexemes
                String[] tokens = takeTokens(line);
                // fill the dictionary without repeat and remove the stop word
                for (String token : tokens) {
                    if (arrayStopWords.contains(token)) {
                        continue;
                    }
                    dictionary.putIfAbsent(token, Collections.synchronizedList(new ArrayList<String>()));
                    if (!dictionary.get(token).contains(pathFile)) {
                        dictionary.get(token).add(pathFile);
                    }
                }
            }
        } catch (IOException E) {
            System.out.println("File read error");
        }
    }

    private String[] takeTokens(String line){
        line =  line.replaceAll("<br /><br />", " ")
                .replaceAll("[^A-Za-z0-9']", " ")
                .toLowerCase();
        return line.split("\\s*(\\s)\\s*");
    }

    public List<String> searchIndex(String line) {
        // clearing unnecessary characters
        line = line.replaceAll("[^A-Za-z0-9']", " ")
                .toLowerCase();
        // break into lexemes
        String[] tokens = line.split("\\s*(\\s)\\s*");

        List<String> array = null;
        List<String> arrayToken = null;
        List<String> arrayTemp = new ArrayList<String>();

        // by combining, go through each token
        // and save only those files where they are repeated (and skip the stop word)
        // if not, then send an array with one value - no results
        boolean first = true;
        for (String token : tokens) {
            if (arrayStopWords.contains(token)) continue;
            if (first) {
                array = dictionary.get(token);
                first = false;
            }
            if (array == null) break;
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
        if (array == null || array.size() == 0) {
            array = new ArrayList<String>();
            array.add("No results");
        }
        // output
        return array;
    }
}