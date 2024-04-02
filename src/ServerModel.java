import java.sql.*;
import java.util.ArrayList;

public class ServerModel {
    private boolean loggedIn = false;
    private int userBalance = 0;
    private int userTotalEarnings = 0;
    private int currUserID = 0;

    public ServerModel() {
        // Load the SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1); // Exit the application if the driver is not found
        }
    }
    void connect() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            String cmd = "CREATE TABLE IF NOT EXISTS betters (" +
                    "id INTEGER PRIMARY KEY," +
                    "username STRING," +
                    "password STRING," +
                    "balance INT DEFAULT 0," +
                    "totalEarnings INT DEFAULT 0)";

            conn.createStatement().executeUpdate(cmd);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addDb(String username, String password) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM betters WHERE username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();

            if (count == 0) {
                conn.createStatement().executeUpdate(String.format("INSERT INTO betters (username, password) VALUES ('%s','%s');", username, password));
                userTotalEarnings = 0;
                userBalance = 0;
                currUserID = getUserID(username);
            } else {
                System.out.println("Username already exists");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    ArrayList getDb() {
        ArrayList<String> data = new ArrayList<String>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            String cmd = "SELECT * FROM betters;";
            ResultSet rs = conn.createStatement().executeQuery(cmd);
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String s = String.format("%3d %10s %10s", id, username, password);
                data.add(s);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    void upDb(int id, int ammount) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            conn.createStatement().executeUpdate(String.format("UPDATE betters SET totalEarnings = '%d' id = %d;", ammount, id));
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void delDb(Integer id) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            conn.createStatement().executeUpdate(String.format("DELETE FROM betters WHERE id = %d;", id));
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    boolean loginUser(String username, String password) {
        Integer loginVal = 0;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            String cmd = (String.format("SELECT password, balance, totalEarnings FROM betters WHERE username = '%s';", username));
            ResultSet rs = conn.createStatement().executeQuery(cmd);
            while (rs.next()) {
//                int id = rs.getInt("id");
                String passwordToCheck = rs.getString("password");
                userBalance = rs.getInt("balance");
                userTotalEarnings = rs.getInt("totalEarnings");
                if (password.equals(passwordToCheck)){
                    loginVal = 1;
                }
            }
            currUserID = getUserID(username);
        } catch (SQLException ex) {
            ex.printStackTrace();
            loggedIn = false;
            return false;
        }
        if (loginVal == 1) {
            loggedIn = true;
            return true;
        } else {
            loggedIn = false;
            return false;
        }
    }

    ArrayList<String> getTopScorers(){
        ArrayList<String> data = new ArrayList<String>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            String cmd = "SELECT username, totalEarnings FROM betters ORDER BY totalEarnings DESC LIMIT 3;";
            ResultSet rs = conn.createStatement().executeQuery(cmd);
            while (rs.next()) {
                int earnings = rs.getInt("totalEarnings");
                String username = rs.getString("username");
                String s = String.format("%d %s", earnings, username);
                data.add(s);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    int updateTotalEarnings(int totalEarningsToAdd){
        int earnings = 0;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            String cmd = (String.format("SELECT totalEarnings FROM betters WHERE id = '%d';", currUserID));
            ResultSet rs = conn.createStatement().executeQuery(cmd);
            while (rs.next()) {
                earnings = rs.getInt("totalEarnings");
            }
            earnings = earnings+totalEarningsToAdd;
            userTotalEarnings = earnings;
            conn.createStatement().executeUpdate(String.format("UPDATE betters SET totalEarnings = '%d' WHERE id = '%d';", earnings, currUserID));
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return earnings;
    }

    int getUserID(String username){
        int id = -1;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            String cmd = (String.format("SELECT id FROM betters WHERE username = '%s';", username));
            ResultSet rs = conn.createStatement().executeQuery(cmd);
            while (rs.next()) {
                id = rs.getInt("id");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    void removeBalance(int balanceToSubtract){
        userBalance = userBalance - balanceToSubtract;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            conn.createStatement().executeUpdate(String.format("UPDATE betters SET balance = '%d' WHERE id = '%d';", userBalance, currUserID));
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isAuthenticated() {
        return loggedIn;
    }

    public int getUserBalance() {
        return userBalance;
    }

    void addBalance(int balanceToAdd){
        int balance = 0;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            String cmd = (String.format("SELECT balance FROM betters WHERE id = '%d';", currUserID));
            ResultSet rs = conn.createStatement().executeQuery(cmd);
            while (rs.next()) {
                balance = rs.getInt("balance");
            }
            balance = balance+balanceToAdd;
            userBalance = balance;
            conn.createStatement().executeUpdate(String.format("UPDATE betters SET balance = '%d' WHERE id = '%d';", balance, currUserID));
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logoutUser() {
        loggedIn = false;
    }
}

