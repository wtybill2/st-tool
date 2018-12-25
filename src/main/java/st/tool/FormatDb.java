// package st.tool;
//
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.ResultSetMetaData;
// import java.sql.SQLException;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
//
// public class FormatDb {
//
// private String driver = null;
// private String url = null;
// private String user = null;
// private String pass = null;
// private String sql = null;
// private Connection conn = null;
// private PreparedStatement pst = null;
// private ResultSet rs = null;
//
// private FormatDb() {
// }
//
// public static FormatDb getInstance(String driver, String url, String user,
// String pass) {
// FormatDb db = new FormatDb();
// db.driver = driver;
// db.url = url;
// db.user = user;
// db.pass = pass;
// db.init();
// db.setAutoCommit(false);
// return db;
// }
//
// public static FormatDb getInstance(String url, String user, String pass) {
// return getInstance(Constant.JDBC_DRIVER, url, user, pass);
// }
//
// public static FormatDb getInstance() {
// return getInstance(Constant.JDBC_URL, Constant.JDBC_USER,
// Constant.JDBC_PASS);
// }
//
// private void init() {
// try {
// Class.forName(driver);// 指定连接类型
// conn = DriverManager.getConnection(url, user, pass);// 获取连接
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
//
// public void setAutoCommit(boolean autoCommit) {
// try {
// conn.setAutoCommit(autoCommit);
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
//
// public void setSql(String sql) {
// try {
// pst = conn.prepareStatement(sql);// 准备执行语句
// } catch (SQLException e) {
// e.printStackTrace();
// }
// }
//
// public void close() {
// try {
// if (rs != null) {
// rs.close();
// }
// if (pst != null) {
// pst.close();
// }
// if (conn != null) {
// conn.close();
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }
// }
//
// private static final char sp = ',';
//
// public static String select_sql(String table, List<String> cols, String
// where) {
// if (FormatEmpty.isEmpty(table)) {
// return null;
// }
// StringBuffer sql_cols = new StringBuffer();
// if (FormatEmpty.isEmpty(cols)) {
// sql_cols.append("* ");
// } else {
// for (String s : cols) {
// sql_cols.append(s).append(sp);
// }
// }
// StringBuffer sql = new StringBuffer();
// sql.append("select ").append(sql_cols.substring(0, sql_cols.length() -
// 1)).append(" from ").append(table);
// if (!FormatEmpty.isEmpty(where)) {
// sql.append(' ').append(where);
// }
// return sql.toString();
// }
//
// public List<Map<String, Object>> select(String table, List<String> cols,
// String where) {
// sql = select_sql(table, cols, where);
// return select(sql);
// }
//
// public List<Map<String, Object>> select(String sql) {
// setSql(sql);
// List<Map<String, Object>> list = new ArrayList<>();
// try {
// ResultSet rs = pst.executeQuery();
// ResultSetMetaData md = rs.getMetaData();
// int colCount = md.getColumnCount();
// Map<String, Object> map = null;
// while (rs.next()) {
// map = new HashMap<>();
// for (int k = 1; k <= colCount; k++) {
// map.put(md.getColumnName(k), rs.getObject(k));
// }
// list.add(map);
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }
// close();
// return list;
// }
//
// public static String insert_sql(String table, List<String> cols) {
// if (FormatEmpty.isEmpty(table) || FormatEmpty.isEmpty(cols)) {
// return null;
// }
// StringBuffer sql_cols = new StringBuffer();
// StringBuffer sql_vals = new StringBuffer();
// for (String s : cols) {
// sql_cols.append(s).append(sp);
// sql_vals.append('?').append(sp);
// }
// StringBuffer sql = new StringBuffer();
// sql.append("insert into
// ").append(table).append('(').append(sql_cols.substring(0, sql_cols.length() -
// 1))
// .append(") values(").append(sql_vals.substring(0, sql_vals.length() -
// 1)).append(");");
// return sql.toString();
// }
//
// public int insert1(String table, List<String> cols, List<?> vals) {
// sql = insert_sql(table, cols);
// int[] result = insert(sql, Arrays.asList(vals));
// return result == null ? -1 : result[0];
// }
//
// public int[] insert(String table, List<String> cols, List<List<?>> vals) {
// sql = insert_sql(table, cols);
// return insert(sql, vals);
// }
//
// public int[] insert(String sql, List<List<?>> vals) {
// setSql(sql);
// int[] result = null;
// try {
// for (List<?> val : vals) {
// int k = 1;
// for (Object v : val) {
// pst.setObject(k++, v);
// }
// pst.addBatch();
// }
// result = pst.executeBatch();
// conn.commit();
// } catch (Exception e) {
// e.printStackTrace();
// }
// close();
// return result;
// }
//
// public static String update_sql(String table, List<String> cols, String
// where) {
// if (FormatEmpty.isEmpty(table) || FormatEmpty.isEmpty(cols)) {
// return null;
// }
// StringBuffer sql_cols = new StringBuffer();
// for (String s : cols) {
// sql_cols.append(s).append("=?").append(sp);
// }
// StringBuffer sql = new StringBuffer();
// sql.append("update ").append(table).append(" set
// ").append(sql_cols.substring(0, sql_cols.length() - 1));
// if (!FormatEmpty.isEmpty(where)) {
// sql.append(' ').append(where);
// }
// return sql.toString();
// }
//
// public int update(String table, List<String> cols, String where, List<?>
// vals) {
// sql = update_sql(table, cols, where);
// return update(sql, vals);
// }
//
// public int update(String sql, List<?> vals) {
// setSql(sql);
// int result = -1;
// try {
// int k = 1;
// for (Object v : vals) {
// pst.setObject(k++, v);
// }
// result = pst.executeUpdate();
// conn.commit();
// } catch (Exception e) {
// e.printStackTrace();
// }
// close();
// return result;
// }
//
// public static String delete_sql(String table, String where) {
// if (FormatEmpty.isEmpty(table) || FormatEmpty.isEmpty(where)) {
// return null;
// }
// StringBuffer sql = new StringBuffer();
// sql.append(" delete from ").append(table).append(' ').append(where);
// return sql.toString();
// }
//
// public int delete(String table, String where) {
// sql = delete_sql(table, where);
// return delete(sql);
// }
//
// public int delete(String sql) {
// setSql(sql);
// int result = -1;
// try {
// result = pst.executeUpdate();
// conn.commit();
// } catch (Exception e) {
// e.printStackTrace();
// }
// close();
// return result;
// }
//
// }
