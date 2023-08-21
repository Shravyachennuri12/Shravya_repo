package GUI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DisplayList extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable savingsTable;
    private JTable studentTable;
    private JTable currentTable;
    private JButton savingsButton;
    private JButton studentButton;
    private JButton currentButton;

    public DisplayList() {
        setTitle("Account List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Create the tables
        savingsTable = new JTable();
        studentTable = new JTable();
        currentTable = new JTable();

        // Add tables to scroll panes
        JScrollPane savingsScrollPane = new JScrollPane(savingsTable);
        savingsScrollPane.setBounds(10, 50, 760, 180);
        contentPane.add(savingsScrollPane);

        JScrollPane studentScrollPane = new JScrollPane(studentTable);
        studentScrollPane.setBounds(10, 240, 760, 180);
        contentPane.add(studentScrollPane);

        JScrollPane currentScrollPane = new JScrollPane(currentTable);
        currentScrollPane.setBounds(10, 430, 760, 180);
        contentPane.add(currentScrollPane);

        // Create buttons for each table
        savingsButton = new JButton("Savings");
        savingsButton.setBounds(10, 10, 100, 30);
        contentPane.add(savingsButton);
        savingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displaySavingsData();
            }
        });

        studentButton = new JButton("Student");
        studentButton.setBounds(120, 10, 100, 30);
        contentPane.add(studentButton);
        studentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayStudentData();
            }
        });

        currentButton = new JButton("Current");
        currentButton.setBounds(230, 10, 100, 30);
        contentPane.add(currentButton);
        currentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayCurrentData();
            }
        });
    }

    // Helper method to convert ResultSet to DefaultTableModel
    private DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            columnNames[columnIndex - 1] = metaData.getColumnLabel(columnIndex);
        }
        Object[][] data = new Object[0][columnCount];
        int rowCount = 0;
        while (resultSet.next()) {
            Object[] rowData = new Object[columnCount];
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                rowData[columnIndex - 1] = resultSet.getObject(columnIndex);
            }
            data = addRowToData(data, rowData);
            rowCount++;
        }
        return new DefaultTableModel(data, columnNames);
    }

    private Object[][] addRowToData(Object[][] data, Object[] rowData) {
        int rowCount = data.length;
        int columnCount = rowData.length;
        Object[][] newData = new Object[rowCount + 1][columnCount];
        System.arraycopy(data, 0, newData, 0, rowCount);
        newData[rowCount] = rowData;
        return newData;
    }

    private void displaySavingsData() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank1", "root", "Admin123");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM savings_accounts");
            DefaultTableModel tableModel = buildTableModel(resultSet);
            savingsTable.setModel(tableModel);
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void displayStudentData() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank1", "root", "Admin123");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM student_accounts");
            DefaultTableModel tableModel = buildTableModel(resultSet);
            studentTable.setModel(tableModel);
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void displayCurrentData() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank1", "root", "Admin123");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM current_accounts");
            DefaultTableModel tableModel = buildTableModel(resultSet);
            currentTable.setModel(tableModel);
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DisplayList frame = new DisplayList();
        frame.setVisible(true);
    }
}
