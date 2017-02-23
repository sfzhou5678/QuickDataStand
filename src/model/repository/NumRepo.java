package model.repository;

import model.addin.NumAddIn;
import tools.Counter;
import tools.RegexTool;

import java.util.*;

/**
 * 专门处理数字数据，仅限于age、height等，不包括20140701000215这种日期数据
 * Created by hasee on 2017/2/22.
 */
public class NumRepo extends DataRepo {
    /**
     * 原始输入的数据，用于恢复原状
     */
    List<Object> oriDatas = new ArrayList<Object>();

    /**
     * 当前保留下来的所有数据，但是包含空值和错误值
     */
    List<Object> curDatas = new ArrayList<Object>();

    /**
     * curDatas按次数排序后的结果，存储在sortedDatas中
     */
    List<Map.Entry<Object, Integer>> sortedDatas;
    private Counter<Object> counter;    // 和sortedDatas配套使用

    private double min = 0.0;
    private double max = 0.0;
    private double sum = 0.0;

    private double ave = 0.0;
    private double median = 0.0;
    private double standardDeviation = 0.0;

    public NumRepo(List<String> displayDatas) {
        getOriDatas(displayDatas);
    }

    /**
     * 将原始数据转换为有效数据(主要针对str->num)，将非数字数据记作errorValue，空值记作emptyValue
     */
    private void getOriDatas(List<String> strDatas) {
        oriDatas=new ArrayList<Object>();
        curDatas=new ArrayList<Object>();
        for (String str : strDatas) {
            if(str.isEmpty()){
                oriDatas.add(NumAddIn.emptyValue);
            }else if(!RegexTool.isNum(str)){
                oriDatas.add(NumAddIn.errorValue);
            }else {
                double num = Double.valueOf(str);
                oriDatas.add(num);
            }
        }
        curDatas.addAll(oriDatas);
        updateDataInfo();
    }

    /**
     * 选出能构成前percent%权重的数据集
     *
     * @param percent
     */
    public void selectTopNPercentData(double percent) {
        List<Object> needKeepDatas = new ArrayList<Object>();
        int curSum = 0;
        for (int i = 0; i < sortedDatas.size(); i++) {
            curSum += sortedDatas.get(i).getValue();
            needKeepDatas.add(sortedDatas.get(i).getKey());
            if (curSum >= (int) (percent * validCount)) {
                break;
            }
        }
        List<Object> newValidDatas = new ArrayList<Object>();
        for (Object key : needKeepDatas) {
            for (Object data : curDatas) {
                if (key.equals(data)) {
                    newValidDatas.add(data);
                }
            }
        }
        curDatas=newValidDatas;
        updateDataInfo();
    }

    /**
     * 在curDatas中仅保留选中的这几列
     */
    public void keepOnlyCurSelectedIndex(List<Integer> selectedIndex) {
        List<Object> newCurDatas=new ArrayList<Object>();
        for (Integer index:selectedIndex){
            Object key=sortedDatas.get(index).getKey();
            for (Object data:curDatas){
                if (data.equals(key)){
                    newCurDatas.add(data);
                }
            }
        }
        curDatas=newCurDatas;
        updateDataInfo();
    }

    /**
     * 在curDatas中删除选中的这几列
     */
    public void deleteCurSelectedIndex(List<Integer> selectedIndex) {
        List<Object> newCurDatas=new ArrayList<Object>();
        for (Object data:curDatas){
            boolean needDelete=false;
            for (Integer index:selectedIndex){
                Object key=sortedDatas.get(index).getKey();
                if (data.equals(key)){
                    needDelete=true;
                }
            }
            if (!needDelete){
                newCurDatas.add(data);
            }
        }
        curDatas=newCurDatas;
        updateDataInfo();
    }

    /**
     * 替换当前选中tag列的值
     */
    public void replaceValue(Object o, List<Integer> selectedIndex) {
        Double value=(Double)o;
        for (Integer index:selectedIndex){
            Object key=sortedDatas.get(index).getKey();
            for (int i=0;i<curDatas.size();i++){
                if (curDatas.get(i).equals(key)){
                    curDatas.set(i,value);
                }
            }
        }
        updateDataInfo();
    }

    /**
     * 在保证validDatas已经更新过的前提下，更新当前数据的状态
     * */
    private void updateDataInfo() {
        updateValidCount();
        sum = 0;
        min = Double.POSITIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
        for (Object data : curDatas) {
            if (data.equals(NumAddIn.errorValue) || data.equals(NumAddIn.emptyValue)) {
                // 跳过空值不作处理
                continue;
            }
            sum += (Double)data;
            min = Math.min(min, (Double)data);
            max = Math.max(max, (Double)data);
        }
        ave = sum / validCount;
//            calculateMedian();
//            calculateSD();

        updateSortedDatas();
    }


    /**
     * NOTE:目前的技术还没发将ValidCounter放到Repo中
     * 如果能做一个通用的Error和Empty标签和通用的Error和Emtpy检测方法出来就可以
     */
    @Override
    public void updateValidCount() {
        validCount=0;
        for (Object data:curDatas){
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
        counter = new Counter<Object>();
        for (Object num : curDatas) {
            counter.count(num);
        }
        // 默认词频统计
        // TODO: 2017/2/21 这一块应该移到basicBarAddIn中去
        Map<Object, Integer> counterMap = counter.getAllKeysStatistics();
        // TODO: 2017/2/21 下面代码重构进BarItem
        sortedDatas = new ArrayList<Map.Entry<Object, Integer>>(counterMap.entrySet());
        Collections.sort(sortedDatas, new Comparator<Map.Entry<Object, Integer>>() {
            public int compare(Map.Entry<Object, Integer> o1,
                               Map.Entry<Object, Integer> o2) {
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

    public void resumeOriDatas() {
        curDatas=oriDatas;
        updateDataInfo();
    }

    @Override
    public void deleteRowsByIndex(List<Integer> curSelectedRowIndexs) {
        for (int i=curSelectedRowIndexs.size()-1;i>=0;i--){
            curDatas.remove((int)curSelectedRowIndexs.get(i));
        }
        updateDataInfo();
    }

    @Override
    public void keepRowsByIndex(List<Integer> curSelectedRowIndexs) {
        List<Object> keptDatas=new ArrayList<Object>();
        for (int index:curSelectedRowIndexs){
            keptDatas.add(curDatas.get(index));
        }
        curDatas=keptDatas;
        updateDataInfo();
    }

    @Override
    public void changeRowsByIndex(List<Integer> curSelectedRowIndexs, Object replaceRowValue) {
        for (int index:curSelectedRowIndexs){
            curDatas.set(index,replaceRowValue);
        }
        updateDataInfo();
    }



    public List<Object> getOriDatas() {
        return oriDatas;
    }

    public void setOriDatas(List<Object> oriDatas) {
        this.oriDatas = oriDatas;
    }

    public List<Object> getCurDatas() {
        return curDatas;
    }

    public void setCurDatas(List<Object> curDatas) {
        this.curDatas = curDatas;
    }

    public List<Map.Entry<Object, Integer>> getSortedDatas() {
        return sortedDatas;
    }

    public void setSortedDatas(List<Map.Entry<Object, Integer>> sortedDatas) {
        this.sortedDatas = sortedDatas;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getSum() {
        return sum;
    }

    public double getAve() {
        return ave;
    }

    public double getMedian() {
        return median;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }
}
