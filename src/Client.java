import java.io.*;
import java.net.Socket;
import java.util.Date;

public class Client {
    public static final String HOST = "localhost";
    public static final int PORT = 8080;

    // socket for communicating with the server
    private static Socket clientSocket;
    // streams for reading from the console, input and output from the socket
    private static BufferedReader console;
    private static BufferedReader in;
    private static BufferedWriter out;
    // time and date of connection
    private static String date;

    private static void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            try {
                // ask the server for connection access
                clientSocket = new Socket("localhost", 8080);
                console = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                // send the date for "registration"
                date = new Date().toString();
                send(date);
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
}