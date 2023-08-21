package GUI;

import javax.swing.JFrame;
import Data.FileIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCurrentAccount extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;

    public AddCurrentAccount() {
        setTitle("Add Current Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.activeCaption);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblAddCurrentAccount = new JLabel("Add Current Account ");
        lblAddCurrentAccount.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblAddCurrentAccount.setHorizontalAlignment(SwingConstants.CENTER);
        lblAddCurrentAccount.setBounds(10, 11, 414, 34);
        contentPane.add(lblAddCurrentAccount);

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblName.setBounds(10, 72, 124, 14);
        contentPane.add(lblName);

        textField = new JTextField();
        textField.setBounds(144, 69, 254, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblBalance = new JLabel("Balance:");
        lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblBalance.setBounds(10, 118, 124, 14);
        contentPane.add(lblBalance);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(144, 115, 254, 20);
        contentPane.add(textField_1);

        JLabel lblMaximumWithdrawLimit = new JLabel("Trade Licence Number:");
        lblMaximumWithdrawLimit.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblMaximumWithdrawLimit.setBounds(10, 163, 135, 14);
        contentPane.add(lblMaximumWithdrawLimit);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(144, 160, 254, 20);
        contentPane.add(textField_2);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = textField.getText();
                double bal = Double.parseDouble(textField_1.getText());
                String trlic = textField_2.getText();

                if (bal < 5000) {
                    JOptionPane.showMessageDialog(getComponent(0), "Minimum Limit 5000", "Warning", 0);
                    textField.setText(null);
                    textField_1.setText(null);
                    textField_2.setText(null);
                } else {
                    if (name == null || bal <= 0 || trlic == null) {
                        JOptionPane.showMessageDialog(getComponent(0), "Typing Mismatch!! Try Again");
                        textField.setText(null);
                        textField_1.setText(null);
                        textField_2.setText(null);
                    } else {
                        int ch = JOptionPane.showConfirmDialog(getComponent(0), "Confirm?");
                        if (ch == 0) {
                            // Generate a unique account number
                            String accountNumber = generateAccountNumber();

                            // Store the data in the SQL database
                            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank1", "root", "Admin123")) {
                                String sql = "INSERT INTO current_accounts (account_number, name, balance, trade_license) VALUES (?, ?, ?, ?)";
                                PreparedStatement statement = connection.prepareStatement(sql);
                                statement.setString(1, accountNumber);
                                statement.setString(2, name);
                                statement.setDouble(3, bal);
                                statement.setString(4, trlic);
                                statement.executeUpdate();
                                JOptionPane.showMessageDialog(getComponent(0), "Success");
                                dispose();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(getComponent(0), "Failed");
                                textField.setText(null);
                                textField_1.setText(null);
                                textField_2.setText(null);
                            }
                        }
                    }
                }
            }
        });
        btnAdd.setBounds(86, 209, 89, 23);
        contentPane.add(btnAdd);

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textField.setText(null);
                textField_1.setText(null);
                textField_2.setText(null);
            }
        });
        btnReset.setBounds(309, 209, 89, 23);
        contentPane.add(btnReset);
    }

    // Method to generate a unique account number
    private String generateAccountNumber() {
        // Implement your logic to generate a unique account number here
        // You can use a combination of timestamp, random numbers, or any other approach
        // Make sure the generated number is unique in your system
        // Return the generated account number as a string

        // Example implementation using timestamp and random numbers
    	return String.valueOf((int) (Math.random() * 1000000));
    }

    public static void main(String[] args) {
        AddCurrentAccount frame = new AddCurrentAccount();
        frame.setVisible(true);
    }
}
