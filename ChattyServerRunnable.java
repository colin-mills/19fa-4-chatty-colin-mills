//TODO not this
//Make a server runnable that handles client interactions beyond the connection
//Make client runnabable that handles client commands and such (See comments for UML)

public class ChattyServerRunnable implements Runnable {
    protected double timeoutCounter;
    protected Socket socket;

    ChattyServerRunnable(Socket sock) {
        socket = sock;

    }// END ChattyThread()
    @Overide
    public void run() {
        BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
        String response = in.readLine();
    }//END start
}//END ChattyThread