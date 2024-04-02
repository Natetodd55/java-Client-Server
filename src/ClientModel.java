import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientModel {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientModel() {
        try {
            socket = new Socket("127.0.0.1", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Couldn't connect to server.");
            System.exit(1);
        }
    }

    boolean sendLogin(String username, String password){
        String result = "";
        String s = String.format("login %s %s", username, password);
        out.println(s);
        try {
            result = in.readLine();
        } catch (IOException e) {
            System.err.println("Error reading from server");
            System.exit(1);
        }
        if (result.equals("loggedin")){
            return true;
        }else {
            return false;
        }
    }

    boolean sendCreate(String username, String password){
        String result = "";
        String s = String.format("create %s %s", username, password);
        out.println(s);
        try {
            result = in.readLine();
        } catch (IOException e) {
            System.err.println("Error reading from server");
            System.exit(1);
        }
        if (result.equals("created")){
            return true;
        }else {
            return false;
        }
    }

    void sendLogout(){
        out.println("logout");
    }

    ArrayList sendScorerRequest(){
        ArrayList<String> data = new ArrayList<String>();
        out.println("getScorers");
        try {
            String dataToRead = in.readLine();
            String[] parts = dataToRead.split(" - ");
            String s1 = String.format("%s %s", parts[0], parts[1]);
            data.add(s1);

            if (parts.length > 3) {
                String s2 = String.format("%s %s", parts[2], parts[3]);
                data.add(s2);
            }
            if (parts.length > 5) {
                String s3 = String.format("%s %s", parts[4], parts[5]);
                data.add(s3);
            }
        } catch (IOException e) {
            System.err.println("Error reading from server");
            System.exit(1);
        }
        return data;
    }

    int sendBet(String betAmmount, String choice){
        int earnings = 0;
        String s = String.format("bet %s %s", betAmmount, choice);
        out.println(s);
        try {
            String outcome = in.readLine();
            String[] parts = outcome.split(" ");
            if(parts[0].equals("WINNER")){
                earnings = Integer.parseInt(parts[1]);
            } else if (parts[0].equals("noFunds")) {
                earnings = -20;
            }
        }catch (IOException e) {
            System.err.println("Error reading from server");
            System.exit(1);
        }
        return earnings;
    }

    void sendBalance(String balanceToAdd){
        String s = String.format("addbalance %s", balanceToAdd);
        out.println(s);
    }
}
