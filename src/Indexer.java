import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Indexer {
    private ArrayList<String> arrayStopWords;
    private ArrayList<File> arrayFiles;
    private ConcurrentHashMap<String, List<String>> dictionary;
    private int numberThreads = 5;

    Indexer() {
        this.dictionary = new ConcurrentHashMap<>();
        this.arrayFiles = new ArrayList<File>();
        this.arrayStopWords = new ArrayList<String>();
        // input data
        double startTime, finalTime, totalTime = 0;
        startTime = System.nanoTime();
        final File fileStopWords = new File("stop-words.txt");
        final File folder = new File("aclImdb");

        // method for loading stop words from a special file
        loadStopWords(fileStopWords);
        // open the root folder and recursively go through it collecting all the files
        listFilesForFolder(folder);
        // splitting a data array into streams and their subsequent indexing
        parallelSharing();

        // test method to check
        //searchIndex("last first man");
        //System.out.println("---");
        //searchIndex("freedom is");

        // time of work
        finalTime = (System.nanoTime() - startTime) / 1000000;
        System.out.println(finalTime);

        // for check debugger
        //System.out.println("debugger");
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
        return array;
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
            if (file.isDirectory())
                listFilesForFolder(file);
            else
                arrayFiles.add(file);
    }

    private void doIndex(File file) {
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
                    dictionary.putIfAbsent(token, Collections.synchronizedList(new ArrayList<String>()));
                    if (!dictionary.get(token).contains(path))
                        dictionary.get(token).add(path);
                }
            }
        } catch (IOException E) {
            System.out.println("File read error");
        }
    }

    private void parallelSharing() {
        Thread[] threads = new Thread[numberThreads];
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
}