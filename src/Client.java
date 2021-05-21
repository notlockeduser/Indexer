import java.io.*;
import java.net.Socket;
import java.util.Date;

public class Client {

    private static Socket clientSocket; // сокет для общения
    private static BufferedReader reader; // поток для чтения с консоли
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    private static String date; // время подключения клиента

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 8080); // запрашиваем у сервера доступ на соединение
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                date = new Date().toString();
                send(date);
                System.out.println(in.readLine());

                String request;
                while (true) { // постоянно смотрим на входящие данные с сервера и если они есть, выводим
                    System.out.println("\nEnter your request");
                    request = reader.readLine();
                    if (request != null) {
                        send(request);
                        System.out.println("server response");
                        int size = Integer.parseInt(in.readLine());
                        for (int i = 0; i < size; i++)
                            System.out.println(in.readLine());
                    }
                }
            } finally { // в любом случае необходимо закрыть сокет и потоки
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
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