import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View {
    DefaultListModel listModel = new DefaultListModel();
    DefaultListModel listScorers = new DefaultListModel();
    JList jList = new JList(listModel);
    JList jListR = new JList(listScorers);
    JList jListU = new JList(listModel);
    JFrame jFrame = new JFrame();
    JTabbedPane jTabs = new JTabbedPane();


    JPanel makeCreateTab = new JPanel();
    JButton create = new JButton("CREATE");
    JTextField textFieldusername = new JTextField(20);
    JTextField textFieldpassword = new JTextField(20);


    JButton login = new JButton("LOGIN");
    JPanel makeLoginTab = new JPanel();
    JTextField logTextFieldusername = new JTextField(20);
    JTextField logTextFieldpassword = new JTextField(20);

    JPanel makeLeaderTab = new JPanel();

    JPanel makeBetTab = new JPanel();
    JButton bet = new JButton("BET");
    JTextField textBet = new JTextField(20);

    JButton logout = new JButton("LOGOUT");
    JPanel makeLogoutTab = new JPanel();


    JPanel makeBalanceTab = new JPanel();
    JButton balance = new JButton("ADD FUNDS");
    JTextField textBalanceToAdd = new JTextField(20);




    public JTabbedPane getjTabs() {
        return jTabs;
    }

    public void setjTabs(JTabbedPane jTabs) {
        this.jTabs = jTabs;
    }

    public JPanel getMakeCreateTab() {
        return makeCreateTab;
    }

    public void setMakeCreateTab(JPanel makeCreateTab) {
        this.makeCreateTab = makeCreateTab;
    }

    public JPanel getMakeBalanceTab() {
        return makeBalanceTab;
    }

    public void setMakeBalanceTab(JPanel makeBalanceTab) {
        this.makeBalanceTab = makeBalanceTab;
    }
    public JButton getBalance() {
        return balance;
    }

    public JTextField getTextBalanceToAdd() {
        return textBalanceToAdd;
    }

    public void setTextBalanceToAdd(String text) {
        getTextBalanceToAdd().setText(text);
    }
    public void setMakeBetTab(JPanel makeBetTab) {
        this.makeBetTab = makeBetTab;
    }

    public JPanel getMakeLoginTab() {
        return makeLoginTab;
    }

    public void setMakeLoginTab(JPanel makeLoginTab) {
        this.makeLoginTab = makeLoginTab;
    }

    public JPanel getMakeLeaderTab() {
        return makeLeaderTab;
    }

    public void setMakeDeleteTab(JPanel makeLeaderTab) {
        this.makeLeaderTab = makeLeaderTab;
    }

    public JButton getCreate() {
        return create;
    }

    public void setCreate(JButton create) {
        this.create = create;
    }

    public JButton getBet() {
        return bet;
    }

    public void setBet(JButton bet) {
        this.bet = bet;
    }

    public JButton getLogin() {
        return login;
    }

    public void setLogin(JButton login) {
        this.login = login;
    }

    public JTextField getTextFieldusername() {
        return textFieldusername;
    }

    public void setTextFieldusername(String text) {
        getTextFieldusername().setText(text);
    }

    public JTextField getTextBet() {
        return textBet;
    }

    public void setTextBet(String text) {
        getTextBet().setText(text);
    }

    public JTextField getTextFieldpassword() {
        return textFieldpassword;
    }

    public void setTextFieldpassword(String text) {
        getTextFieldpassword().setText(text);
    }

    public void setLogTextFieldusername(String username) {
        getLogTextFieldusername().setText(username);
    }

    public void setLogTextFieldpassword(String password) {
        getLogTextFieldpassword().setText(password);
    }

    public JTextField getLogTextFieldusername() {
        return logTextFieldusername;
    }

    public JTextField getLogTextFieldpassword() {
        return logTextFieldpassword;
    }

    public JButton getLogout() {
        return logout;
    }


    public void setListModel(ArrayList list) {
        listModel.clear();
        for (int i = 0; i < list.size(); i++) {
            listModel.add(i, list.get(i));
        }
    }

    public void setListScorers(ArrayList list) {
        listScorers.clear();
        for (int i = 0; i < list.size(); i++) {
            listScorers.add(i, list.get(i));
        }
    }

    public JList getjListR() {
        return jListR;
    }

    public JList getjListU() {
        return jListU;
    }

    void go() {
        //Create
        makeCreateTab.setLayout(new GridLayout(3,2));
        makeCreateTab.add(new JLabel("Username:"));
        makeCreateTab.add(getTextFieldusername());
        makeCreateTab.add(new JLabel("Password:"));
        makeCreateTab.add(getTextFieldpassword());
        ClientModel model = new ClientModel();
        Controller controller = new Controller(this, model);
        getCreate().setActionCommand("create");
        makeCreateTab.add(getCreate());
        getCreate().addActionListener(controller);

        //LOGIN
        JPanel inner = new JPanel();
        inner.setLayout(new GridLayout(2,2));
        makeLoginTab.add(getLogTextFieldusername());
        makeLoginTab.add(getLogTextFieldpassword());
        inner.add(new JLabel("Username:"));
        inner.add(logTextFieldusername);
        inner.add(new JLabel("Password:"));
        inner.add(logTextFieldpassword);
        getLogin().setEnabled(true);
        makeLoginTab.add(inner);
        getLogin().setActionCommand("login");
        makeLoginTab.add(getLogin());
        getLogin().addActionListener(controller);

        //leaderboard
        makeLeaderTab.add(getjListR());

        //Bet
        makeBetTab.setLayout(new GridLayout(2,2));
        makeBetTab.add(new JLabel("Ammount:"));
        makeBetTab.add(getTextBet());
        getBet().setEnabled(false);
        getBet().setActionCommand("bet");
        makeBetTab.add(getBet());
        getBet().addActionListener(controller);

        //Account
        makeBalanceTab.setLayout(new GridLayout(2,2));
        makeBalanceTab.add(new JLabel("Ammount:"));
        makeBalanceTab.add(getTextBalanceToAdd());
        getBalance().setEnabled(false);
        getBalance().setActionCommand("addBalance");
        makeBalanceTab.add(getBalance());
        getBalance().addActionListener(controller);

        //logout
        getLogout().setActionCommand("logout");
        makeLogoutTab.add(getLogout());
        getLogout().setEnabled(false);
        getLogout().addActionListener(controller);

        jTabs.addTab("CREATE", makeCreateTab);
        jTabs.addTab("LOGIN", makeLoginTab);
        jTabs.addTab("LEADERBOARD", makeLeaderTab);
        jTabs.add("BET", makeBetTab);
        jTabs.add("LOGOUT", makeLogoutTab);
        jTabs.add("Balance", makeBalanceTab);

        jFrame.add(jTabs);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(400, 400);
        jFrame.setVisible(true);
    }
}

