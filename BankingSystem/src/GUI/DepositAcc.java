package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Exceptions.AccNotFound;
import Exceptions.InvalidAmount;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class DepositAcc extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;

    // Database connection variables
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/Bank1";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Admin123";

    public DepositAcc() {
        setTitle("Deposit To Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.activeCaption);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblDepositToAccount = new JLabel("Deposit To Account");
        lblDepositToAccount.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblDepositToAccount.setHorizontalAlignment(SwingConstants.CENTER);
        lblDepositToAccount.setBounds(10, 11, 414, 36);
        contentPane.add(lblDepositToAccount);

        JLabel lblName = new JLabel("Account Number:");
        lblName.setHorizontalAlignment(SwingConstants.RIGHT);
        lblName.setBounds(0, 86, 111, 14);
        contentPane.add(lblName);

        textField = new JTextField();
        textField.setBounds(121, 83, 211, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(121, 147, 211, 20);
        contentPane.add(textField_1);

        JLabel lblAmount = new JLabel("Amount:");
        lblAmount.setHorizontalAlignment(SwingConstants.RIGHT);
        lblAmount.setBounds(0, 150, 111, 14);
        contentPane.add(lblAmount);

        JButton btnFind = new JButton("Find");
        btnFind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accountNum = textField.getText();

                try {
                    // Connect to the database
                    Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

                    // Check the table of the provided account number and retrieve the account holder's name
                    String table = getTable(accountNum);
                    if (table != null) {
                        // Retrieve the account holder's name from the respective table
                        String nameQuery = "SELECT name FROM " + table + " WHERE account_number = ?";
                        PreparedStatement nameStatement = conn.prepareStatement(nameQuery);
                        nameStatement.setString(1, accountNum);
                        ResultSet result = nameStatement.executeQuery();

                        if (result.next()) {
                            String accountHolderName = result.getString("name");
                            JOptionPane.showMessageDialog(getComponent(0), "Account Holder: " + accountHolderName);
                        } else {
                            JOptionPane.showMessageDialog(getComponent(0), "Account Not Found");
                        }
                    } else {
                        JOptionPane.showMessageDialog(getComponent(0), "Invalid Account Number");
                    }

                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(getComponent(0), "An error occurred");
                }
            }
        });
        btnFind.setBounds(175, 115, 89, 20);
        contentPane.add(btnFind);

        JButton btnDeposit = new JButton("Deposit");
        btnDeposit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accountNum = textField.getText();
                double amount = Double.parseDouble(textField_1.getText());

                try {
                    // Connect to the database
                    Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

                    // Check the table of the provided account number and perform the deposit
                    String table = getTable(accountNum);
                    if (table != null) {
                        // Perform the deposit in the respective table
                        String depositQuery = "UPDATE " + table + " SET balance = balance + ? WHERE account_number = ?";
                        PreparedStatement depositStatement = conn.prepareStatement(depositQuery);
                        depositStatement.setDouble(1, amount);
                        depositStatement.setString(2, accountNum);

                        int rowsAffected = depositStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(getComponent(0), "Deposit Successful");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(getComponent(0), "Account Not Found");
                        }
                    } else {
                        JOptionPane.showMessageDialog(getComponent(0), "Invalid Account Number");
                    }

                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(getComponent(0), "An error occurred");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(getComponent(0), "Invalid Amount");
                }
            }
        });
        btnDeposit.setBounds(73, 212, 89, 23);
        contentPane.add(btnDeposit);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(243, 212, 89, 23);
        contentPane.add(btnReset);

        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textField.setText(null);
                textField_1.setText(null);
            }
        });
    }

    private String getTable(String accountNum) {
        String table = null;

        try {
            Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM savings_accounts WHERE account_number = ?");
            statement.setString(1, accountNum);
            if (statement.executeQuery().next()) {
                table = "savings_accounts";
            } else {
                statement = conn.prepareStatement("SELECT * FROM student_accounts WHERE account_number = ?");
                statement.setString(1, accountNum);
                if (statement.executeQuery().next()) {
                    table = "student_accounts";
                } else {
                    statement = conn.prepareStatement("SELECT * FROM current_accounts WHERE account_number = ?");
                    statement.setString(1, accountNum);
                    if (statement.executeQuery().next()) {
                        table = "current_accounts";
                    }
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return table;
    }
}
