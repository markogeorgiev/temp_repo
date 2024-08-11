//package TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread{
    private int port;
    public static int NUM_MESSAGES;
    public static int NUM_CLIENTS;

    public TCPServer(int port){
        this.port = port;
    }

    public static void printNuMessages(){

    }

    @Override

    public void run() {
        System.out.println("TCP Server is starting...");

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("TCP server has started!");
        System.out.println("Waiting for clients to connect");

        try {
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection from " + socket.getRemoteSocketAddress());
                TCPServer.NUM_CLIENTS++;
                new WorkerThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TCPServer server = new TCPServer(9002);
        server.start();
    }
}

class WorkerThread extends Thread {
    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;

    public WorkerThread(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        String receivedLine = null;
        int temp = socket.getPort();
        try {
            while (!((receivedLine = reader.readLine()) == null)){
                TCPServer.NUM_MESSAGES++;
                System.out.printf("%d. From %s: %s\n",TCPServer.NUM_MESSAGES, socket.getInetAddress(), receivedLine);
                writer.write(String.format("Message '%s' received\n", receivedLine));
                writer.flush();
            }

        } catch (IOException ignored) {

        } finally {
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException ignored) {

                }
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException ignored) {

                }
            }
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.printf("Client at port '%d', has left the chat!\n", temp);
    }
}