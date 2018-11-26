import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MessageWindow extends JPanel implements ActionListener {
    JTextField jtf_username;
    JEditorPane jEditorPane;
    public MessageWindow() {
        this.setPreferredSize(new Dimension(350, 200));
        jtf_username = new JTextField();
        jtf_username.setColumns(10);
        this.add(jtf_username);
        jEditorPane = new JEditorPane();
        jEditorPane.setText("please leave the message in here");
        jEditorPane.setSize(new Dimension(300,300));
        this.add(jEditorPane);

        JButton jButton = new JButton("SUBMIT");
        jButton.addActionListener(this);
        jButton.setActionCommand("submit");
        this.add(jButton);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "submit":
                Connection connection = null;
                try {
                    connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/database/coffee.db");
                    Statement statement = connection.createStatement();
                    String sql = "insert into message values (\'"+jtf_username.getText()+"\',\'"+jEditorPane.getText()+"\')";
                    statement.executeUpdate(sql);
                        JOptionPane.showMessageDialog(this, "leave the message successful",
                                "message received", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                try {
                    assert connection != null;
                    connection.close();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
                break;
        }

    }

    private static void createAndShowGUI(){
        JFrame frame = new JFrame("selling-coffee-system Message window");
        frame.add(new MessageWindow());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(300,300);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public static void main(String[]args){
        //Schedule a job for the event dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
