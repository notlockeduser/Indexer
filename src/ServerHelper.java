import java.io.*;
import java.net.Socket;

class ServerHelper extends Thread {

    private Socket socket; // сокет, через который сервер общается с клиентом
    private BufferedReader reader; // поток для чтения с консоли
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток записи в сокет
    private String date; // время и дата присоеденинения

    private static String searchRequest(String request){
        return request + " processing...";
    }

    public ServerHelper(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(System.in));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start(); // вызываем run()
    }

    @Override
    public void run() {
        try {
            date = in.readLine();
            send("connected to server");
            System.out.println("new client is connected - " + date);

            String request;
            while (true){ // постоянно смотрим на входящие данные с сервера и если они есть, выводим
                request = in.readLine();
                if (request != null){
                    System.out.println("request - " + request);
                    send(searchRequest(request));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void send(String msg) { // метод для удобной отправки сообщений
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}