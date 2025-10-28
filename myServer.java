import java.io.*;
import java.net.*;

public class myServer {

     enum CommandType {
            type0,
            type1,
            type2,
            type3,
            type4
        }

    public static int cypher = 3;
    public static String decoded;
    public static int port = 12345;
    public static String serverAddress = "";

    public static void main(String[] args) {


        String command;
        String[] parsedCommand = new String[4];
        ServerSocket serverSocket = null;
        InputStream inputStream = null;
        String errorMessage = "ERROR: Invalid command format";


        try{
            serverAddress = InetAddress.getLocalHost().getHostAddress();
        }catch(Exception e){
            e.printStackTrace();
        }    
        try{port = Integer.valueOf(args[0]) ; // Server port
        } catch(Exception e){
            System.out.println("Using default port 12345");
            System.out.println();
        }

        System.out.println("Server IP Address: " + serverAddress);


        try {
            // Create a server socket to listen for client connections
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

            while (true) {
                // Accept a client connection
                

                inputStream = clientSocket.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                command = in.readLine(); // Read a line of text

                parsedCommand = command.split(" ");
                System.out.println("Received command: " + command);
                addtoLog("Received command from : " + clientSocket.getInetAddress() + " contents " + command);


                if (!(parsedCommand[0].equals("CSI4118"))) {
                    System.out.println(errorMessage); 
                    try{
                    clientSocket.getOutputStream().write((errorMessage + "\n").getBytes());
                    continue;
                    } catch(Exception e){
                     e.printStackTrace();
                    }

                }else if("type0".equals(parsedCommand[1])){
                    // Handle type0 command
                    System.out.println("Handling type0 command");
                    cypher = Integer.parseInt(parsedCommand[3]);
                    if (cypher < 1 || cypher > 25) {
                        System.out.println("Cypher value out of range");
                        try{
                        clientSocket.getOutputStream().write(("ERROR: Cypher value out of range" + "\n").getBytes());
                        continue;
                        } catch(Exception e){
                         e.printStackTrace();
                        }
                        cypher = 3; // reset to default
                    } else {
                        System.out.println("Cypher set to " + cypher);
                        try{
                        clientSocket.getOutputStream().write(("OK" + "\n").getBytes());
                        continue;
                        } catch(Exception e){
                         e.printStackTrace();
                        }
                    }

                }else if ("type1".equals(parsedCommand[1])) {
                    System.out.println("Handling type1 command");
                    try{
                    clientSocket.getOutputStream().write((clientSocket.getInetAddress() + "\n").getBytes());
                    } catch(Exception e){
                     e.printStackTrace();
                    }
                }else if ("type2".equals(parsedCommand[1])) {
                    System.out.println("Handling type2 command");
                    try{
                    clientSocket.getOutputStream().write(("1 2 3 4 5 6 7 8 9 10" + "\n").getBytes());
                    } catch(Exception e){
                     e.printStackTrace();
                    }
                }else if ("type3".equals(parsedCommand[1])) {
                    System.out.println("Handling type3 command");
                    decoded = decodeString(parsedCommand[3]);
                    addtoLog(decoded + '\n' );
                    try{
                    clientSocket.getOutputStream().write(("GOT IT" + "\n").getBytes());
                    } catch(Exception e){
                     e.printStackTrace();
                    }   
                }else if ("type4".equals(parsedCommand[1])) {
                    System.out.println("Handling type4 command");
                    try{
                    clientSocket.getOutputStream().write(("BYE" + "\n").getBytes());
                    } catch(Exception e){
                     e.printStackTrace();
                    }
                }else{
                    System.out.println("Unknown command type");
                    try{
                    clientSocket.getOutputStream().write(("ERROR: Unknown command type" + "\n").getBytes());
                    } catch(Exception e){
                     e.printStackTrace();
                    }
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String decodeString(String input) {
        char[] caesar = input.toCharArray();
        for (int i = 0; i < caesar.length; i++) {
            caesar[i] = (char) (caesar[i] - cypher);
        }

        return "";
    }

    public static void addtoLog(String logEntry) {
        try {
            FileWriter fw = new FileWriter("log.txt", true);
            fw.write(logEntry + "\n");
            fw.close();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}