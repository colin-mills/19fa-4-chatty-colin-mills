import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;


public class ChattyChatChatServer {

    protected int portNumber;
    protected String serverName;

    ChattyChatChatServer() {
        portNumber = 0;
        serverName = "localhost";
    }//ChattyDefault

    /**
     */
    ChattyChatChatServer(String portNum, String servName) {
        portNumber = portNum;
        serverName = servName;
    }//END ChattyChatChat(portNum, ServNaMe)


    /**
     *
     * @param args command line arguments given by user
     */
    public static void main(String[] args) {
        ServerSocket portListener = null;
        boolean runServer = true;
        String stringPort = args[0];
        int intPort = Integer.parseInt();

        this.setPortNumber(intPort);

        //ChattyChatChatServer server = new ChattyChatChatServer(10071, "localhost");

        try {
            portListener = new ServerSocket(server.portNumber);
        }
        /*catch (SocketException e) {
            System.out.println("Error establishing listener");
            runServer = false;
        }//END IO error */
        catch (Exception e) {
            System.out.println("Unknown error establishing listener");
            runServer = false;
        }//END unkown error

        while(runServer) {
            Socket socket = null;
            try {
                socket = portListener.accept();

                this.startNewRun(socket)
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
                    socket.close();
            }//END try close
                catch (Exception e) {

                }//END catch close exception

        }//END whileServer loop

    }//END main

    /**
     *
     */
    public void startNewThread(Socket socket) {
        Runnable serverRun = new ChattyServerRunnable(socket);
        Thread handleClientThread = new Thread(serverRun);
        handleClientThread.start();
    }//END newRun

    public void setPortNumber(int port) {
        portNumber = port;
    }//END setPortNumber

}