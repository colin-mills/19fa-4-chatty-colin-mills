import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ChattyChatChatServer {

    protected int portNumber;
    //protected String serverName;
    protected int clientNumber;
    protected Thread[] myThreads;

    ChattyChatChatServer() {
        portNumber = 0;
        clientNumber = 0;
        //serverName = "localhost";
        myThreads = null;
    }//ChattyDefault

    /**
     */
    ChattyChatChatServer(String portNum, String servName) {
        portNumber = portNum;
        //serverName = servName;
        clientNumber = 0;
        myThreads = null;
    }//END ChattyChatChat(portNum, ServNaMe)


    /**
     *
     * @param args command line arguments given by user
     */
    public static void main(String[] args) {
        ServerSocket portListener = null;
        boolean runServer = true;
        String stringPort = args[0];
        int intPort = Integer.parseInt(stringPort);
        this.setPortNumber(intPort);

        ChattyChatChatServer server = new ChattyChatChatServer(intPort) //, "localhost");

        try {
            portListener = new ServerSocket(portNumber);
        }
        catch (SocketException e) {
            System.out.println("Error establishing listener");
            runServer = false;
        }//END Socket error
        catch (Exception e) {
            System.out.println("Unknown error establishing listener");
            runServer = false;
        }//END unkown error

        while(runServer) {
            Socket socket = null;
            try {
                socket = portListener.accept();
                this.startNewRun(socket, server.clientNumber);
                server.addClient();
                //BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                //String response = in.readLine();
            }
            catch (IOException e) {
                System.out.println("Error establishing socket");
                runServer = false;
            }//END IO error
            catch (Exception e) {
                System.out.println("Unknown error establishing socket");
                runServer = false;
            }//END unkown error
            finally {
                try {
                    //socket.close();
                    //portListener.close();
            }//END try close
                catch (Exception e) {

                }//END catch close exception

        }//END whileServer loop



    }//END main

    /**
     *
     */
    public void startNewThread(Socket socket, int clientNum, ChattyChatChatServer serv) {
        Runnable serverRun = new ChattyServerRunnable(socket, serv);
        myThread[clientNum] = new Thread(serverRun);
        myThread[clientNumber].start();
    }//END newRun

    public void setPortNumber(int port) {
        portNumber = port;
    }//END setPortNumber

    public void addClient() {
        this.clientNumber = clientNumber + 1;
        }//END add client


}