import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener, ChangeListener{
    private View view;
    private ClientModel model;

    public Controller(View view, ClientModel model) {
        this.view = view;
        this.model = model;
    }

    public Controller(View view) {
        this.view = view;
        view.go();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "create") {
            String strName = view.getTextFieldusername().getText();
            String strPass = view.getTextFieldpassword().getText();
            if(strPass != "" && strName != "") {
                view.setTextFieldpassword("");
                view.setTextFieldusername("");
                model.sendCreate(strName, strPass);
            }
            update();
        }
        if (e.getActionCommand() == "login") {
            try {
                String strName = view.getLogTextFieldusername().getText();
                String strPass = view.getLogTextFieldpassword().getText();
                if (model.sendLogin(strName, strPass)){
                    view.getBet().setEnabled(true);
                    view.getLogout().setEnabled(true);
                    view.getBalance().setEnabled(true);
                    view.getLogin().setEnabled(false);
                    view.getCreate().setEnabled(false);
                }else{
                    view.getTextBet().setEnabled(false);
                    view.getLogout().setEnabled(false);
                    view.getBalance().setEnabled(false);
                    view.getLogin().setEnabled(true);
                    view.getCreate().setEnabled(true);
                }
            } catch (Throwable i) {
                System.out.println("Can not use current inputs");
            }
            view.setLogTextFieldpassword("");
            view.setLogTextFieldusername("");
            update();
        }
        if (e.getActionCommand() == "logout") {
            model.sendLogout();
            view.getTextBet().setEnabled(false);
            view.getLogout().setEnabled(false);
            view.getLogin().setEnabled(true);
            view.getCreate().setEnabled(true);
            update();
        }

        if (e.getActionCommand() == "bet") {
            String betText = view.getTextBet().getText();
            String[] parts = betText.split(" ");
            if (parts.length > 1) {
                int earned = model.sendBet(parts[0], parts[1]);
                if (earned == -20) {
                    System.out.println("Insufficient Funds");
                }
                if (earned == 0) {
                    JLabel loserLabel = new JLabel("LOSER");
                    view.makeBetTab.add(loserLabel, 3); // change index to 3
                    view.makeBetTab.revalidate();
                    view.makeBetTab.repaint();
                    int count = view.makeBetTab.getComponentCount();
                    if (count >= 4) {
                        view.makeBetTab.remove(4);
                    }
                } else {
                    String s = String.format("WINNER! %d", earned);
                    JLabel winnerLabel = new JLabel(s);
                    view.makeBetTab.add(winnerLabel, 3); // change index to 3
                    view.makeBetTab.revalidate();
                    view.makeBetTab.repaint();
                    int count = view.makeBetTab.getComponentCount();
                    if (count >= 4) {
                        view.makeBetTab.remove(4);
                    }
                }

            }
            update();
        }


        if(e.getActionCommand() == "addBalance"){
            String balance = view.getTextBalanceToAdd().getText();
            int balanceToAdd = Integer.parseInt(balance);
            if (balanceToAdd > 0){
                model.sendBalance(balance);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        update();
    }

    void update(){
        view.setListScorers(model.sendScorerRequest());
    }
}
