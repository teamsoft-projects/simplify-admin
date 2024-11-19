/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : localhost:3306
 Source Schema         : framework

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 17/03/2024 21:44:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary`;
CREATE TABLE `sys_dictionary`  (
  `id` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '编号',
  `code` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '编码',
  `group_code` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '分组编码',
  `join_code` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '连接码',
  `name` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `sort_order` int NULL DEFAULT NULL COMMENT '排序码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dictionary
-- ----------------------------
INSERT INTO `sys_dictionary` VALUES ('0af127410ccd483e98b41311805ef914', 'yes', 'yes_no', '', '是', 1);
INSERT INTO `sys_dictionary` VALUES ('6800558a4a8a4962bbe19e38bf105af5', 'no', 'yes_no', '', '否', 2);
INSERT INTO `sys_dictionary` VALUES ('c2bbb72a348d4fe99dde0a57f661ddaf', 'male', 'gender', '', '男', 1);
INSERT INTO `sys_dictionary` VALUES ('4bfd1767b1b64df3bba30a2220d17c89', 'female', 'gender', '', '女', 2);

-- ----------------------------
-- Table structure for sys_dictionary_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary_group`;
CREATE TABLE `sys_dictionary_group`  (
  `id` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '编号',
  `code` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `sort_order` int NULL DEFAULT NULL COMMENT '排序码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '数据字典分组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dictionary_group
-- ----------------------------
INSERT INTO `sys_dictionary_group` VALUES ('51616dca35984e48806733b335670c27', 'yes_no', '是否', 1);
INSERT INTO `sys_dictionary_group` VALUES ('88e52a52f7a046749d6ce38e7504fe90', 'gender', '性别', 2);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `parent_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级菜单编号',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `icon` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接地址',
  `sort_order` int NULL DEFAULT NULL COMMENT '排序码',
  `create_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('65734b5ad4eb439a9f2a47eeb47dfff1', '6e362a598b224fdb90225b57092cd5bb', '用户管理', 'layui-icon layui-icon-friends', 'sys/user/list', 3, '2018-06-26 14:38:02');
INSERT INTO `sys_menu` VALUES ('6e362a598b224fdb90225b57092cd5bb', 'top', '系统管理', 'layui-icon layui-icon-util', '', 1000, '2018-06-26 14:38:02');
INSERT INTO `sys_menu` VALUES ('76f05807cd8e453d91437e32810386ff', '6e362a598b224fdb90225b57092cd5bb', '权限管理', 'layui-icon layui-icon-component', 'sys/role/list', 2, '2018-06-26 14:38:02');
INSERT INTO `sys_menu` VALUES ('9f0595bb2bac43fabea7600955ff946a', '6e362a598b224fdb90225b57092cd5bb', '系统配置', 'layui-icon layui-icon-set-sm', 'sys/paramConfig/list', 4, '2018-06-26 14:38:02');
INSERT INTO `sys_menu` VALUES ('de4652369af54d3287590d2858f7de8f', '6e362a598b224fdb90225b57092cd5bb', '数据字典', 'layui-icon layui-icon-template', 'sys/dictionary/list', 6, '2018-06-26 14:38:02');
INSERT INTO `sys_menu` VALUES ('e1ea7fa9eaca43b1acfc4d92281b2d40', '6e362a598b224fdb90225b57092cd5bb', '菜单管理', 'layui-icon layui-icon-date', 'sys/menu/list', 1, '2018-06-26 14:38:02');

-- ----------------------------
-- Table structure for sys_menu_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_function`;
CREATE TABLE `sys_menu_function`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `menu_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单编号',
  `identify` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一标识',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `icon` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接地址',
  `description` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `sort_order` int NULL DEFAULT NULL COMMENT '排序码',
  `create_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu_function
-- ----------------------------
INSERT INTO `sys_menu_function` VALUES ('209a1b83fb4145d681983bb6eb5b99d4', 'de4652369af54d3287590d2858f7de8f', 'dictionary-group-add', 'groupAdd', 'layui-icon-add-1', 'sys/dictionaryGroup/edit', '分组新增', 5, NULL);
INSERT INTO `sys_menu_function` VALUES ('4af55465b17845eeaf10e8d43a6310d1', 'de4652369af54d3287590d2858f7de8f', 'dictionary-group', 'group', 'layui-icon-templeate-1', 'sys/dictionaryGroup/list', '字典分组', 4, NULL);
INSERT INTO `sys_menu_function` VALUES ('5867daa732a64c52a8c5303e8a30e4be', '65734b5ad4eb439a9f2a47eeb47dfff1', 'user-add', 'add', 'layui-icon-add-1', 'sys/user/edit', '新增', 1, NULL);
INSERT INTO `sys_menu_function` VALUES ('5b56335dc4714ef2947ef119a4d883cf', 'e1ea7fa9eaca43b1acfc4d92281b2d40', 'menu-edit', 'edit', 'layui-icon-edit', 'sys/menu/edit', '修改', 2, NULL);
INSERT INTO `sys_menu_function` VALUES ('619a7ccf8e82467fba6a92f63748b9d9', '76f05807cd8e453d91437e32810386ff', 'role-edit', 'edit', 'layui-icon-edit', 'sys/role/edit', '修改', 2, NULL);
INSERT INTO `sys_menu_function` VALUES ('678e521cfc2f47a2849aa11e5364f8ef', 'e1ea7fa9eaca43b1acfc4d92281b2d40', 'menu-add', 'add', 'layui-icon-add-1', 'sys/menu/edit', '新增', 1, NULL);
INSERT INTO `sys_menu_function` VALUES ('771d12eacc7245c48089be732c4e71d4', 'de4652369af54d3287590d2858f7de8f', 'dictionary-remove', 'remove', 'layui-icon-delete', 'sys/dictionary/remove', '删除', 3, NULL);
INSERT INTO `sys_menu_function` VALUES ('87d0287523cc4c18a2e007747db3fde4', '65734b5ad4eb439a9f2a47eeb47dfff1', 'user-resetPasswd', 'resetPasswd', 'layui-icon-unlink', 'sys/user/resetPasswd', '重置密码', 4, NULL);
INSERT INTO `sys_menu_function` VALUES ('8e97e61ef331453888a4f039c13ea8a4', '76f05807cd8e453d91437e32810386ff', 'role-auth', 'auth', 'layui-icon-password', 'sys/role/auth', '角色授权', 4, NULL);
INSERT INTO `sys_menu_function` VALUES ('913f585bf9c947b2a707db344f9400c2', '65734b5ad4eb439a9f2a47eeb47dfff1', 'user-edit', 'edit', 'layui-icon-edit', 'sys/user/edit', '修改', 2, NULL);
INSERT INTO `sys_menu_function` VALUES ('9bfde1a97177477f9a73efd6d69f417c', '76f05807cd8e453d91437e32810386ff', 'role-add', 'add', 'layui-icon-add-1', 'sys/role/edit', '新增', 1, NULL);
INSERT INTO `sys_menu_function` VALUES ('9dba5b3861884b74839cbed831368fac', '65734b5ad4eb439a9f2a47eeb47dfff1', 'user-remove', 'remove', 'layui-icon-delete', 'sys/user/remove', '删除', 3, NULL);
INSERT INTO `sys_menu_function` VALUES ('a338d4525ce34755a9d738e32e4b7fd0', 'e1ea7fa9eaca43b1acfc4d92281b2d40', 'menu-func', 'func', 'layui-icon-release', 'sys/menuFunc/list', '功能配置', 4, NULL);
INSERT INTO `sys_menu_function` VALUES ('a6dae328381643bba908061530439899', 'de4652369af54d3287590d2858f7de8f', 'dictionary-group-remove', 'groupRemove', 'layui-icon-delete', 'sys/dictionaryGroup/remove', '分组删除', 7, NULL);
INSERT INTO `sys_menu_function` VALUES ('b551cd960b0847c4baaf4defc72d3e36', 'e1ea7fa9eaca43b1acfc4d92281b2d40', 'menu-remove', 'remove', 'layui-icon-delete', 'sys/menu/remove', '删除', 3, NULL);
INSERT INTO `sys_menu_function` VALUES ('c06ee9b0343d47ea8f33f6539a5ac259', 'de4652369af54d3287590d2858f7de8f', 'dictionary-group-edit', 'groupEdit', 'layui-icon-edit', 'sys/dictionaryGroup/edit', '分组修改', 6, NULL);
INSERT INTO `sys_menu_function` VALUES ('c5950d7eea8e4cab953d070e8f07547d', 'de4652369af54d3287590d2858f7de8f', 'dictionary-edit', 'edit', 'layui-icon-edit', 'sys/dictionary/edit', '修改', 2, NULL);
INSERT INTO `sys_menu_function` VALUES ('cafeb7e022df44b1b203a594f3efe87a', '76f05807cd8e453d91437e32810386ff', 'role-remove', 'remove', 'layui-icon-delete', 'sys/role/remove', '删除', 3, NULL);
INSERT INTO `sys_menu_function` VALUES ('cec7ec19a23a4a87b78d6b40fb2b7fba', 'de4652369af54d3287590d2858f7de8f', 'dictionary-add', 'add', 'layui-icon-add-1', 'sys/dictionary/edit', '新增', 1, NULL);

-- ----------------------------
-- Table structure for sys_param_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_param_config`;
CREATE TABLE `sys_param_config`  (
  `param_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置项',
  `param_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置项名称',
  `param_value` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置值',
  `foo1` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拓展1',
  `foo2` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '拓展2',
  `tips` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提示信息',
  `show_flag` tinyint NULL DEFAULT 1 COMMENT '是否页面展示 1：展示 0：不展示',
  `sort_order` int NULL DEFAULT NULL COMMENT '排序码',
  PRIMARY KEY (`param_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_param_config
-- ----------------------------
INSERT INTO `sys_param_config` VALUES ('SYSTEM_NAME', '系统名称', 'XXXXXXXxxxx系统', NULL, NULL, NULL, 1, 120);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `sort_order` int NULL DEFAULT NULL COMMENT '排序码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('fc6ae41183724264a6f0753ca3554136', '超级管理员', '系统管理员', 1);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
                                  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
                                  `role_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                  `menu_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('0277d4c2a50f4dd7a3a71a971a1772f1', 'fc6ae41183724264a6f0753ca3554136', '9f0595bb2bac43fabea7600955ff946a');
INSERT INTO `sys_role_menu` VALUES ('1874e9015d3448b58a290a74a4e21793', 'fc6ae41183724264a6f0753ca3554136', 'de4652369af54d3287590d2858f7de8f');
INSERT INTO `sys_role_menu` VALUES ('7df01073fa6743fab6f316d628da2197', 'fc6ae41183724264a6f0753ca3554136', '76f05807cd8e453d91437e32810386ff');
INSERT INTO `sys_role_menu` VALUES ('b23cc400521245d3bc51723081e50b68', 'fc6ae41183724264a6f0753ca3554136', 'e1ea7fa9eaca43b1acfc4d92281b2d40');
INSERT INTO `sys_role_menu` VALUES ('e3f66bc7f40c45a2aa9dbf4833b3070b', 'fc6ae41183724264a6f0753ca3554136', '65734b5ad4eb439a9f2a47eeb47dfff1');
INSERT INTO `sys_role_menu` VALUES ('efc7bfde3a634559832205a2ee661cee', 'fc6ae41183724264a6f0753ca3554136', '6e362a598b224fdb90225b57092cd5bb');

-- ----------------------------
-- Table structure for sys_role_menu_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu_function`;
CREATE TABLE `sys_role_menu_function`  (
                                           `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
                                           `role_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色编号',
                                           `menu_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单编号',
                                           `func_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能编号',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单功能表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu_function
-- ----------------------------
INSERT INTO `sys_role_menu_function` VALUES ('182c010621d84e618e662f0a13ea15d1', 'fc6ae41183724264a6f0753ca3554136', 'e1ea7fa9eaca43b1acfc4d92281b2d40', '5b56335dc4714ef2947ef119a4d883cf');
INSERT INTO `sys_role_menu_function` VALUES ('2d6252c15dc84146b7a51d1bc2f7bd28', 'fc6ae41183724264a6f0753ca3554136', 'de4652369af54d3287590d2858f7de8f', '771d12eacc7245c48089be732c4e71d4');
INSERT INTO `sys_role_menu_function` VALUES ('322867b67e9740a9b8e8646bb518894f', 'fc6ae41183724264a6f0753ca3554136', 'de4652369af54d3287590d2858f7de8f', 'a6dae328381643bba908061530439899');
INSERT INTO `sys_role_menu_function` VALUES ('340e807489564f2db57e0f2bbcbaaa93', 'fc6ae41183724264a6f0753ca3554136', 'de4652369af54d3287590d2858f7de8f', 'c5950d7eea8e4cab953d070e8f07547d');
INSERT INTO `sys_role_menu_function` VALUES ('372cb0b2f8dd4e7e9cf952a980fa9926', 'fc6ae41183724264a6f0753ca3554136', '76f05807cd8e453d91437e32810386ff', '9bfde1a97177477f9a73efd6d69f417c');
INSERT INTO `sys_role_menu_function` VALUES ('3942b4edaa7342a6b168cd0810437bc1', 'fc6ae41183724264a6f0753ca3554136', 'de4652369af54d3287590d2858f7de8f', '4af55465b17845eeaf10e8d43a6310d1');
INSERT INTO `sys_role_menu_function` VALUES ('3f2f4a37b2d94bd58b6796cb58bb365e', 'fc6ae41183724264a6f0753ca3554136', 'de4652369af54d3287590d2858f7de8f', 'cec7ec19a23a4a87b78d6b40fb2b7fba');
INSERT INTO `sys_role_menu_function` VALUES ('42579fd5b4554851b2a2fa6245ad36a7', 'fc6ae41183724264a6f0753ca3554136', '76f05807cd8e453d91437e32810386ff', 'cafeb7e022df44b1b203a594f3efe87a');
INSERT INTO `sys_role_menu_function` VALUES ('44da4d43d8b245358cd5ac516947f71b', 'fc6ae41183724264a6f0753ca3554136', 'e1ea7fa9eaca43b1acfc4d92281b2d40', '678e521cfc2f47a2849aa11e5364f8ef');
INSERT INTO `sys_role_menu_function` VALUES ('574ebe08b649430bba28129449f59b35', 'fc6ae41183724264a6f0753ca3554136', 'de4652369af54d3287590d2858f7de8f', 'c06ee9b0343d47ea8f33f6539a5ac259');
INSERT INTO `sys_role_menu_function` VALUES ('5fed59c8d25f4ff28256e9761959d7c1', 'fc6ae41183724264a6f0753ca3554136', '65734b5ad4eb439a9f2a47eeb47dfff1', '913f585bf9c947b2a707db344f9400c2');
INSERT INTO `sys_role_menu_function` VALUES ('61cb98582ee24086be1c3237f9ef7333', 'fc6ae41183724264a6f0753ca3554136', '65734b5ad4eb439a9f2a47eeb47dfff1', '5867daa732a64c52a8c5303e8a30e4be');
INSERT INTO `sys_role_menu_function` VALUES ('71985c2584ce476385088d757f2b12d0', 'fc6ae41183724264a6f0753ca3554136', 'e1ea7fa9eaca43b1acfc4d92281b2d40', 'a338d4525ce34755a9d738e32e4b7fd0');
INSERT INTO `sys_role_menu_function` VALUES ('802d8f7d3531473eafd1e8b6fb54b893', 'fc6ae41183724264a6f0753ca3554136', 'de4652369af54d3287590d2858f7de8f', '209a1b83fb4145d681983bb6eb5b99d4');
INSERT INTO `sys_role_menu_function` VALUES ('8979a277eb4c49dbaeb230f3a0be2716', 'fc6ae41183724264a6f0753ca3554136', 'e1ea7fa9eaca43b1acfc4d92281b2d40', 'b551cd960b0847c4baaf4defc72d3e36');
INSERT INTO `sys_role_menu_function` VALUES ('8d9d5901e1a44087bb97a7aa03ccd918', 'fc6ae41183724264a6f0753ca3554136', '65734b5ad4eb439a9f2a47eeb47dfff1', '9dba5b3861884b74839cbed831368fac');
INSERT INTO `sys_role_menu_function` VALUES ('92144f4e55594a368048c7388b645b7e', 'fc6ae41183724264a6f0753ca3554136', '65734b5ad4eb439a9f2a47eeb47dfff1', '87d0287523cc4c18a2e007747db3fde4');
INSERT INTO `sys_role_menu_function` VALUES ('92f484fd008d4f1998f956903850e630', 'fc6ae41183724264a6f0753ca3554136', '76f05807cd8e453d91437e32810386ff', '8e97e61ef331453888a4f039c13ea8a4');
INSERT INTO `sys_role_menu_function` VALUES ('a8223eaffde24312bb40cafde2ae2f4b', 'fc6ae41183724264a6f0753ca3554136', '76f05807cd8e453d91437e32810386ff', '619a7ccf8e82467fba6a92f63748b9d9');

-- ----------------------------
-- Table structure for sys_sequence
-- ----------------------------
DROP TABLE IF EXISTS `sys_sequence`;
CREATE TABLE `sys_sequence`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `sequence` int NULL DEFAULT NULL COMMENT '自增sequence',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数字序列表(用于生成不重复sequence)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_sequence
-- ----------------------------
INSERT INTO `sys_sequence` VALUES (1, 'member_identify', 1010);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `role_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色编号',
  `login_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录号',
  `passwd` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `description` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `is_default_passwd` tinyint NULL DEFAULT NULL COMMENT '是否默认密码(1.是 0.否)',
  `create_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `sort_order` int NULL DEFAULT NULL COMMENT '排序码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表(后台用户)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('09ed0c0571954f46a083336a8373f708', 'fc6ae41183724264a6f0753ca3554136', 'admin', 'bc7af5067bcb7f3e210894d201bc22c3', '系统管理员', '18855558888', '超级管理员', 0, '2017-08-28 09:06:31', 1);

SET FOREIGN_KEY_CHECKS = 1;
