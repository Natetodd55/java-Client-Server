import io.github.pixee.security.BoundedLineReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started on port 5000");

            while (true) {
                System.out.println("Waiting for client...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ServerModel serverModel;


        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            this.serverModel = new ServerModel();
            this.serverModel.connect();
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                String line;
                while ((line = BoundedLineReader.readLine(in, 5_000_000)) != null) {
                    try {
                        String[] parts = line.split(" ");
                        String action = parts[0];
                        switch(action){
                            case "login":
                                if(serverModel.loginUser(parts[1], parts[2])){
                                    out.println("loggedin");
                                }else{
                                    out.println("failedlogin");
                                }
                                break;
                            case "create":
                                serverModel.addDb(parts[1], parts[2]);
                                out.println("created");
                                break;
                            case "bet":
                                int betAmmount = Integer.parseInt(parts[1]);
                                if (betAmmount > serverModel.getUserBalance()){
                                    out.println("noFunds");
                                }else {
                                    Random random = new SecureRandom();
                                    int result = random.nextInt(2);
                                    String output = (result == 0) ? "heads" : "tails";
                                    if (output.equals(parts[2])) {
                                        betAmmount += betAmmount;
                                        String s = String.format("WINNER %d", betAmmount);
                                        serverModel.updateTotalEarnings(betAmmount);
                                        out.println(s);
                                    } else {
                                        serverModel.removeBalance(betAmmount);
                                        String s = String.format("LOSER %d", betAmmount);
                                        out.println(s);
                                    }
                                }
                                break;
                            case "logout":
                                serverModel.logoutUser();
                                break;
                            case "getScorers":
                                StringBuilder sb = new StringBuilder();
                                ArrayList<String> topScorers = serverModel.getTopScorers();
                                for (String data : topScorers) {
                                    String[] partss = data.split(" ");
                                    String username = partss[1];
                                    int earnings = Integer.parseInt(partss[0]);
                                    sb.append(username).append(" - ").append(earnings).append(" - ");
                                }
                                String stringtosend = sb.toString();
                                out.println(stringtosend);
                                break;
                            case "addbalance":
                                int bal = Integer.parseInt(parts[1]);
                                serverModel.addBalance(bal);
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid action: " + action);
                        }
                    } catch (Exception e) {
                        System.err.println("Error processing request: " + e.getMessage());
                        out.println("Error: " + e.getMessage());
                    }
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
