package com.iflow.json;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页数据
 * 
 * @author chenyf
 *
 */
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;

	private Integer pageNow = 1;

	private Integer pageSize = 20;

	private Long total = 0l;

	private List rows;

    public Integer getPageNow() {
        return pageNow;
    }

    public void setPageNow(Integer pageNow) {
        this.pageNow = pageNow;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

}
