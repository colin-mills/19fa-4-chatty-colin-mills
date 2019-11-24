import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChattyChatChatClient {

    protected String name;

    /**

     */
    ChattyChatChatClient() {

    }//END ChattyChatChat()

    /**

     */
    ChattyChatChatClient(String nickName) {

    }//END ChattyChatChat(nickName)


    /**

     */
    public static void main(String[] args) {
        //Todo accept variables as command line arguments
        String hostName = "localhost";
        int port = 10071;
        Socket socket = null;
        boolean runServer = true;
        BufferedReader readerIn = new BufferedReader(new InputStreamReader(System.in));

        while (runServer) {
            try {
                socket = new Socket(hostName, port);
                String userInput = readerIn.readLine(); //Gets user input
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

     */
    public void sendNickName() {

    }//End sendNickName

    /**

     */
    public void setNickName(String myName) {

    }//End setNickName

    /**

     */
    public void sendNormalMessage(String message) {

    }//END sendNormalMessage

    public void sendPrivate(String name, String msg) {

    }//END sendPrivate

    public void exit() {

    }//END exit()

    public static class ChattyClientRunnable implements Runnable {
        protected double timeoutCounter;

        ChattyClientRunnable() {

        }// END ChattyThread()
        @Override
        public void run() {

        }//END start
    }//END ChattyClientRunnable

}