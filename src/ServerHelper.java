import java.io.*;
import java.net.Socket;
import java.util.List;

class ServerHelper extends Thread {

    private Socket socket; // сокет, через который сервер общается с клиентом
    private BufferedReader reader; // поток для чтения с консоли
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток записи в сокет
    private String date; // время и дата присоеденинения
    private Indexer folder;

    private void searchRequest(String request) {
        List<String> array = folder.searchIndex(request);
        if (array != null)
            send(Integer.toString(array.size()));
            for (String path : array)
                send(path);
    }

    public ServerHelper(Socket socket, Indexer folder) throws IOException {
        this.socket = socket;
        this.folder = folder;
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
            while (true) { // постоянно смотрим на входящие данные с сервера и если они есть, выводим
                request = in.readLine();
                if (request != null) {
                    System.out.println("request - " + request);
                    searchRequest(request);
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