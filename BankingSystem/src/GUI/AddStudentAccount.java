package GUI;
import javax.swing.JFrame;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddStudentAccount extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;

    public AddStudentAccount() {
        setTitle("Add Student Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblAddCurrentAccount = new JLabel("Add Student Account ");
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

        JLabel lblMaximumWithdrawLimit = new JLabel("Institution Name:");
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
                String insname = textField_2.getText();

                if (bal < 100) {
                    JOptionPane.showMessageDialog(getComponent(0), "Minimum Limit 5000", "Warning", 0);
                    textField.setText(null);
                    textField_1.setText(null);
                    textField_2.setText(null);
                } else {
                    if (name == null || bal <= 0 || insname == null) {
                        JOptionPane.showMessageDialog(getComponent(0), "Typing Mismatch!! Try Again");
                        textField.setText(null);
                        textField_1.setText(null);
                        textField_2.setText(null);
                    } else {
                        try {
                            // Establish a database connection
                            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank1", "root", "Admin123");

                            // Generate a unique account number
                            String accountNumber = generateAccountNumber();

                            // Prepare the SQL insert statement
                            String query = "INSERT INTO student_accounts (account_number, name, balance, institution_name) VALUES (?, ?, ?, ?)";
                            PreparedStatement pstmt = conn.prepareStatement(query);
                            pstmt.setString(1, accountNumber);
                            pstmt.setString(2, name);
                            pstmt.setDouble(3, bal);
                            pstmt.setString(4, insname);

                            // Execute the SQL insert statement
                            int rowsAffected = pstmt.executeUpdate();

                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(getComponent(0), "Added Successfully");
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(getComponent(0), "Failed to add student account");
                                textField.setText(null);
                                textField_1.setText(null);
                                textField_2.setText(null);
                            }

                            // Close the statement and connection
                            pstmt.close();
                            conn.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
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

    private String generateAccountNumber() {
        // Generate a unique account number logic goes here
        // You can use UUID or any other logic to generate a unique account number
        // For simplicity, let's generate a random number as the account number
        return String.valueOf((int) (Math.random() * 1000000));
    }
}
