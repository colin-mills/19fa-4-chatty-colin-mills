import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ChattyChatChatClient {

    /**

     */
    public static void main(String[] args) {
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
            socket = new Socket(hostName, port);
            System.out.println("Connection successful, Type \"/quit\" to quit, \"dm\" followed by a name and message to dm, or just a message for all.");
            readerIn = new BufferedReader(new InputStreamReader(System.in));
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketOut = new PrintStream(socket.getOutputStream(), true);

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

                socketOut.println(userInput);
                //System.out.println("Sent: " + userInput);

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
                socket.close();
                readerIn.close();
                socketOut.close();
                System.out.println("Connection successfully terminated");
            }//END try close
            catch (Exception e) {

            }//END catch close exception

        }//END finally


    }//END main

    public static class ChattyClientRunnable implements Runnable {
        protected BufferedReader socketIn;

        ChattyClientRunnable(BufferedReader socketIn) {
            this.socketIn = socketIn;
        }// END ChattyThread()

        @Override
        public void run() {
            String inMessage = "";
            Boolean done = false;

            while (!done) {

                try {
                    inMessage = socketIn.readLine();

                    if (!(inMessage.equals(""))) {
                        System.out.println(inMessage);
                    }//END else
                }//END try
                catch (IOException e) {
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

}