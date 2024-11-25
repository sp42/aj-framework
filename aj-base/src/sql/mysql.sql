CREATE TABLE IF NOT EXISTS `sys_tenant` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
    `parent_id` int(11) DEFAULT NULL COMMENT '父 id',
    `name` varchar(20) NOT NULL COMMENT '租户名称',
    `content` varchar(200) DEFAULT NULL COMMENT '简介、描述',
    `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
    `contacts` varchar(150) DEFAULT NULL COMMENT '联系方式',
    `stat` tinyint(2) DEFAULT NULL COMMENT '数据字典：状态',
    `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
    `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
    `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
    `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
    `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE
) COMMENT = '租户信息';

CREATE TABLE IF NOT EXISTS `sys_datadict` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
    `name` varchar(50) NOT NULL COMMENT '名称',
    `value` varchar(256) DEFAULT NULL COMMENT '值',
    `content` varchar(200) DEFAULT NULL COMMENT '简介、描述',
    `parent_id` int(11) DEFAULT NULL COMMENT '父 id',
    `type` int(11) DEFAULT NULL COMMENT '类型 id',
    `sort_no` tinyint(4) DEFAULT NULL COMMENT '顺序、序号',
    `stat` tinyint(2) DEFAULT NULL COMMENT '数据字典：状态',
    `uid` bigint(20) DEFAULT NULL COMMENT '唯一 id，通过 uuid 生成不重复 id',
    `extend` text COMMENT '扩展属性',
    `tenant_id` int(11) NOT NULL DEFAULT '0' COMMENT '租户 id。0 = 不设租户',
    `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
    `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
    `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
    `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
    `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
    KEY `tenantId` (`tenant_id`) USING BTREE
) COMMENT = '数据字典';

CREATE TABLE IF NOT EXISTS `sys_attachment` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
    `name` varchar(20) NOT NULL COMMENT '名称',
    `desc` varchar(200) DEFAULT NULL COMMENT '简介、描述',
    `type` int(11) DEFAULT '0' COMMENT '分类：null/0/1=普通图片、2=头像/封面图片、3=相册图片',
    `tenant_id` int(11) NOT NULL DEFAULT '0' COMMENT '租户 id。0 = 不设租户',
    `path` varchar(255) DEFAULT NULL COMMENT '路径',
    `file_size` int(11) NOT NULL COMMENT '文件大小（单位：字节）',
    `owner_id` bigint(20) DEFAULT NULL COMMENT '该图片属于哪个实体？这里给出实体的 uid',
    `stat` tinyint(4) DEFAULT NULL COMMENT '数据字典：状态',
    `uid` bigint(20) DEFAULT NULL COMMENT '唯一 id，通过 uuid 生成不重复 id',
    `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
    `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
    `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
    `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
    `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
    KEY `tenantId` (`tenant_id`) USING BTREE
) COMMENT = '上传附件';

CREATE TABLE IF NOT EXISTS `sys_tag` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
    `tag_index` tinyint(4) NOT NULL COMMENT '标签索引',
    `name` varchar(20) DEFAULT NULL COMMENT '名称、自定义编码、相当于 key。可选的',
    `desc` varchar(200) DEFAULT NULL COMMENT '简介、描述',
    `stat` tinyint(4) DEFAULT NULL COMMENT '数据字典：状态',
    `uid` bigint(20) DEFAULT NULL COMMENT '唯一 id，通过 uuid 生成不重复 id',
    `tenant_id` int(11) NOT NULL DEFAULT '0' COMMENT '租户 id。0 = 不设租户',
    `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
    `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
    `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
    `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
    `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
    KEY `tenantId` (`tenant_id`) USING BTREE
) COMMENT = '标签信息';

CREATE TABLE IF NOT EXISTS `sys_log` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
    `name` varchar(200) NOT NULL COMMENT '简介、描述',
    `user_id` int(11) DEFAULT '0' COMMENT '操作者 id',
    `tenant_id` int(11) DEFAULT '0' COMMENT '租户 id。0 = 不设租户',
    `ip` varchar(20) DEFAULT NULL COMMENT '操作者 ip',
    `sql` text COMMENT '相关执行的 SQL',
    `content` text COMMENT '其他操作内容',
    `error` text COMMENT '操作异常，如果为空表示操作成功',
    `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
    KEY `tenantId` (`tenant_id`) USING BTREE
) COMMENT = '操作日志';

