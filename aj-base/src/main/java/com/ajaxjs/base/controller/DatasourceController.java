package com.ajaxjs.base.controller;

import com.ajaxjs.base.model.DataSourceInfo;
import com.ajaxjs.base.service.datasource.Column;
import com.ajaxjs.data.PageResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 数据源配置的 CRUD，另外有一些实用工具方法。
 *
 * @author Frank Cheung sp42@qq.com
 */
@RestController
@RequestMapping("/datasource")
public interface DatasourceController {
    /**
     * 列出数据源
     *
     * @return 数据源列表
     */
    @GetMapping
    List<DataSourceInfo> list();

    /**
     * 测试数据源是否连接成功
     *
     * @param id 数据源 id
     * @return true 表示成功
     */
    @GetMapping("/test/{id}")
    Boolean test(@PathVariable Long id);

    /**
     * 创建数据源
     *
     * @param entity 数据源实体
     * @return 数据源 id
     */
    @PostMapping
    Long create(DataSourceInfo entity);

    /**
     * 修改数据源
     *
     * @param entity 数据源实体
     * @return true 表示成功
     */
    @PutMapping
    Boolean update(DataSourceInfo entity);

    /**
     * 删除数据源
     *
     * @param id 数据源 id
     * @return true 表示成功
     */
    @DeleteMapping("/{id}")
    Boolean delete(@PathVariable Long id);

    /**
     * 获取所有表名及其表注释
     *
     * @param id 数据源 id
     * @return 所有表名及其表注释
     * @throws SQLException 如果发生 SQL 异常
     */
    @GetMapping("/{id}/all_tables_comment")
    List<Map<String, Object>> getAllTablesComment(@PathVariable Long id) throws SQLException;


    /**
     * 获取指定数据库表的所有列
     *
     * @param id        数据源 id
     * @param tableName 数据库表名
     * @return 指定数据库表的所有列
     * @throws SQLException 如果发生 SQL 异常
     */
    @GetMapping("/{id}/table_all_column/{tableName}")
    List<Column> getTableColumn(@PathVariable Long id, @PathVariable String tableName) throws SQLException;

    /**
     * 指定数据源返回数据源下的表名和表注释
     *
     * @param dataSourceId
     * @param start
     * @param limit
     * @param tableName    搜索的关键字
     */
    @GetMapping("/{dataSourceId}/get_all_tables")
    PageResult<Map<String, Object>> getTableAndComment(@PathVariable Long dataSourceId, Integer start, Integer limit, String tableName, String dbName);
}
