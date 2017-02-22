package model.repository;

import model.addin.NumAddIn;
import tools.Counter;

import java.util.*;

/**
 * 专门处理数字数据，仅限于age、height等，不包括20140701000215这种日期数据
 * Created by hasee on 2017/2/22.
 */
public class NumRepo extends DataRepo {
    /**
     * 原始输入的数据，用于恢复原状
     */
    List<Double> oriDatas = new ArrayList<Double>();

    /**
     * 当前保留下来的所有数据，但是包含空值和错误值
     */
    List<Double> curDatas = new ArrayList<Double>();

    /**
     * curDatas按次数排序后的结果，存储在sortedDatas中
     */
    List<Map.Entry<Double, Integer>> sortedDatas;
    private Counter<Double> counter;    // 和sortedDatas配套使用

    /**
     * NOTE:目前的技术还没发将ValidCounter放到Repo中
     * 如果能做一个通用的Error和Empty标签和通用的Error和Emtpy检测方法出来就可以
     */
    @Override
    public void updateValidCount() {
        validCount=0;
        for (Double data:curDatas){
            if (data.equals(NumAddIn.errorValue) || data.equals(NumAddIn.emptyValue)) {
                // 跳过空值不作处理
                continue;
            }
            validCount++;
        }
    }

    /**
     * 更新sortedDatas
     */
    @Override
    public void updateSortedDatas() {
        counter = new Counter<Double>();
        for (Double num : curDatas) {
            counter.count(num);
        }
        // 默认词频统计
        // TODO: 2017/2/21 这一块应该移到basicBarAddIn中去
        Map<Double, Integer> counterMap = counter.getAllKeysStatistics();
        // TODO: 2017/2/21 下面代码重构进BarItem
        sortedDatas = new ArrayList<Map.Entry<Double, Integer>>(counterMap.entrySet());
        Collections.sort(sortedDatas, new Comparator<Map.Entry<Double, Integer>>() {
            public int compare(Map.Entry<Double, Integer> o1,
                               Map.Entry<Double, Integer> o2) {
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

    public List<Double> getOriDatas() {
        return oriDatas;
    }

    public void setOriDatas(List<Double> oriDatas) {
        this.oriDatas = oriDatas;
    }

    public List<Double> getCurDatas() {
        return curDatas;
    }

    public void setCurDatas(List<Double> curDatas) {
        this.curDatas = curDatas;
    }

    public List<Map.Entry<Double, Integer>> getSortedDatas() {
        return sortedDatas;
    }

    public void setSortedDatas(List<Map.Entry<Double, Integer>> sortedDatas) {
        this.sortedDatas = sortedDatas;
    }
}
