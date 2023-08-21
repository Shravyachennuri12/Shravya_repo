package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Exceptions.AccNotFound;
import Exceptions.InvalidAmount;
import Exceptions.MaxBalance;
import Exceptions.MaxWithdraw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class WithdrawAcc extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JLabel lblAccountName;
    private JLabel lblAccountBalance;
    private Connection connection;

    private static final String DB_URL = "jdbc:mysql://localhost/Bank1";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Admin123";

    public WithdrawAcc() {
        setTitle("Withdraw From Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 400);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.activeCaption);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblWithdrawFromAccount = new JLabel("Withdraw From Account");
        lblWithdrawFromAccount.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblWithdrawFromAccount.setHorizontalAlignment(SwingConstants.CENTER);
        lblWithdrawFromAccount.setBounds(10, 11, 414, 36);
        contentPane.add(lblWithdrawFromAccount);

        JLabel lblAccountNumber = new JLabel("Account Number:");
        lblAccountNumber.setHorizontalAlignment(SwingConstants.RIGHT);
        lblAccountNumber.setBounds(0, 86, 106, 14);
        contentPane.add(lblAccountNumber);

        textField = new JTextField();
        textField.setBounds(116, 83, 216, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        lblAccountName = new JLabel("Account Name: ");
        lblAccountName.setBounds(10, 120, 350, 14);
        contentPane.add(lblAccountName);

        lblAccountBalance = new JLabel("Account Balance: ");
        lblAccountBalance.setBounds(10, 145, 350, 14);
        contentPane.add(lblAccountBalance);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(116, 170, 216, 20);
        contentPane.add(textField_1);

        JLabel lblAmount = new JLabel("Amount:");
        lblAmount.setHorizontalAlignment(SwingConstants.RIGHT);
        lblAmount.setBounds(10, 173, 96, 14);
        contentPane.add(lblAmount);

        JButton btnFind = new JButton("Find");
        btnFind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accountNum = textField.getText();

                try {
                    connectToDatabase();
                    findAccount(accountNum);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(getComponent(0), "An error occurred: " + ex.getMessage());
                } catch (AccNotFound ex) {
                    JOptionPane.showMessageDialog(getComponent(0), "Sorry! Account Not Found");
                    resetAccountInfo();
                }
            }
        });
        btnFind.setBounds(342, 82, 82, 23);
        contentPane.add(btnFind);

        JButton btnWithdraw = new JButton("Withdraw");
        btnWithdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accountNum = textField.getText();
                double amount = Double.parseDouble(textField_1.getText());

                try {
                    int confirm = JOptionPane.showConfirmDialog(getComponent(0), "Confirm withdrawal?");
                    if (confirm == 0) {
                        withdrawFromAccount(accountNum, amount);
                        JOptionPane.showMessageDialog(getComponent(0), "Withdrawal Successful");
                        resetAccountInfo();
                    } else {
                        textField_1.setText(null);
                    }
                } catch (MaxBalance ex) {
                    JOptionPane.showMessageDialog(getComponent(0), "Insufficient Balance");
                    textField_1.setText(null);
                } catch (AccNotFound ex) {
                    JOptionPane.showMessageDialog(getComponent(0), "Sorry! Account Not Found");
                    resetAccountInfo();
                } catch (MaxWithdraw ex) {
                    JOptionPane.showMessageDialog(getComponent(0), "Maximum Withdraw Limit Exceeded");
                    textField_1.setText(null);
                } catch (InvalidAmount ex) {
                    JOptionPane.showMessageDialog(getComponent(0), "Invalid Amount");
                    textField_1.setText(null);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(getComponent(0), "An error occurred: " + ex.getMessage());
                }
            }
        });
        btnWithdraw.setBounds(73, 212, 89, 23);
        contentPane.add(btnWithdraw);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(243, 212, 89, 23);
        contentPane.add(btnReset);
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetAccountInfo();
                textField_1.setText(null);
            }
        });
    }

    private void connectToDatabase() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    private void disconnectFromDatabase() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void findAccount(String accountNum) throws SQLException, AccNotFound {
        String query = "SELECT name, balance FROM savings_accounts WHERE account_number = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, accountNum);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            double balance = resultSet.getDouble("balance");
            lblAccountName.setText("Account Name: " + name);
            lblAccountBalance.setText("Account Balance: " + balance);
        } else {
            query = "SELECT name, balance FROM student_accounts WHERE account_number = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, accountNum);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                double balance = resultSet.getDouble("balance");
                lblAccountName.setText("Account Name: " + name);
                lblAccountBalance.setText("Account Balance: " + balance);
            } else {
                query = "SELECT name, balance FROM current_accounts WHERE account_number = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, accountNum);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    double balance = resultSet.getDouble("balance");
                    lblAccountName.setText("Account Name: " + name);
                    lblAccountBalance.setText("Account Balance: " + balance);
                } else {
                    throw new AccNotFound();
                }
            }
        }
    }

    private void withdrawFromAccount(String accountNum, double amount)
            throws SQLException, AccNotFound, MaxBalance, MaxWithdraw, InvalidAmount {
        String query = "UPDATE savings_accounts SET balance = balance - ? WHERE account_number = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDouble(1, amount);
        statement.setString(2, accountNum);
        int rowsAffected = statement.executeUpdate();

        if (rowsAffected == 0) {
            query = "UPDATE student_accounts SET balance = balance - ? WHERE account_number = ?";
            statement = connection.prepareStatement(query);
            statement.setDouble(1, amount);
            statement.setString(2, accountNum);
            rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                query = "UPDATE current_accounts SET balance = balance - ? WHERE account_number = ?";
                statement = connection.prepareStatement(query);
                statement.setDouble(1, amount);
                statement.setString(2, accountNum);
                rowsAffected = statement.executeUpdate();

                if (rowsAffected == 0) {
                    throw new AccNotFound();
                }
            }
        }
    }

    private void resetAccountInfo() {
        lblAccountName.setText("Account Name:");
        lblAccountBalance.setText("Account Balance:");
    }

    public static void main(String[] args) {
        WithdrawAcc frame = new WithdrawAcc();
        frame.setVisible(true);
    }
}
