import java.io.*;
import java.net.Socket;

class ServerHelper extends Thread {

    private Socket socket; // сокет, через который сервер общается с клиентом
    private BufferedReader reader; // поток для чтения с консоли
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток записи в сокет
    private String date; // время и дата присоеденинения
    public String nickname; // никнейм подключенного клиента

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
            nickname = in.readLine();
            System.out.println("new Client - " + nickname + " - " + date);
            send("new client is registered");
            send("\n --- Wait message from server --- \n");

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