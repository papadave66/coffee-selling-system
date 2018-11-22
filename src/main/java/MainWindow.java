import model.coffee;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/*
WARNING! THIS PAGE CODE HAS SOME ISSUE THAT NOT COMPLETED YET.
you should select one coffee first to get normal.
directly click add to cart and purchase will be buggy.
it will be fixed if the I have time
 */
public class MainWindow extends JPanel implements ListSelectionListener,ActionListener {
    //the list to storage coffee data
     private List<coffee> coffees = new ArrayList<>();
     private JTextArea jta_coffeeInfo;
     private JLabel jLabel_balance;
     JList<String> jList_shoppingCart;
     DefaultListModel<String> defaultListModel_shoppingcart;
     String coffee_name;
     int balance ,total_price = 0;

    MainWindow(){
        //load the data from database to List
        loadDataToList();

        JList<String> coffee_name;
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (model.coffee coffee : coffees) {
            listModel.addElement(coffee.getGname());
        }
        coffee_name = new JList<>(listModel);
        coffee_name.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coffee_name.setSelectedIndex(0);
        coffee_name.addListSelectionListener(this);
        JScrollPane listScrollPane = new JScrollPane(coffee_name);
        this.add(listScrollPane);

        //TODO: let JTextArea in middle shows coffee info.
        jta_coffeeInfo = new JTextArea();
        jta_coffeeInfo.setEditable(false);
        //in default , it shows the first coffee info.until you select other's
        jta_coffeeInfo.setText(coffees.get(0).getGinfo());
        this.add(jta_coffeeInfo);


        defaultListModel_shoppingcart = new DefaultListModel<String>();
        jList_shoppingCart = new JList<String>(defaultListModel_shoppingcart);
        JScrollPane scrollPaneshoppingcart = new JScrollPane(jList_shoppingCart);
        this.add(scrollPaneshoppingcart);

        JButton jbt_addToCart;
        jbt_addToCart = new JButton("ADD TO CART");
        jbt_addToCart.setActionCommand("ADD TO CART");
        jbt_addToCart.addActionListener(this);
        this.add(jbt_addToCart);

        JLabel text_balance = new JLabel("balance:");
        this.add(text_balance);
        jLabel_balance = new JLabel();
        this.add(jLabel_balance);


        JButton btn_purchase = new JButton("purchase");
        btn_purchase.addActionListener(this);
        btn_purchase.setActionCommand("purchase");
        this.add(btn_purchase);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        //TODO the info will be changed when you select other coffee
        JList list = (JList)e.getSource();
        jta_coffeeInfo.setText(coffees.get(list.getSelectedIndex()).getGinfo());
        coffee_name = coffees.get(list.getSelectedIndex()).getGname();
        balance = coffees.get(list.getSelectedIndex()).getGprice();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "ADD TO CART":
                //System.out.println("button ADD TO CART pressed");
                //TODO: "add to cart" pressed, then we should do plenty of things
                //TODO: like calculate the balance of coffees , change the list you have chosen
                //add coffee into your shopping list
                defaultListModel_shoppingcart.insertElementAt(coffee_name,0);
                total_price += balance;
                jLabel_balance.setText(String.valueOf(total_price));
                break;
            case "purchase":
                //TODO: throw a success window and clean all the shopping cart and balance.
                JOptionPane.showMessageDialog(this,"you have purchased"+total_price,
                        "thank you",JOptionPane.INFORMATION_MESSAGE);
                defaultListModel_shoppingcart.clear();
                total_price = 0;
                break;

        }

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
