package com.ajaxjs.base.service.datasource;

import com.ajaxjs.base.controller.DatasourceController;
import com.ajaxjs.base.model.DataSourceInfo;
import com.ajaxjs.base.service.DataServiceUtils;
import com.ajaxjs.data.PageResult;
import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.framework.CRUD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DatasourceService implements DatasourceController {
    @Override
    public List<DataSourceInfo> list() {
        return CRUD.list(DataSourceInfo.class, "SELECT * FROM adp_datasource WHERE stat != 1");
    }

    @Override
    public Boolean test(Long id) {
        try (Connection connection = getConnectionByDataSourceId(id)) {
            log.info(connection.getMetaData().getURL());

            return true;
        } catch (SQLException e) {
            log.warn("err:", e);
            return false;
        }
    }

    @Override
    public Long create(DataSourceInfo entity) {
        return CRUD.create(entity);
    }

    /**
     * 是否重复数据源编码
     *
     * @param dsId 数据源 id，非 null 时候表示更新排除自己
     */
    private void checkIfIsRepeat(DataSourceInfo entity, Long dsId) {
        String sql;

        if (dsId != null)
            sql = "SELECT id FROM adp_datasource WHERE url_dir = ? AND id != " + dsId + " LIMIT 1";
        else
            sql = "SELECT id FROM adp_datasource WHERE url_dir = ? LIMIT 1";

        Long id = CRUD.queryOne(Long.class, sql, entity.getUrlDir());

        if (id != null)
            throw new IllegalArgumentException("已存在相同编码的数据源 " + entity.getUrlDir());
    }

    @Override
    public Boolean update(DataSourceInfo entity) {
        if (entity.getId() == null)
            throw new IllegalArgumentException("缺少 id 参数");

        checkIfIsRepeat(entity, entity.getId());

        return CRUD.updateWithIdField(entity);
    }

    @Override
    public Boolean delete(Long id) {
        return CRUD.delete("adp_datasource", id);
    }

    @Override
    public List<Map<String, Object>> getAllTablesComment(Long id) throws SQLException {
        try (Connection connection = getConnectionByDataSourceId(id)) {
            TableQuery q = new TableQuery(connection);

            return q.getTableCommentWithAnnotateAsList(q.getAllTableName(null), null);
        }
    }

    @Override
    public List<Column> getTableColumn(Long id, String tableName) throws SQLException {
        try (Connection connection = getConnectionByDataSourceId(id)) {
            return new ColumnQuery(connection).getColumnComment(tableName, null);
        }
    }

    Connection getConnectionByDataSourceId(Long id) {
        DataSourceInfo info = CRUD.info(DataSourceInfo.class, "SELECT * FROM adp_datasource WHERE stat!= 1 AND id =?", id);

        return DataServiceUtils.getConnectionByDataSourceInfo(info);
    }

    @Override
    public PageResult<Map<String, Object>> getTableAndComment(Long dataSourceId, Integer start, Integer limit, String tableName, String dbName) {
        if (start == null)
            start = 0;
        if (limit == null)
            limit = 99;

        try (Connection conn = getConnectionByDataSourceId(dataSourceId)) {
            return getTableAndComment(conn, start, limit, tableName, dbName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回数据源下的表名和表注释，支持分页和表名搜索
     *
     * @param _conn
     * @param start
     * @param limit
     * @param tablename 搜索的关键字
     * @return
     * @throws SQLException
     */
    private static PageResult<Map<String, Object>> getTableAndComment(Connection _conn, Integer start, Integer limit, String tablename, String dbName) throws SQLException {
        int total;
        List<Map<String, Object>> list = null;

        try {
            TableQuery tableQuery = new TableQuery(_conn);
            List<String> allTableName = tableQuery.getAllTableName(dbName);

            // 有可能出现配置表本身，删除
            allTableName.remove("adp_data_service");

            if (StringUtils.hasText(tablename)) // 搜索关键字
                allTableName = allTableName.stream().filter(item -> item.contains(tablename)).collect(Collectors.toList());

            total = allTableName.size();

            if (total > 0) {
//                List<String> subList = allTableName.subList(start, limit); // 有坑 会返回空 List
                List<String> subList = new ArrayList<>();

                for (int i = start; i < (start + limit); i++) {
                    if (i < total)
                        subList.add(allTableName.get(i));
                }

                list = tableQuery.getTableCommentWithAnnotateAsList(subList, dbName);
            }
        } finally {
            JdbcConn.closeDb(_conn);
        }

        PageResult<Map<String, Object>> result = new PageResult<>();

        if (list != null)
            result.addAll(list);

        // 排序
        Comparator<Map<String, Object>> byName = Comparator.comparing(o -> o.get("tableName").toString());
        result.sort(byName);
        result.setTotalCount(total);

        return result;
    }

    /**
     * 根据创建表的SQL语句获取注释
     *
     * @param createTableSql 创建表的SQL语句
     * @return 注释内容，如果不存在注释则返回null
     */
    private static String getCommentFromCreateTableSql(String createTableSql) {
        // 查找注释起始位置
        int commentStartIndex = createTableSql.indexOf("COMMENT='");

        // 如果找不到注释起始位置，则返回null
        if (commentStartIndex == -1)
            return null;
        // 查找注释结束位置
        int commentEndIndex = createTableSql.indexOf("'", commentStartIndex + 9);

        // 如果找不到注释结束位置，则返回null
        if (commentEndIndex == -1)
            return null;

        // 返回注释内容
        return createTableSql.substring(commentStartIndex + 9, commentEndIndex);
    }

}
