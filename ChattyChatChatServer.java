import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * @author Colin Mills
 */


public class ChattyChatChatServer {

    /**
     * Main waits at port for connections and then passes clients to new thread
     * @param args command line arguments given by user
     */
    public static void main(String[] args) {
        //Initialize all needed objects
        ServerSocket portListener = null;
        boolean runServer = true;
        String stringPort = args[0];
        int intPort = Integer.parseInt(stringPort);
        Socket socket = null;
        ClientList list = new ClientList();
        BufferedReader readerIn = new BufferedReader(new InputStreamReader(System.in));
        String serverSideInput = "";
        Thread myThread = null;
        Runnable myRun = null;

        try {
            //First bind to port
            System.out.println("Binding to port " + stringPort + " If you wish to terminate type \"exit\"");
            portListener = new ServerSocket(intPort);
        } catch (IOException e) {
            System.out.println("Error establishing listener");
            runServer = false;
        }//END IO error
        catch (Exception e) {
            System.out.println("Unknown error establishing listener");
            runServer = false;
        }/* END unknown error */

        while (runServer) {
            try {
                //Waits until connection is accepted
                System.out.println("Ready for a new client.");
                socket = portListener.accept();
                myRun = new ChattyServerRunnable(socket, list);
                myThread = new Thread(myRun);
                //Passes client handling responsibilities to new thread
                myThread.start();
            }
            catch (IOException e) {
                System.out.println("Error establishing socket");
                System.out.println(e.fillInStackTrace());
                System.out.println(e.getCause());
                runServer = false;
            }//END IO error
            catch (Exception e) {
                System.out.println("Unknown error establishing socket");
                System.out.println(e.fillInStackTrace());
                System.out.println(e.getCause());
                runServer = false;
            }//END unknown error
        }//END while loop
        try {
            System.out.println("Closing connection");
        }
        finally {
                try {
                    portListener.close();
                    readerIn.close();
                }//END try close
                catch (Exception e) {/* Nothing */}//END catch close exception
        }//END Finally
    }//END main

    /**
     * Client knows its own name and how to send to that client
     */
    public static class Client {
        protected String nickName;
        protected PrintWriter socketOut;

        /**
         * Initialize client
         * @param out is Print Writer for desired socket connection
         */
        Client(PrintWriter out) {
            nickName = " ";
            socketOut = out;
        } //END constructor

        /**
         * Sets nickname for DM purposes
         * @param newName value passed by client handler for what client name should be
         */
        public void setNickName (String newName) {
            nickName = newName;
        }//END setNickName

        /**
         * Lets client handler find person with nickName
         * @return returns nickName
         */
        public String getNickName() {
            return nickName;
        }//END get NickName

        /**
         * Uses print writer to send message
         * @param msg string of what should be sent to client
         */
        public void sendMsg (String msg) {
            socketOut.println(msg);
        }//END sendMsg
    } //END Client

    /**
     * Keeps track of all clients for server and delegates what Clients should do
     */
    public static class ClientList {
        protected Vector<Client> clientList;

        /**
         * Initialize new vector of type client
         */
        ClientList() {
            clientList = new Vector<>();
        }//END constructor

        /**
         * adds new client.. called by ChattyRunnable
         * @param newClient new client that has been connected
         */
        public void addClient(Client newClient) {
            clientList.add(newClient);
        }//END addClient

        /**
         * Tells each client to send this message
         * @param msg String sent to each connected client
         */
        public void sendPublic(String msg) {
            for(Client c: clientList) {
                c.sendMsg(msg);
            }//END for Client
        }//END sendPublic

        /**
         * Sends private DM
         * @param name Only sent to matching names
         * @param msg from client
         */
        public void sendDM(String name, String msg) {
            for(Client c: clientList) {
                if(name.equals(c.getNickName())) {
                    //Only send if matching
                    c.sendMsg(msg);
                }//END if
            }//END for Client
        }//END send DM

    }//END ClientList

    /**
     * Runnable that gets started in a new thread and is responsible for interaction with a single client
     */
    public static class ChattyServerRunnable implements Runnable {
        protected Socket socket;
        protected BufferedReader in;
        protected Client myClient;
        protected ClientList list;

        /**
         * Initializes everything this thread needs to know and tells client list
         * @param sock socket connection used to establish input and output readers/writers
         * @param list clientList passed in so new client can be added to same one
         */
        ChattyServerRunnable(Socket sock, ClientList list) {
            try {
                socket = sock;
                //Create reader/writer from socket connection
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
                //Create and add client to list
                myClient = new Client(socketOut);
                list.addClient(myClient);
                this.list = list;
            }//END try
            catch (IOException e) {
                System.out.println("Error establishing client server connection in runnable thread");
                System.out.println(e.fillInStackTrace());
                System.out.println(e.getCause());
            }//
        }// END ChattyServerRunnable()

        /**
         * Stays running as long as client is connected
         * Delegates all responsibilities to client handler when necessary
         */
        @Override
        public void run() {

            boolean done = false;
            System.out.println("Successfully passed client to their own thread. Waiting for client interaction.");

                try {
                    String[] parsedResponse;
                    while(!done) {
                        String response = in.readLine();
                        System.out.println("Received: " + response);
                        parsedResponse = response.split(" ");
                        //Gets response and splits it up for evaluation

                        if (parsedResponse[0].equals("/quit")) {
                            System.out.println("Ending connection with a client");
                            done = true;
                        }//END quit
                        else if (parsedResponse[0].equals("/nick")) {
                            System.out.println("Changing name of a client");
                            myClient.setNickName(parsedResponse[1]);
                        }//END nickname
                        else if (parsedResponse[0].equals("/dm")) {
                            String name = parsedResponse[1];
                            String message = "";
                            System.out.println("Sending DM to " + name);
                            for (int i = 2; i < parsedResponse.length; i++) {
                                if (i == 2) {
                                    //So there isn't a leading space
                                    message += parsedResponse[i];
                                }//END first time
                                else {
                                    message += (" " + parsedResponse[i]);
                                }//END else
                            }//END for
                            list.sendDM(name, message);
                        }//END DM
                        else { //This is just a normal message
                            String message = "";
                            System.out.println("Sending message to all");
                            for (int i = 0; i < parsedResponse.length; i++) {
                                if (i == 0) {
                                    //So there isn't a leading space
                                    message += parsedResponse[i];
                                }//First case
                                else {
                                    message += (" " + parsedResponse[i]);
                                }//End else
                            }//END for
                            list.sendPublic(message);
                        }//END normal
                    }//END while !done
                }//END try
                catch (IOException e) {
                    System.out.println("Connection interrupted... Ending Thread.");
                }//END IO exception
                catch (Exception e) {
                    System.out.println("Unknown error from ChattyServerRunnable");
                    System.out.println(e.fillInStackTrace());
                    System.out.println(e.getCause());
                }//END unknown error
                finally {
                    try {
                        socket.close();
                        System.out.println("Socket successfully closed.");
                    }//END try close
                    catch (Exception e) {/*Nothing*/}
                }//END finally
        }//END run()

    }//END ChattyThread

}//END Chatty