CREATE TABLE IF NOT EXISTS `article` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
    `name` varchar(90) NOT NULL COMMENT '名称',
    `title` varchar(255) DEFAULT NULL COMMENT '副标题',
    `brief` varchar(255) DEFAULT NULL COMMENT '简介',
    `content` text COMMENT '内容',
    `author` varchar(100) DEFAULT NULL COMMENT '作者',
    `source` varchar(100) DEFAULT NULL COMMENT '出处',
    `cover` varchar(255) DEFAULT NULL COMMENT '封面图',
    `hot` smallint(6) DEFAULT NULL COMMENT '热度',
    `source_url` varchar(255) DEFAULT NULL COMMENT '源网址',
    `catelog_id` int(11) DEFAULT NULL COMMENT '分类id',
    `stat` tinyint(2) DEFAULT NULL COMMENT '数据字典：状态',
    `uid` bigint(20) NOT NULL COMMENT '唯一 id，通过“雪花算法”生成不重复 id',
    `tenant_id` smallint(6) DEFAULT '0' COMMENT '门户 id。0 = 不设门户',
    `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
    `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
    `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
    `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
    `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
    KEY `tenantId` (`tenant_id`) USING BTREE
) COMMENT = '文章';

CREATE TABLE IF NOT EXISTS `feedback` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
    `title` varchar(255) DEFAULT NULL COMMENT '简介',
    `content` text COMMENT '内容',
    `name` varchar(90) NOT NULL COMMENT '联系人',
    `ip` varchar(45) NOT NULL COMMENT '客户 ip',
    `contact` varchar(255) DEFAULT NULL COMMENT '联系方式',
    `feedback` varchar(100) DEFAULT NULL COMMENT '反馈',
    `type_id` int(11) DEFAULT NULL COMMENT '分类id',
    `stat` tinyint(2) unsigned NOT NULL COMMENT '数据字典：状态',
    `uid` bigint(20) NOT NULL COMMENT '唯一 id，通过“雪花算法”生成不重复 id',
    `tenant_id` smallint(6) DEFAULT '0' COMMENT '门户 id。0 = 不设门户',
    `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
    `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
    `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
    `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
    `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
    KEY `tenantId` (`tenant_id`) USING BTREE
) COMMENT = '留言反馈';

CREATE TABLE IF NOT EXISTS `adp_datasource` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
    `name` varchar(45) NOT NULL COMMENT '名称',
    `url_dir` varchar(50) NOT NULL COMMENT 'URL 目录',
    `type` tinyint(4) NOT NULL COMMENT '数据源类型',
    `url` varchar(255) NOT NULL COMMENT '连接地址',
    `username` varchar(255) DEFAULT NULL COMMENT '登录用户',
    `password` varchar(255) DEFAULT NULL COMMENT '登录密码',
    `connect_ok` tinyint(1) DEFAULT NULL COMMENT '是否连接验证成功',
    `stat` tinyint(4) DEFAULT NULL COMMENT '数据字典：状态',
    `cross_db` tinyint(1) DEFAULT NULL COMMENT '是否跨库',
    `uid` bigint(20) DEFAULT NULL COMMENT '唯一 id，通过 uuid 生成不重复 id',
    `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
    `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
    `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
    `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
    `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE 
 
) COMMENT = '数据源';

CREATE TABLE IF NOT EXISTS `adp_data_service` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
    `name` varchar(255) DEFAULT NULL COMMENT '该命令的描述',
    `datasource_id` int(11) NOT NULL DEFAULT '0' COMMENT '数据源 id',
    `table_name` varchar(50) NOT NULL COMMENT '对应的表名',
    `url_root` varchar(255) DEFAULT NULL COMMENT 'URL 前缀',
    `url_dir` varchar(50) DEFAULT NULL COMMENT 'URL 目录',
    `json` longtext COMMENT '配置 JSON',
    `key_gen` tinyint(4) DEFAULT '1' COMMENT '主键生成策略',
    `tags` int(11) DEFAULT NULL COMMENT '标签 total',
    `stat` tinyint(2) DEFAULT NULL COMMENT '数据字典：状态',
    `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
    `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
    `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
    `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
    `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE
) COMMENT = '表配置';