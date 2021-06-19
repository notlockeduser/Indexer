import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    public static final int PORT = 8080;
    public static final int nThreads = 5;
    public static final String rootPath = "C:\\Users\\Bogdan\\Documents\\GitHub\\Parallel-processing-Course-work\\";
    public static final String inputPath = rootPath + "input\\";
    public static final String stopWordsPath = rootPath + "assets\\stop-words.txt";

    // list of all sockets associated with the client
    public static LinkedList<ServerHelper> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        // create server
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running");
            System.out.println(System.getProperty("user.dir"));
            Indexer folder = new Indexer(nThreads, rootPath, inputPath, stopWordsPath);
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
}

