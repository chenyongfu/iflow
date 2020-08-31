package com.iflow.util;

public class SqlUtil {

    /**
     * 模糊查询 返回 % keyword %
     *
     * @param keyword
     * @return
     */
    public static String getLikeKeyword(String keyword) {
        keyword = keyword.replace("%", "\\\\%");
        keyword = keyword.replace("_", "\\\\_");
        return "%" + keyword + "%";
    }

    /**
     * 模糊查询 返回 keyword %
     *
     * @param keyword
     * @return
     */
    public static String getPreffixLikeKeyword(String keyword) {
        keyword = keyword.replace("%", "\\\\%");
        keyword = keyword.replace("_", "\\\\_");
        return keyword + "%";
    }

    /**
     * 模糊查询 返回 % keyword
     *
     * @param keyword
     * @return
     */
    public static String getSubffixLikeKeyword(String keyword) {
        keyword = keyword.replace("%", "\\\\%");
        keyword = keyword.replace("_", "\\\\_");
        return "%" + keyword;
    }

}
