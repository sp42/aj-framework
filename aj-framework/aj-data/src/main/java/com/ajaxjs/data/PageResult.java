/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权； 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本： http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供， 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息 bean
 *
 * @param <T> Bean 对象，也可以是 Map
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PageResult<T> extends ArrayList<T> {
    private static final long serialVersionUID = 543109149479031294L;

    /**
     * 总记录数
     */
    private int totalCount;

    /**
     * 从第几笔记录开始
     */
    private int start;

    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 当前第几页
     */
    private int currentPage;

    /**
     * 是否没有数据，就是查询了之后，一条记录符合都没有
     */
    private boolean isZero;

    /**
     * 分页的逻辑运算
     */
    public void page() {
        int totalPage = getTotalCount() / getPageSize(), remainder = getTotalCount() % getPageSize();// 余数

        totalPage = (remainder == 0 ? totalPage : totalPage + 1);
        setTotalPage(totalPage);

        int currentPage = (getStart() / getPageSize()) + 1;

        setCurrentPage(currentPage);
    }

    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 12;

    /**
     * 列表不分页，转换为 PageResult
     *
     * @param <T>  实体类型
     * @param list 普通列表
     * @return 分页结果
     */
    public static <T> PageResult<T> list2PageList(List<T> list) {
        PageResult<T> result = new PageResult<>();
        result.addAll(list);
        result.setPageSize(list.size());
        result.setTotalCount(list.size());

        return result;
    }

    /*
     * 分页时高效的总页数计算 我们一般分页是这样来计算页码的： int row=200; //记录总数 int page=5;//每页数量 int
     * count=row%5==0?row/page:row/page+1; 上面这种是用的最多的! 那么下面我们来一种最简单的，不用任何判断！ 看代码：
     * int row=21; int pageCount=5; int sum=(row-1)/pageCount+1;//这样就计算好了页码数量，逢1进1
     */
}