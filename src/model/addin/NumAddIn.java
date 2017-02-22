package model.addin;

import tools.Counter;
import tools.RegexTool;

import java.util.*;

/**
 * 处理num形式数据的插件
 * 可以用于展示平均数、最大最小值、中位数、标准差、四分位数等
 * Created by zsf on 2017/2/21.
 */
public class NumAddIn extends AddIn {
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

    private boolean sortReverse = false;

    private double min = 0.0;
    private double max = 0.0;

    private double sum = 0.0;
    private int validCount = 0;

    private double ave = 0.0;
    private double median = 0.0;
    private double standardDeviation = 0.0;

    // TODO: 2017/2/22 数据类型还是应该封装到类中
    public static final double errorValue = Double.NEGATIVE_INFINITY;
    public static final double emptyValue = Double.NaN;

    List<Integer> selectedIndex;

    public NumAddIn(List<String> oriDatas) {
        getOriDatas(oriDatas);
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
            if (data.equals(errorValue) || data.equals(emptyValue)) {
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
     * 将原始数据转换为有效数据(主要针对str->num)，将非数字数据记作errorValue，空值记作emptyValue
     */
    private void getOriDatas(List<String> strDatas) {
        oriDatas=new ArrayList<Double>();
        curDatas=new ArrayList<Double>();
        for (String str : strDatas) {
            if(str.isEmpty()){
                oriDatas.add(emptyValue);
            }else if(!RegexTool.isNum(str)){
                oriDatas.add(errorValue);
            }else {
                double num = Double.valueOf(str);
                oriDatas.add(num);
            }
        }
        curDatas.addAll(oriDatas);
    }

    /**
     * 更新sortedDatas
     */
    private void updateSortedDatas() {
        // FIXME: 2017/2/22 调用selectTopNPercentData之后还要更新validData，counter等，这块需要重新设计
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
     * 鼠标选中某级列tag，然后显示这几列的详情
     * @param indexs
     */
    public void selectTagByIndex(List<Integer> indexs){
        this.selectedIndex=indexs;
        System.out.println("Select Index"+indexs+":");
//        for (Integer index:indexs){
//            System.out.println(sortedDatas.get(index));
//        }
    }

    /**
     * 在curDatas中仅保留选中的这几列
     */
    public void keepOnlyCurSelectedIndex(){
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
        clearSelectedIndex();
    }

    /**
     * 在curDatas中删除选中的这几列
     */
    public void deleteCurSelectedIndex(){
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
        clearSelectedIndex();
    }

    /**
     * 替换当前选中tag列的值
     * @param value
     */
    public void replaceValue(Double value){
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
     * 恢复原始数据
     * 注:原始数据可以通过保存而被覆盖
     */
    public void resumeOriDatas(){
        curDatas=oriDatas;
        updateDataInfo();
        clearSelectedIndex();
    }

    /**
     * 每次操作之后，清空选中的tag列
     */
    private void clearSelectedIndex() {
        selectedIndex=new ArrayList<Integer>();
    }
    /**
     * 在当前有效数据中筛选出大于某个值的
     *
     * @param target
     */
    public void selectDataLargerThan(double target) {
        // 暂时不做
    }

    /**
     * 在当前有效数据中删选出小于某个值的
     *
     * @param target
     */
    public void selectDataSmallerThan(double target) {
        // 暂时不做
    }

    private void updateValidCount() {
        validCount=0;
        for (Double data:curDatas){
            if (data.equals(errorValue) || data.equals(emptyValue)) {
                continue;
            }
            validCount++;
        }
    }

    @Override
    public void commandShow() {
        showCounterMap();

        System.out.println(String.format("total:%d valid:%d max:%f min:%f sum:%f ave:%f median:%f sd:%f",
                curDatas.size(), validCount, min, max, sum, ave, median, standardDeviation));
    }

    /**
     * 绘制词频柱状图，默认从高到低排列
     */
    private void showCounterMap() {
        for (int i = 0; i < sortedDatas.size(); i++) {
            // TODO: 2017/2/21 图形界面show
            System.out.println(sortedDatas.get(i));
        }
    }
}
