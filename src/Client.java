import java.io.*;
import java.net.Socket;
import java.util.Date;

public class Client {
    public static String HOST;
    public static int PORT;
    public static final String configClientPath = System.getProperty("user.dir") + "\\assets\\ClientConfig.txt";

    private static Socket clientSocket;
    // streams for reading from the console, input and output from the socket
    private static BufferedReader console;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static String dateConnection;

    public static void main(String[] args) {
        try {
            try {
                getConfig(configClientPath);
                // ask the server for connection access
                clientSocket = new Socket(HOST, PORT);
                console = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                // send the date for "registration"
                dateConnection = new Date().toString();
                send(dateConnection);
                System.out.println(in.readLine());

                // send request to the server and receive a response
                String request = null;
                while (true) {
                    System.out.println("\n\nEnter your request");
                    request = console.readLine();
                    while (request.isEmpty()) {
                        System.out.println("Repeat your request");
                        request = console.readLine();
                    }

                    send(request);
                    System.out.println("\nServer response");
                    int size = Integer.parseInt(in.readLine());
                    for (int i = 0; i < size; i++)
                        System.out.println(in.readLine());
                }
            } finally {
                // in any case, you need to close the socket and streams
                System.out.println("The client has been closed");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getConfig(String configServerPath){
        try (BufferedReader bufReader = new BufferedReader(new FileReader(configServerPath))) {
            String line = bufReader.readLine();
            String[] pairPORT = line.trim().split("=");
            PORT = Integer.parseInt(pairPORT[1]);
            line = bufReader.readLine();
            String[] pairHOST = line.trim().split("=");
            HOST = pairHOST[1];
        } catch (IOException E) {
            System.out.println("ServerConfig.txt read error");
        }
    }

    private static void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}