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
        oriDatas=new ArrayList<Double>();
        curDatas=new ArrayList<Double>();
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
        List<Double> needKeepDatas = new ArrayList<Double>();
        int curSum = 0;
        for (int i = 0; i < sortedDatas.size(); i++) {
            curSum += sortedDatas.get(i).getValue();
            needKeepDatas.add(sortedDatas.get(i).getKey());
            if (curSum >= (int) (percent * validCount)) {
                break;
            }
        }
        List<Double> newValidDatas = new ArrayList<Double>();
        for (Double key : needKeepDatas) {
            for (Double data : curDatas) {
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
        List<Double> newCurDatas=new ArrayList<Double>();
        for (Integer index:selectedIndex){
            Double key=sortedDatas.get(index).getKey();
            for (Double data:curDatas){
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
        List<Double> newCurDatas=new ArrayList<Double>();
        for (Double data:curDatas){
            boolean needDelete=false;
            for (Integer index:selectedIndex){
                Double key=sortedDatas.get(index).getKey();
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
            Double key=sortedDatas.get(index).getKey();
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
        for (Double data : curDatas) {
            if (data.equals(NumAddIn.errorValue) || data.equals(NumAddIn.emptyValue)) {
                // 跳过空值不作处理
                continue;
            }
            sum += data;
            min = Math.min(min, data);
            max = Math.max(max, data);
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

    public void resumeOriDatas() {
        curDatas=oriDatas;
        updateDataInfo();
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
