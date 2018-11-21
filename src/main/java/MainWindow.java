import model.coffee;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JPanel implements ListSelectionListener,ActionListener {
    //the list to storage coffee data
     private List<coffee> coffees = new ArrayList<>();
     private List coffee_name;
     private JTextArea jta_coffeeInfo;
     private JList jList_shoppingCart;
     private JLabel jLabel_balance;
     private JButton jbt_addToCart;

    MainWindow(){
        //load the data from database to List
        loadDataToList();
        JList<String> jList;
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (model.coffee coffee : coffees) {
            listModel.addElement(coffee.getGname());
        }
        jList = new JList<>(listModel);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setSelectedIndex(0);
        jList.addListSelectionListener(this);
        JScrollPane listScrollPane = new JScrollPane(jList);
        this.add(listScrollPane);

        //TODO: let JTextArea in middle shows coffee info.
        jta_coffeeInfo = new JTextArea();
        jta_coffeeInfo.setEditable(false);
        //in default , it shows the first coffee info.until you select other's
        jta_coffeeInfo.setText(coffees.get(0).getGinfo());
        this.add(jta_coffeeInfo);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        //TODO the info will be changed when you select other coffee
        JList list = (JList)e.getSource();
        jta_coffeeInfo.setText(coffees.get(list.getSelectedIndex()).getGinfo());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void loadDataToList() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/database/coffee.db");
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM coffee";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                coffee coffee = new coffee();
                coffee.setGaddr(resultSet.getString("addr"));
                coffee.setGinfo(resultSet.getString("info"));
                coffee.setGname(resultSet.getString("name"));
                coffee.setGprice(resultSet.getInt("price"));
                coffee.setGtype(resultSet.getString("type"));
                //add this coffee into coffees list
                coffees.add(coffee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createAndShowGUI(){
        JFrame frame = new JFrame("selling-coffee-system Main window");
        frame.add(new MainWindow());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(300,300);
        frame.pack();
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
