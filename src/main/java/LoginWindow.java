import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginWindow extends JPanel implements ActionListener{
    private JLabel username_text,password_text;
    private JTextField jtf_username;
    private JPasswordField jpf_password;
    private JButton btn_login;
    private static final String login_command = "login!";

    private LoginWindow(){
        //setLayout(new FlowLayout());
        //we need some button on login page
        this.setPreferredSize(new Dimension(250, 120));
        username_text = new JLabel("username:");
        jtf_username = new JTextField(10);
        password_text = new JLabel("password:");
        jpf_password = new JPasswordField(10);
        btn_login = new JButton("login");
        btn_login.setActionCommand(login_command);
        this.add(username_text);
        this.add(jtf_username);
        this.add(password_text);
        this.add(jpf_password);
        this.add(btn_login);

        btn_login.addActionListener(this);
    }

    private static void createAndShowGUI(){
        JFrame frame = new JFrame("selling-coffee-system Login window");
        frame.add(new LoginWindow());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private boolean ifLogin(String username, String password) {
        Connection connection = null;
        try {
            //TODO:the line below has some serious issue. fix this line ASAP
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/database/coffee.db");
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM user where username=\'" + username + "\' AND password=\'" + password + "\'";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                //System.out.println("login successful");
                JOptionPane.showMessageDialog(this, "login successful",
                        "welcome back", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }

            //shutdown the connection

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (connection!=null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //return true;
        }
        return false;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (login_command.equals(e.getActionCommand())){
            String username = jtf_username.getText();
            String password = String.valueOf(jpf_password.getPassword());
            if(ifLogin(username,password)){
                //TODO:destroy this page and open the main window. The following 3 lines have not being tested.
                JFrame login_main_frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                //login_main_frame.dispose();
                //TODO: The following lines are not clever enough, very bad code.
                MainWindow mainWindow = new MainWindow();
                JFrame jFrame = new JFrame();
                jFrame.add(mainWindow);
                jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                jFrame.pack();
                jFrame.setLocationRelativeTo(null);
                jFrame.setVisible(true);
            }else {
                JOptionPane.showMessageDialog(this, "login failed",
                        "please check your username or password", JOptionPane.INFORMATION_MESSAGE);
                System.err.println("in LoginWindow.class:Login error");
            }
        }
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
