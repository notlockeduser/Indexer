import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    public static int PORT;
    public static int nThreads;
    //public static final String rootPath = System.getProperty("user.dir");
    public static final String rootPath = "C:\\Users\\Bogdan\\Documents\\GitHub\\Parallel-processing-Course-work";
    public static final String inputPath = rootPath + "\\input\\";
    public static final String stopWordsPath = rootPath + "\\assets\\StopWords.txt";
    public static final String configServerPath = rootPath + "\\assets\\ServerConfig.txt";

    // list of all sockets associated with the client
    public static LinkedList<ServerHelper> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        getConfig(configServerPath);
        // create server
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running");
            Indexer folder = new Indexer(nThreads, inputPath, stopWordsPath);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    serverList.add(new ServerHelper(clientSocket, folder));
                } catch (IOException e) {
                    System.out.println(e);
                    clientSocket.close();
                }
            }
        }
    }

    public static void getConfig(String configServerPath){
        try (BufferedReader bufReader = new BufferedReader(new FileReader(configServerPath))) {
            String line = bufReader.readLine();
            String[] pairPORT = line.trim().split("=");
            PORT = Integer.parseInt(pairPORT[1]);
            line = bufReader.readLine();
            String[] pairTreads = line.trim().split("=");
            nThreads = Integer.parseInt(pairTreads[1]);
        } catch (IOException E) {
            System.out.println("ServerConfig.txt read error");
        }
    }
}

