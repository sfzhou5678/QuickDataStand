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
    List<String> oriDatas = new ArrayList<String>();
    List<Double> validDatas = new ArrayList<Double>();
    /**
     * 如果有需要可以选择升序会降序排列，保留原始数据，将排序后的数据存储到sortedDatas中
     */
    List<Map.Entry<Double,Integer>> sortedDatas;
    private Counter<Double> counter;    // 和sortedDatas配套使用

    private boolean sortReverse=false;

    private double min = 0.0;
    private double max = 0.0;

    private double sum = 0.0;
    private int validCount = 0;

    private double ave = 0.0;
    private double median = 0.0;
    private double standardDeviation = 0.0;


    public NumAddIn(List<String> oriDatas) {
        this.oriDatas = oriDatas;
        try {

            for (String str : oriDatas) {
                if (str.isEmpty() || !RegexTool.isNum(str)) {
                    // TODO: 2017/2/21 空值处理，不仅仅是num，其他数据也有这种问题
                    continue;
                }
                double num = Double.valueOf(str);

                sum += num;
                min = Math.min(min, num);
                max = Math.max(max, num);
                // TODO: 2017/2/21 中位数、标准差、四分卫等

                validDatas.add(num);
            }
            validCount = validDatas.size();
            ave = sum / validCount;
//            calculateMedian();
//            calculateSD();

            updateCounter();
        } catch (Exception e) {
            // TODO: 2017/2/21 如果分类错误，可能无法将oridata转换成numData
            e.printStackTrace();
        }
    }

    /**
     * 更新counter和sortedDatas
     */
    private void updateCounter() {
        // FIXME: 2017/2/22 调用selectTopNPercentData之后还要更新validData，counter等，这块需要重新设计
        counter=new Counter<Double>();
        for (Double num:validDatas){
            counter.count(num);
        }
        // 默认词频统计
        // TODO: 2017/2/21 这一块应该移到basicBarAddIn中去
        Map<Double,Integer> counterMap = counter.getAllKeysStatistics();
        // TODO: 2017/2/21 下面代码重构进BarItem
        sortedDatas =new ArrayList<Map.Entry<Double,Integer>>(counterMap.entrySet());
        Collections.sort(sortedDatas, new Comparator<Map.Entry<Double, Integer>>() {
            public int compare(Map.Entry<Double, Integer> o1,
                               Map.Entry<Double, Integer> o2) {
                if (sortReverse){
                    // 从小到大
                    return (o1.getValue() - o2.getValue());
                }else {
                    // 从大到小
                    return (o2.getValue() - o1.getValue());
                }
            }
        });
    }

    /**
     * 选出能构成前percent%权重的数据集
     * @param percent
     */
    public void selectTopNPercentData(double percent){
        List<Map.Entry<Double,Integer>> topPercentDatas=new ArrayList<Map.Entry<Double,Integer>>();
        int curSum=0;
        for (int i=0;i<sortedDatas.size();i++){
            curSum+=sortedDatas.get(i).getValue();
            topPercentDatas.add(sortedDatas.get(i));
            if (curSum>=(int)percent*validCount){
                break;
            }
        }
        sortedDatas=topPercentDatas;
        // FIXME: 2017/2/22 还要更新validData，counter等，这块需要重新设计
    }

    /**
     * 在当前有效数据中筛选出大于某个值的
     * @param target
     */
    public void selectDataLargerThan(double target){
        // 暂时不做
    }

    /**
     * 在当前有效数据中删选出小于某个值的
     * @param target
     */
    public void selectDataSmallerThan(double target){
        // 暂时不做
    }



    @Override
    public void commandShow() {
        showCounterMap();

        System.out.println(String.format("total:%d valid:%d max:%f min:%f sum:%f ave:%f median:%f sd:%f",
                oriDatas.size(), validCount, min, max, sum, ave, median, standardDeviation));
    }

    /**
     * 绘制词频柱状图，默认从高到低排列
     */
    private void showCounterMap() {
        for (int i=0;i<sortedDatas.size();i++){
            // TODO: 2017/2/21 图形界面show
            System.out.println(sortedDatas.get(i));
        }
    }
}
