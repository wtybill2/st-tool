package st.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class FormatSpringJDBC {

    public String            url, user, pass;
    private JdbcTemplate     jdbcTemplate     = null;
    private SimpleJdbcInsert simpleJdbcInsert = null;

    public FormatSpringJDBC() {
        url = Constant.JDBC_URL;
        user = Constant.JDBC_USER;
        pass = Constant.JDBC_PASS;
        jdbcTemplate = new JdbcTemplate(getDS());
    }

    public FormatSpringJDBC(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        jdbcTemplate = new JdbcTemplate(getDS());
    }

    public DriverManagerDataSource getDS() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Constant.JDBC_DRIVER);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(pass);
        return dataSource;
    }

    public static String select_sql(String table, List<String> cols, String where) {
        if (FormatEmpty.isEmpty(table)) {
            return null;
        }
        StringBuffer sql_cols = new StringBuffer();
        if (FormatEmpty.isEmpty(cols)) {
            sql_cols.append("* ");
        } else {
            for (String s : cols) {
                sql_cols.append(s).append(',');
            }
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select ").append(sql_cols.substring(0, sql_cols.length() - 1)).append(" from ").append(table);
        if (!FormatEmpty.isEmpty(where)) {
            sql.append(' ').append(where);
        }
        return sql.toString();
    }

    public List<Map<String, Object>> select(String table, List<String> cols, String where) {
        return select(select_sql(table, cols, where));
    }

    public List<Map<String, Object>> select(String sql) {
        System.out.println(sql);
        return jdbcTemplate.queryForList(sql);
    }

    public int insert1(String table, Map<String, Object> m) {
        @SuppressWarnings("unchecked")
        int[] res = insert(table, new Map[] { m });
        return res == null ? -1 : res[0];
    }

    public int[] insert(String table, Map<String, Object>[] list) {
        if (FormatEmpty.isEmpty(table) || FormatEmpty.isEmpty(list)) {
            return null;
        }
        simpleJdbcInsert = new SimpleJdbcInsert(getDS());
        simpleJdbcInsert.withTableName(table).setGeneratedKeyName("id");
        return simpleJdbcInsert.executeBatch(list);
    }

    public static String update_sql(String table, List<String> cols, String where) {
        if (FormatEmpty.isEmpty(table) || FormatEmpty.isEmpty(cols) || FormatEmpty.isEmpty(where)) {
            return null;
        }
        StringBuffer sql_cols = new StringBuffer();
        for (String s : cols) {
            sql_cols.append(s).append("=?").append(',');
        }
        StringBuffer sql = new StringBuffer();
        sql.append("update ").append(table).append(" set ").append(sql_cols.substring(0, sql_cols.length() - 1))
                .append(' ').append(where);
        return sql.toString();
    }

    public int[] update(String table, List<String> cols, String where, List<List<Object>> vals) {
        if (FormatEmpty.isEmpty(table) || FormatEmpty.isEmpty(cols) || FormatEmpty.isEmpty(where)
                || FormatEmpty.isEmpty(vals)) {
            return null;
        }
        String sql = update_sql(table, cols, where);
        List<Object[]> parameters = new ArrayList<>();
        for (List<Object> objs : vals) {
            parameters.add(objs.toArray());
        }
        System.out.println(sql);
        return jdbcTemplate.batchUpdate(sql, parameters);
    }

    public int update1(String table, List<String> cols, String where, List<Object> vals) {
        int[] res = update(table, cols, where, Arrays.asList(vals));
        return res == null ? -1 : res[0];
    }

    public static String delete_sql(String table, String where) {
        if (FormatEmpty.isEmpty(table) || FormatEmpty.isEmpty(where)) {
            return null;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from ").append(table).append(' ').append(where);
        System.out.println(sql);
        return sql.toString();
    }

    public int delete(String table, String where) {
        return jdbcTemplate.update(delete_sql(table, where));
    }

}

// class FormatDataSource {
//
// public String url, user, pass;
//
// public FormatDataSource() {
// url = Constant.JDBC_URL;
// user = Constant.JDBC_USER;
// pass = Constant.JDBC_PASS;
// }
//
// public FormatDataSource(String url, String user, String pass) {
// this.url = url;
// this.user = user;
// this.pass = pass;
// }
//
// public DriverManagerDataSource getDataSource() {
// DriverManagerDataSource dataSource = new DriverManagerDataSource();
// dataSource.setDriverClassName(Constant.JDBC_DRIVER);
// dataSource.setUrl(url);
// dataSource.setUsername(user);
// dataSource.setPassword(pass);
// return dataSource;
// }
//
// }