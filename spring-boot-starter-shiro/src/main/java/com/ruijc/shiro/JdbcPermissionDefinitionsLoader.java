package com.ruijc.shiro;

import org.apache.shiro.util.JdbcUtils;
import org.springframework.beans.factory.FactoryBean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 从数据库加载
 *
 * @author Storezhang
 */
public class JdbcPermissionDefinitionsLoader implements FactoryBean<Map<String, String>> {

    public static final String PERMISSION_STRING = "perms[\"%s\"]";

    private DataSource dataSource;
    private String sql;

    public JdbcPermissionDefinitionsLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, String> getObject() throws Exception {
        Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = null;
        Map<String, String> filters = new LinkedHashMap<String, String>();

        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                String url = rs.getString(1);
                String permission = rs.getString(2);
                filters.put(url, String.format(PERMISSION_STRING, permission));
            }
        } finally {
            JdbcUtils.closeResultSet(rs);
        }

        return filters;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Class<?> getObjectType() {
        return this.getClass();
    }

    public boolean isSingleton() {
        return false;
    }
}
