# Parallel-processing-Course-work (Indexer)

- [What is it?](#What-is-it?)
- [Installation](#Installation)
- [Launching](#Launching)
- [Configuring](#Configuring)

# What is it?

The task of this course work is to develop programs for building an inverted index, as well as - to use the index.

- During construction, it was possible to speed up the solution time by varying the
  number of threads.
- When constructing and using an index, a data structure with parallel access is used,
  while accessing it with several threads.
- It is possible to access this structure from different processes using network
  sockets (ie Client-Server application).

# Installation
Clone repository
```sh
git clone https://github.com/notlockeduser/Parallel-processing-Course-work.git
```
 - Place the unpacked input data in the "input/" folder.
 - In the file "assets/stop-words.txt" you can change the directory of stop words
  if necessary.
   
Compile
```sh
javac -d out/production/Course-work ./src/*
```

# Launching

 - Open folder
```sh
cd out/production/Course-work
```
- Firstly, launch server
```sh
java Server
```

 - Secondly, launch client
```sh
java Client
```

# Configuring
 - Server (Server.java)
    - Port 
    ```sh
    public static final int PORT = 8080;
    ```
    - Number of threads
    ```sh
    public static final int nThreads = 5;
    ```
- Client (Client.java)
  - Ip and Port
   ```sh
   clientSocket = new Socket("localhost", 8080);
   ```
