import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

//import java.io.IOException.SocketException;


public class ChattyChatChatServer {

    /**
     *
     * @param args command line arguments given by user
     */
    public static void main(String[] args) {
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
                System.out.println("Ready for a new client.");
                socket = portListener.accept();
                myRun = new ChattyServerRunnable(socket, list);
                myThread = new Thread(myRun);
                myThread.start();
            } catch (IOException e) {
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

            /*try {
                serverSideInput = readerIn.readLine();

                if (serverSideInput.equals("exit") || serverSideInput.equals("Exit")) {
                    runServer = false;
                }//END if exit
            } catch (IOException e) {
                System.out.println("Error with server side input");
                System.out.println(e.fillInStackTrace());
                System.out.println(e.getCause());
            }
*/

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

    public static class Client {
        protected String nickName;
        protected PrintWriter socketOut;

        Client(PrintWriter out) {
            nickName = " ";
            socketOut = out;
        } //END constructor

        public void setNickName (String newName) {
            nickName = newName;
        }//END setNickName

        public String getNickName() {
            return nickName;
        }//END get NickName

        public void sendMsg (String msg) {
            socketOut.println(msg);
        }//END sendMsg
    } //END Client

    public static class ClientList {
        protected Vector<Client> clientList;

        ClientList() {
            clientList = new Vector<>();
        }//END constructor

        public void addClient(Client newClient) {
            clientList.add(newClient);
        }//END addClient

        public void sendPublic(String msg) {
            for(Client c: clientList) {
                c.sendMsg(msg);
            }//END for Client
        }//END sendPublic

        public void sendDM(String name, String msg) {
            for(Client c: clientList) {
                if(name.equals(c.getNickName())) {
                    c.sendMsg(msg);
                }//END if
            }//END for Client
        }//END send DM

    }//END ClientList

    public static class ChattyServerRunnable implements Runnable {
        protected Socket socket;
        protected BufferedReader in;
        protected Client myClient;
        protected ClientList list;

        ChattyServerRunnable(Socket sock, ClientList list) {
            try {
                socket = sock;
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
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
                        //System.out.println("parsed first word: " + parsedResponse[0]);

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

