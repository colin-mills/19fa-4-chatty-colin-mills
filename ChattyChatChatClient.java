import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ChattyChatChatClient {

    /**
     * Connects client to server and waits for client input to send to server
     * passes listening responsibilities to new thread
     * @param args should have a hostName and a port number as command line arguments
     */
    public static void main(String[] args) {
        //All objects that main needs to use
        String hostName = args[0];
        String userInput = "";
        String[] parsedInput = null;
        int port = Integer.parseInt(args[1]);
        boolean runServer = true;
        Socket socket = null;
        BufferedReader readerIn = null;
        BufferedReader serverIn = null;
        PrintStream socketOut = null;
        Thread clientThread = null;
        Runnable clientRun = null;

        try {
            //Connect to server
            socket = new Socket(hostName, port);
            System.out.println("Connection successful, Type \"/quit\" to quit, \"dm\" followed by a name and message to dm, or just a message for all.");
            //Initialize readers/writers for communication
            readerIn = new BufferedReader(new InputStreamReader(System.in));
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketOut = new PrintStream(socket.getOutputStream(), true);
            //Pass listening responsibilities elsewhere
            clientRun = new ChattyClientRunnable(serverIn);
            clientThread = new Thread(clientRun);
            clientThread.start();
            System.out.println("Listening for messages from the server on another thread");

            while (runServer) {
                userInput = readerIn.readLine();
                parsedInput = userInput.split(" ");

                if (parsedInput[0].equals("/quit")) {
                    runServer = false;
                    System.out.println("Terminating connection to server");
                }//END if
                //Will send a message to the server regardless, but will end loop if quit is the msg
                socketOut.println(userInput);
            }//END while (runServer)
        }
        catch (IOException e) {
            System.out.println("Error connecting to server");
            System.out.println(e.fillInStackTrace());
            System.out.println(e.getCause());
        }//End can't connect to server
        catch (Exception e) {
            System.out.println("Unknown Error connecting to server");
            System.out.println(e.fillInStackTrace());
            System.out.println(e.getCause());
        }
        finally {
            try {
                //Make sure everything is closed
                socket.close();
                readerIn.close();
                socketOut.close();
                System.out.println("Connection successfully terminated");
            }//END try close
            catch (Exception e) {
            }//END catch close exception
        }//END finally
    }//END main

    /**
     * Listens to server until connection is stopped by main
     */
    public static class ChattyClientRunnable implements Runnable {
        protected BufferedReader socketIn;

        /**
         * initializes where to listen from server
         * @param socketIn Buffered Reader connected to server through socket
         */
        ChattyClientRunnable(BufferedReader socketIn) {
            this.socketIn = socketIn;
        }// END ChattyThread()

        /**
         * Listens as long as there is a connection and outputs results
         */
        @Override
        public void run() {

            String inMessage = "";
            Boolean done = false;

            while (!done) {
                try {
                    inMessage = socketIn.readLine();
                    //If there is anything it'll get output
                    if (!(inMessage.equals(""))) {
                        System.out.println(inMessage);
                    }//END else
                }//END try
                catch (IOException e) {
                    //Main has ended connection or other issue has come up, either way this runnable isn't needed
                    System.out.println("Connection interrupted... Ending runnable.");
                    done = true;
                }//END IO exception
                catch (Exception e) {
                    System.out.println("Unknown error while listening to server.");
                    System.out.println(e.fillInStackTrace());
                    System.out.println(e.getCause());
                    done = true;
                }//END unknown exception
            }//END while
        }//END run
    }//END ChattyClientRunnable
}//END ChattyChatChatClient