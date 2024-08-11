//package TCP;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class TCPClient extends Thread{
    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;
    Random random;

    public TCPClient(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        String msgFromServer;
        try {
            while ((msgFromServer = reader.readLine()) != null){
                System.out.println(msgFromServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
                reader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendData(){
        String msgFromServer;
        try {
            Scanner sc = new Scanner(System.in);
            String msgToSend;

            while ((msgToSend = sc.nextLine()) != null){
                writer.write(msgToSend);
                writer.newLine();
                writer.flush();

                if (msgToSend.equals("END")){
                    break;
                }
            }
            //Receive 7 communication data
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
                reader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9002);
        TCPClient client = new TCPClient(socket);
        client.start();
        client.sendData();
    }
}
