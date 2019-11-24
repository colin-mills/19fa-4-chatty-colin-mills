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

        try {
            socket = new Socket(hostName, port);
            userInput = readerIn.readLine(); //Gets user input
            readerIn = new BufferedReader(new InputStreamReader(System.in));
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketOut = new PrintStream(socket.getOutputStream(), true);

            new Thread(new ChattyClientRunnable(serverIn)).start();
            while (runServer) {
                userInput = readerIn.readLine();
                parsedInput = userInput.split(" ");

                if (parsedInput[0].equals("/quit")) {
                    runServer = false;
                    System.out.println("Terminating connection to server");
                }//END if

                socketOut.println(userInput);

            }//END while (runServer)
        }
        catch (IOException e) {
            System.out.println("Error connecting to server");
        }//End can't connect to server
        catch (Exception e) {
            System.out.println("Unknown Error connecting to server");
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

                    if (!inMessage.equals(null)) {
                        System.out.println(inMessage);
                    }//END else
                }//END try
                catch (IOException e) {
                    System.out.println("Connection interrupted.");
                    done = true;
                }//END IO exception
                catch (Exception e) {
                    System.out.println("Unknown error while listening to server.");
                    done = true;
                }//END unknown exception

            }//END while

        }//END run
    }//END ChattyClientRunnable

}