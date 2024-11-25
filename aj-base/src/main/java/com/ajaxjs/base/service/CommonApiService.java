package com.ajaxjs.base.service;

import com.ajaxjs.base.controller.CommonApiController;
import com.ajaxjs.data.DataAccessObject;
import com.ajaxjs.data.crud.CRUD_Service;
import com.ajaxjs.data.data_service.DataService;
import com.ajaxjs.framework.filter.dbconnection.DataBaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class CommonApiService extends DataService implements CommonApiController {
    @Autowired
    @Lazy
    private CRUD_Service crudService;

    @Override
    public DataAccessObject getDao() {
        return crudService;
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) throws SQLException {
        try (Connection conn = DataBaseConnection.initDb()) {
            CRUD_Service crudService = this.crudService;
//            System.out.println("CRUD_Service init: " + crudService.hashCode());
            setDao(crudService);

            reloadConfig();// 在 Spring 初始化完成后执行的操作
        }
    }
}