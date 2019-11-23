import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;


public class ChattyChatChatServer {

    protected String portNumber;
    protected String serverName;


    /**
     * Populates the shapeDefinitions vector with
     * some definitions of shapes.  You'll need to implement
     * the convertDescriptionsToShapes function to actually convert
     * these descriptions to useful shapes.
     *
     * @param filename the file containing the definitions of the shapes
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

        //TODO take portNumber as command line arg
        ChattyChatChatServer server = new ChattyChatChatServer(10071, "localhost");

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
                BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                String response = in.readLine();
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
     * Computes the sum of the shapes' areas, where the shapes
     * are from the shapes list
     * @return the sum of the shapes' areas
     */
    public void handleClient() {

    }//End clientHandler



    /**
     * Computes the sum of the shapes' perimeters, where the shapes
     * are from the shapes list
     * @return the sum of the shapes' perimeters
     */
    public void startNewThread(Runnable myRun) {

    }//END newThread

    public void startNewRun() {

    }//END newRun

    public void setPortNumber(String port) {
        portNumber = port;
    }//END setPortNumber

}