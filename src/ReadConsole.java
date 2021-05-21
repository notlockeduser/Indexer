import java.io.*;

class ReadConsole extends Thread {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() {

        String command;
        try {
            while (true) {
                command = reader.readLine();
                if (command.equals("take")) { // записуем список пользователей в файл для удобной обработки
                    try (FileWriter writer = new FileWriter("C:\\Users\\Bogdan\\Desktop\\users.txt", false)) {
                        for (ServerHelper user : Server.serverList) {
                            String text = user.nickname;
                            writer.write(text + "\n");
                        }
                        writer.flush();
                        System.out.println("File users.txt is already filled with data");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (command.equals("push")) {// после ручного отредактирования файла // отсылаем нужным пользователям сообщение
                    try {
                        File file = new File("C:\\Users\\Bogdan\\Desktop\\users.txt");
                        FileReader fr = new FileReader(file);
                        BufferedReader readerFile = new BufferedReader(fr);
                        String line = readerFile.readLine();
                        while (line != null) {
                            System.out.println(line);
                            for (ServerHelper user : Server.serverList) {
                                if (user.nickname.equals(line))
                                    user.send("hello " + line);
                            }
                            line = readerFile.readLine();
                        }
                        System.out.println("push completed");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
