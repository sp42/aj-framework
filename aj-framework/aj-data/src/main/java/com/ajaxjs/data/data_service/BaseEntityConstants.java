package com.ajaxjs.data.data_service;

import java.util.HashMap;
import java.util.Map;

public interface BaseEntityConstants {
    /**
     *
     */
    Map<Integer, String> STATE = new HashMap<>() {
        private static final long serialVersionUID = -873485978038563365L;

        {
            put(-1, "草稿");
            put(0, "正常");
            put(1, "已删除");
            put(2, "禁用");
        }
    };

    /**
     * 正常
     */
    int STATUS_OK = 0;

    /**
     * 删除
     */
    int STATUS_DELETED = 1;

    /**
     * 草稿
     */
    int STATUS_DRAFT = -1;

    /**
     * 下架/下线/隐藏
     */
    int STATUS_OFFLINE = 2;

    /**
     * ID 类型，可以是自增、雪花算法、UUID
     */
    interface IdType {
        /**
         * 自增
         */
        int AUTO_INC = 1;

        /**
         *
         */
        int SNOW = 2;

        /**
         * UUID
         */
        int UUID = 3;
    }
}