import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChattyServerRunnable implements Runnable {
    protected Socket socket;
    protected String clientName;
    protected ChattyChatChat server;
    protected BufferedReader in;
    protected PrintWriter socketOut;

    ChattyServerRunnable(Socket sock, ChattyChatChatServer myServer) {
        socket = sock;
        clientName = "";
        server = myServer;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socketOut = new PrintWriter(socket.getOutputStream(), true);
    }// END ChattyServerRunnable()

    @Overide
    public void run() {
        boolean done = false;

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
                    this.sendDM(parsedResponse);
                }//END DM
                else { //This is just a normal message
                    this.sendNormal(parsedResponse);
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
    }//END setNickName

    public void sendDM(String[] parsedResponse) {
        String name = parsedResponse[1];
        String message = "";
        for (int i = 1; i < parsedResponse.length(); i++) {
            message += parsedResponse[i];
        }//END for loop message
        server.findDM(name, message);
    }//END sendDM

    public void sendNormal(String[] parsedResponse) {
        String message = "";
        for (int i = 0; i < parsedResponse.length(); i++) {
            message += parsedResponse[i];
        }//END for loop message
        server.sendAll(message);
    }//END sendNormal

    public void sendMessage(string message) {
        socketOut.println(message);
    }//END send message

    public String getClientName() {
        return clientName;
    }//END
}//END ChattyThread