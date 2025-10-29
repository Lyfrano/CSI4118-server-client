import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;



public class myClient {

    public static String serverAddress; // Server address
    public static int port; // Server port
    public static Socket socket = null;
    public static InputStream inputStream = null;
    
    public static void main(String[] args) {
        
        try {
            serverAddress = args[0];} 
        catch(Exception e){
            System.out.println("Server Adress Error"); }
        try {       
        port = Integer.valueOf(args[1]);} 
        catch(Exception e){
            System.out.println("Server Port Error");}
        try {
            // Create a socket to connect to the server
            socket = new Socket(serverAddress, port);
            System.out.println("Connected to server at " + serverAddress + ":" + port);
            socket.setSoTimeout(5000);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        String command;
        
        Scanner scanner = new Scanner(System.in);
    

        while(true){
        
            System.out.print("(Format is : CSI4118 <commandType> <studentNumber> [parameters] ) > ");
            command = scanner.nextLine();

            
            if (command.equals("CSI4118 type4")) {
                try {
                    try{
                        socket.getOutputStream().write((command + "\n").getBytes());
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                    try {
                        inputStream = socket.getInputStream();
                        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                        command = in.readLine(); // Read a line of text
                        System.out.println("Server: " + command);
                    } catch(SocketTimeoutException e){
                        System.out.println("No response from server, timed out.");
                        continue;
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }

                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            } 

            try{
                socket.getOutputStream().write((command + "\n").getBytes());
            } catch(Exception e){
                e.printStackTrace();
            }

            
            try {
            inputStream = socket.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                command = in.readLine(); // Read a line of text
                System.out.println("Server: " + command);
            } catch(SocketTimeoutException e){
                System.out.println("No response from server, timed out.");
                continue;
            }
             catch (Exception e) {
                e.printStackTrace();
            }
            
            
            
        }
        scanner.close();

    }

}