package com.yabobaozb.ecom.common;

import lombok.Data;

import java.util.List;

/** 分页数据 */
@Data
public class PageResultData<T> {

    /** 总页数 */
    private int totalPages;

    /** 当前页数据 */
    private List<T> dataList;


}
