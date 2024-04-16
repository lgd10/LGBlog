package com.lgblog.statement;

public class statementVal {
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;

    /**
     *  文章是正常发布状态
     */
    public static final String  ARTICLE_STATUS_NORMAL = "0";

    /**
     * 文章列表当前查询页数
     */
    public static final int ARTICLE_STATUS_CURRENT = 1;

    /**
     * 文章列表每页显示的数据条数
     */
    public static final int ARTICLE_STATUS_SIZE = 10;

    /**
     * 文章分类状态 有些被删除的分类的状态不是0
     */

    public static final String ARTICLE_CATEGORY_STATUS="0";

    /**
     * 链接状态 0审核通过，1表示不通过
     */
    public static final String FLINK_STATUS_NORMAL="0";

    public static final String FLINK_STATUS_UNSAFE="1";
    /**
     * 根评论的父id -1表示没有父评论
     */
    public static final Long PID=-1L;

    /**
     * 评论类型
     * 0表示文章评论
     * 1表示友链评论
     */
    public static final String ARTICLE_COMMENT="0";
    public static final String LINK_COMMENT="1";

    /**
     * 文章浏览量哈希值
     */
    public static final String ARTICLE_VIEWCOUNT_KEY="article:viewCount";

    /**
     * 菜单状态是正常的
     */
    public static String MENU_NORMAL="0";
    /**
     * 权限类型，菜单
     */
    public static final String TYPE_MENU = "C";

    /**
     * 权限类型，按钮
     */
    public static final String TYPE_BUTTON = "F";

    /**
     * 标签状态0是未删除 1是删除
     */
    public static final Integer TAG_NORMAL=0;
    public static final Integer TAG_DELETE=1;

    /**
     * 用户类型 0普通用户 1是管理员
     */

    public static final String ADMIN_TYPE="1";
    public static final String NORMAL_TYPE="0";

    /**
     * 文章是否删除
     * 0 表示未删除
     * 1 表示已删除
     */
    public static final Integer ARTICLE_DELETE=1;
    public static final Integer ARTICLE_EXIT=0;

    /**
     * 角色状态
     * 0 正在使用
     * 1 已经停用
     */
    public static final String ROLE_STATUS_USED="0";
    public static final String ROLE_STATUS_FORBID="1";
}
