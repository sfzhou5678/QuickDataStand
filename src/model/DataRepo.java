package model;

import tools.Counter;

import java.util.*;

/**
 * 大列的数据仓库
 * NOTE:暂时还无法使用，等待高级的Valid技巧
 * Created by hasee on 2017/2/22.
 */
public class DataRepo<T> {
    /**
     * 原始输入的数据，用于恢复原状
     */
    List<T> oriDatas = new ArrayList<T>();

    /**
     * 当前保留下来的所有数据，但是包含空值和错误值
     */
    List<T> curDatas = new ArrayList<T>();

    /**
     * curDatas按次数排序后的结果，存储在sortedDatas中
     */
    List<Map.Entry<T, Integer>> sortedDatas;
    private Counter<T> counter;    // 和sortedDatas配套使用

    private boolean sortReverse = false;
    private int validCount = 0;

    /**
     * 更新sortedDatas
     */
    public void updateSortedDatas() {
        // FIXME: 2017/2/22 调用selectTopNPercentData之后还要更新validData，counter等，这块需要重新设计
        counter = new Counter<T>();
        for (T num : curDatas) {
            counter.count(num);
        }
        // 默认词频统计
        // TODO: 2017/2/21 这一块应该移到basicBarAddIn中去
        Map<T, Integer> counterMap = counter.getAllKeysStatistics();
        // TODO: 2017/2/21 下面代码重构进BarItem
        sortedDatas = new ArrayList<Map.Entry<T, Integer>>(counterMap.entrySet());
        Collections.sort(sortedDatas, new Comparator<Map.Entry<T, Integer>>() {
            public int compare(Map.Entry<T, Integer> o1,
                               Map.Entry<T, Integer> o2) {
                if (sortReverse) {
                    // 从小到大
                    return (o1.getValue() - o2.getValue());
                } else {
                    // 从大到小
                    return (o2.getValue() - o1.getValue());
                }
            }
        });
    }

    /**
     * NOTE:目前的技术还没发将ValidCounter放到Repo中
     * 如果能做一个通用的Error和Empty标签和通用的Error和Emtpy检测方法出来就可以
     */
//    public void updateValidCount() {
//        validCount=0;
//        for (T data:curDatas){
//            if (false) {
//                continue;
//            }
//            validCount++;
//        }
//    }


    public List<T> getOriDatas() {
        return oriDatas;
    }

    public List<T> getCurDatas() {
        return curDatas;
    }

    public List<Map.Entry<T, Integer>> getSortedDatas() {
        return sortedDatas;
    }

    public boolean isSortReverse() {
        return sortReverse;
    }

    public int getValidCount() {
        return validCount;
    }

    public void setOriDatas(List<T> oriDatas) {
        this.oriDatas = oriDatas;
    }

    public void setCurDatas(List<T> curDatas) {
        this.curDatas = curDatas;
    }

    public void setSortReverse(boolean sortReverse) {
        this.sortReverse = sortReverse;
    }

    public void setValidCount(int validCount) {
        this.validCount = validCount;
    }
}
