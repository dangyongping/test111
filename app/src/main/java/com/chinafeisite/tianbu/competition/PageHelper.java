package com.chinafeisite.tianbu.competition;

import android.util.Log;

import java.util.List;

/*
 *  @项目名：  RK3188-BP 
 *  @包名：    com.chinafeisite.tianbu.competition
 *  @文件名:   PageHelper
 *  @创建者:   Administrator
 *  @创建时间:  2016/12/8 18:06
 *  @描述：    TODO
 */
public class PageHelper<T> {
    private static final String TAG = "PageHelper";
    private List<T> allData; // 所有数据
    private int perPage = 12; // 每页条目
    private int currentPage = 1;// 当前页
    private int pageNum = 1; // 页码
    private List<T> childData;// 子数据
    private int allNum;// 总共条目

    public PageHelper(List<T> datas, int perPage) {
        this.allData = datas;
        if (perPage > 0)
            this.perPage = perPage;
        // 如果数据大于10条
        if (allData != null && allData.size() > perPage) {
            childData = allData.subList(0, perPage - 1);
        }
        allNum = allData.size();//总条目,即总集合的大小
        // 如果总数能除断perPage，页数就是商，否则+1
        pageNum = allNum % perPage == 0 ? (allNum / perPage) : (allNum / perPage + 1);
    }

    public int getCount() {
        return this.allNum;//总条目
    }

    public int getCurrentPage() {
        return this.currentPage;//当前页
    }

    public int getPageNum() {
        return this.pageNum;//页数
    }

    public int getPerPage() {
        return this.perPage;//每页的条目
    }

    public void gotoPage(int n) { // 页面跳转
        currentPage = n > pageNum ? pageNum : (n < 1 ? 1 : n);
    }

    public boolean hasNextPage() {// 是否有下一页,如果当前页小于页码,true
        return currentPage < pageNum;
    }

    public boolean hasPrePage() {// 是否有前一页,如果当前页大于1,true
        return currentPage > 1;
    }

    public void headPage() {// 第一页
        currentPage = 1;
    }

    public void lastPage() {// 最后一页
        currentPage = pageNum;
    }

    public void nextPage() {// 下一页,有下一页,当前页+1,否则当前页就是最后一页
        currentPage = hasNextPage() ? currentPage + 1 : pageNum;
    }

    public void prePage() {// 前一页,有前一页,当前页_1,否则当前页就是第一页
        currentPage = hasPrePage() ? currentPage - 1 : 1;
    }

    public void setPerPage(int perPage) {// 设置上一页面
        this.perPage = perPage;
    }

    /**
     * 获得当前数据
     * @return
     */
    public List<T> currentList() {
        if (currentPage == 1) {
            childData = allData.subList(0, perPage);
        } else if (currentPage == pageNum) {//pageNum是页数,最后一页的页码值
            childData = allData.subList(perPage * (pageNum - 1), allNum);
        } else {
            childData = allData.subList(perPage * (currentPage - 1), perPage * currentPage);
            Log.d("PageHelper", "长度="+childData.size()+"内容"+childData.toString());
        }
        return childData;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
