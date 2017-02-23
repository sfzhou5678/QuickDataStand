package model.repository;

import model.addin.NumAddIn;
import tools.Counter;

import java.util.*;

/**
 * 处理大部分str格式的数据，如id、gender、language、location等
 * Created by hasee on 2017/2/22.
 */
public abstract class StringRepo extends DataRepo {
//    /**
//     * 原始输入的数据，用于恢复原状
//     */
//    List<String> oriDatas = new ArrayList<String>();
//
//    /**
//     * 当前保留下来的所有数据，但是包含空值和错误值
//     */
//    List<String> curDatas = new ArrayList<String>();
//
//    /**
//     * curDatas按次数排序后的结果，存储在sortedDatas中
//     */
//    List<Map.Entry<String, Integer>> sortedDatas;
//    private Counter<String> counter;    // 和sortedDatas配套使用
//
//
//    /**
//     * 更新sortedDatas
//     */
//    @Override
//    public void updateSortedDatas() {
//        counter = new Counter<String>();
//        for (String num : curDatas) {
//            counter.count(num);
//        }
//        // 默认词频统计
//        // TODO: 2017/2/21 这一块应该移到basicBarAddIn中去
//        Map<String, Integer> counterMap = counter.getAllKeysStatistics();
//        // TODO: 2017/2/21 下面代码重构进BarItem
//        sortedDatas = new ArrayList<Map.Entry<String, Integer>>(counterMap.entrySet());
//        Collections.sort(sortedDatas, new Comparator<Map.Entry<String, Integer>>() {
//            public int compare(Map.Entry<String, Integer> o1,
//                               Map.Entry<String, Integer> o2) {
//                if (sortReverse) {
//                    // 从小到大
//                    return (o1.getValue() - o2.getValue());
//                } else {
//                    // 从大到小
//                    return (o2.getValue() - o1.getValue());
//                }
//            }
//        });
//    }
//
//    @Override
//    public void updateValidCount() {
//        validCount=0;
//        for (String data:curDatas){
//            if (false) {
//                // 跳过空值不作处理
//                continue;
//            }
//            validCount++;
//        }
//    }
//
//    @Override
//    public void deleteRowsByIndex(List<Integer> curSelectedRowIndexs) {
//
//    }
//
//    @Override
//    public void keepRowsByIndex(List<Integer> curSelectedRowIndexs) {
//
//    }
//
//    @Override
//    public void changeRowsByIndex(List<Integer> curSelectedRowIndexs, Object replaceRowValue) {
//
//    }
//
//    public List<String> getOriDatas() {
//        return oriDatas;
//    }
//
//    public void setOriDatas(List<String> oriDatas) {
//        this.oriDatas = oriDatas;
//    }
//
//    public List<String> getCurDatas() {
//        return curDatas;
//    }
//
//    public void setCurDatas(List<String> curDatas) {
//        this.curDatas = curDatas;
//    }
//
//    public List<Map.Entry<String, Integer>> getSortedDatas() {
//        return sortedDatas;
//    }
//
//    public void setSortedDatas(List<Map.Entry<String, Integer>> sortedDatas) {
//        this.sortedDatas = sortedDatas;
//    }
}
