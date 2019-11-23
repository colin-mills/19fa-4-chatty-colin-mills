import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChattyServerRunnable implements Runnable {
    protected Socket socket;
    protected String clientName;
    protected ChattyChatChat

    ChattyServerRunnable(Socket sock, ChattyChatChatServer myServer) {
        socket = sock;
        clientName = "";
    }// END ChattyServerRunnable()

    @Overide
    public void run() {
        boolean done = false;
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);

        while(!done) {
            try {
                String response = in.readLine();

                String[] parsedResponse = response.split(" ");

                if (parsedResponse[0] == "/quit") {
                    done = true;
                }//END quit
                else if (parsedResponse[0] == "/nick") {
                    this.setNickName(parsedResponse);
                }//END nickname
                else if (parsedResponse[0] == "/dm") {
                    this.sendDM(parsedResponse, socketOut);
                }//END DM
                else { //This is just a normal message
                    this.sendNormal(parsedResponse, socketOut);
                }//END normal
            }//END try
            catch (Exception e) {
                System.out.println("Unknown error from ChattyServerRunnable");
                done = true;
            }//END unkown error
            finally {
                try {
                    socket.close();
                }//END try close
                catch (Exception e) {/*Nothing*/}
            }//END finally
        }//END while !done
    }//END run()

    public void setNickName(String[] parsedResponse) {
        clientName = parsedResponse[1];

    }//END

    public void sendDM(String[] parsedResponse, PrintWriter socketOut) {

    }//END

    public void sendNormal(String[] parsedResponse, PrintWriter socketOut) {

    }//END



}//END ChattyThread