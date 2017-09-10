package com.kaku.colorfulnews.mvp.entity;

import java.util.List;

/**
 * Created by Hob Den on 2017/9/10.
 */

public class THUNewsList {
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private int totalRecords;
    private List<BriefNewsRaw> list;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<BriefNewsRaw> getList() {
        return list;
    }

    public void setList(List<BriefNewsRaw> list) {
        this.list = list;
    }
}

