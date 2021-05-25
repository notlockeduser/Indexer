import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    public static final int PORT = 8080;
    public static final int nThreads = 5;
    public static final String rootPath = "C:\\Users\\Bogdan\\Documents\\GitHub\\Parallel-processing-Course-work\\";

    // list of all sockets associated with the client
    public static LinkedList<ServerHelper> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        // create server
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running");
            Indexer folder = new Indexer(nThreads, rootPath);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    // add a new connection to the list
                    serverList.add(new ServerHelper(clientSocket, folder));
                } catch (IOException e) {
                    System.out.println(e);
                    clientSocket.close();
                }
            }
        }
    }
}

