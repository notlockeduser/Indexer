import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    public static final int PORT = 8080;
    public static LinkedList<ServerHelper> serverList = new LinkedList<>(); // список всех сокетов связаных с клиентом

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // создание сервера
            System.out.println("Server is running");
            new ReadConsole().start(); // поток для чтения с консоли в сервере
            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    serverList.add(new ServerHelper(clientSocket)); // добавить новое соединенние в список
                } catch (IOException e) {
                    clientSocket.close();
                }
            }
        }
    }
}

