package st.tool;

import java.util.ResourceBundle;

public class Constant {

    private static final ResourceBundle res2 = ResourceBundle.getBundle("jdbc");

    public final static String JDBC_DRIVER = res2.getString("jdbc.driver");
    public final static String JDBC_URL    = res2.getString("jdbc.url");
    public final static String JDBC_USER   = res2.getString("jdbc.user");
    public final static String JDBC_PASS   = res2.getString("jdbc.pass");

}