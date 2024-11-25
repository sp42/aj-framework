package com.ajaxjs.base.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 数据字典
 *
 * @author Frank Cheung
 */
@Data
public class DataDict implements IBaseModel {
    private static final long serialVersionUID = 8694943130249360094L;

    /**
     * 主键 id，自增
     */
    private Long id;

    /**
     * 名称、自定义编码、相当于 key。可选的
     */
    @NotNull
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 简介、描述
     */
    private String desc;

    /**
     * 父 id
     */
    @NotNull
    private Long parentId;

    /**
     * 类型 id
     */
//	@NotNull
    private Long type;

    /**
     * 顺序、序号
     */
    private Integer sortNo;

    /**
     * 数据字典：状态
     */
    private Integer stat;

    /**
     * 唯一 id，通过 uuid 生成不重复 id
     */
    private Long uid;

    /**
     * 租户 id。0 = 不设租户
     */
    private Long tenantId;

    /**
     * 租户 id。0 = 不设租户
     */
    private Long portalId;

    /**
     * 创建者 id
     */
    private Long createByUser;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date updateDate;
}