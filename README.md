# Indexer

- [What is it?](#What-is-it?)
- [Installation](#Installation)
- [Launching](#Launching)
- [Configuring](#Configuring)

# What is it?

The task of this work is to develop programs for building an inverted index, as well as - to use the index.

- During construction, it was possible to speed up the solution time by varying the
  number of threads.
- When constructing and using an index, a data structure with parallel access is used,
  while accessing it with several threads.
- It is possible to access this structure from different processes using network
  sockets (ie Client-Server application).

# Installation
Clone repository
```sh
git clone https://github.com/notlockeduser/Indexer.git
```
 - Place the unpacked input data in the "input/" folder.
 - In the file "assets/stop-words.txt" you can change the list of stop words
  if necessary.
   
Compile
```sh
javac -d out ./src/*
```

# Launching

 - Open folder
```sh
cd out
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
 - ServerConfig.txt
     - PORT - the port on which the server will run
     - nThreads - number of threads involved in indexing
    ```sh
    PORT=8080
    nThreads=5
    ```

   
- ClientConfig.txt
    - HOST - host to connect to the server
    - PORT - port for connecting to the server
   ```sh
   PORT=8080
   HOST=localhost
   ```
