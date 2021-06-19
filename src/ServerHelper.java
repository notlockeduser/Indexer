import java.io.*;
import java.net.Socket;
import java.util.List;

class ServerHelper extends Thread {

    // socket through which the server communicates with the client
    private Socket socket;
    // streams for reading from the console, input and output from the socket
    private BufferedReader console;
    private BufferedReader in;
    private BufferedWriter out;
    private String date;
    private Indexer folder;

    public ServerHelper(Socket socket, Indexer folder) throws IOException {
        this.socket = socket;
        this.folder = folder;
        console = new BufferedReader(new InputStreamReader(System.in));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        try {
            date = in.readLine();
            send("Connected to server");
            System.out.println("New client is connected - " + date);

            // constantly look at the incoming data from the server and if there is any, display
            String request;
            while (true) {
                request = in.readLine();
                if (request != null) {
                    System.out.println("Request - " + request);
                    searchRequest(request);
                }
            }

        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    // searching indexes on request and sending them to the client
    private void searchRequest(String request) {
        List<String> array = folder.searchIndex(request);
        if (array != null) {
            send(Integer.toString(array.size()));
        }
        for (String path : array) {
            send(path);
        }
    }

    // easy send to client
    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}