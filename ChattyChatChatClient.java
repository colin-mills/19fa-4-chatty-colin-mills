import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChattyChatChatClient {

    protected String name;

    /**
     * Populates the shapeDefinitions vector with
     * some definitions of shapes.  You'll need to implement
     * the convertDescriptionsToShapes function to actually convert
     * these descriptions to useful shapes.
     *
     * @param filename the file containing the definitions of the shapes
     */
    ChattyChatChatClient() {

    }//END ChattyChatChat()

    /**
     * Populates the shapeDefinitions vector with
     * some definitions of shapes.  You'll need to implement
     * the convertDescriptionsToShapes function to actually convert
     * these descriptions to useful shapes.
     *
     * @param filename the file containing the definitions of the shapes
     */
    ChattyChatChatClient(String nickName) {

    }//END ChattyChatChat(nickName)


    /**
     *
     * @param args command line arguments given by user
     */
    public static void main(String[] args) {
        //Todo accept variables as command line arguments
        string hostName = "localhost";
        int port = 10071;
        Socket socket = null;
        runServer = true;

        while (runServer) {
            try {
                socket = new Socket(hostName, port);
                String userInput = userin.readLine(); //Gets user input
                //BufferedReader in = new BufferedReader( new InputStreamReader(System.in));
                //String response = in.readLine();
            } catch (IOException e) {
                System.out.println("Error connecting to server");
                runServer = false;
            }//End can't connect to server
            finally {
                try {
                    socket.close();
                }//END try close
                catch (Exception e) {

                }//END catch close exception
            }//END finally
        }//END while (runServer)
    }//END main

    /**
     * Computes the sum of the shapes' areas, where the shapes
     * are from the shapes list
     * @return the sum of the shapes' areas
     */
    public void sendNickName() {

    }//End sendNickName

    /**
     * Computes the sum of the shapes' areas, where the shapes
     * are from the shapes list
     * @return the sum of the shapes' areas
     */
    public void setNickName(myName) {

    }//End setNickName

    /**
     * Computes the sum of the shapes' perimeters, where the shapes
     * are from the shapes list
     * @return the sum of the shapes' perimeters
     */
    public void sendNormalMessage(String message) {

    }//END sendNormalMessage

    public void sendPrivate(String name, String msg) {

    }//END sendPrivate

    public void exit() {

    }//END exit()

}