package st.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class FormatJDBC {
    private FormatJDBC() {
    }

    private static String url;
    private static String user;
    private static String password;

    static {
        ResourceBundle rb = ResourceBundle.getBundle("jdbc");
        url = rb.getString("url");
        user = rb.getString("user");
        password = rb.getString("pass");
        try {
            // 1、加载驱动：Class.forName("");
            Class.forName(rb.getString("driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        // 2、获得链接对象Connection
        return DriverManager.getConnection(url, user, password);
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
        // 6、关闭连接
        if (resultSet != null) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    public static void close(Connection connection, Statement statement) throws SQLException {
        // 6、关闭连接
        close(connection, statement, null);
    }

    public static void main(String[] args) throws SQLException {
        // ResourceBundle rb = ResourceBundle.getBundle("j1803.t0409.jdbc");
        // System.out.println(rb.getString("driver"));
        Connection con = FormatJDBC.getConnection();
        FormatJDBC.close(con, null);
    }

}
