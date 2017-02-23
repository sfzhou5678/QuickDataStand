package model.addin;

import model.repository.DataRepo;
import model.repository.NumRepo;

import java.util.*;

/**
 * 处理num形式数据的插件
 * 可以用于展示平均数、最大最小值、中位数、标准差、四分位数等
 * Created by zsf on 2017/2/21.
 */
public class NumAddIn extends AddIn implements UnderObservation {
    List<Integer> selectedIndex;

    private double min = 0.0;
    private double max = 0.0;
    private double sum = 0.0;

    private double ave = 0.0;
    private double median = 0.0;
    private double standardDeviation = 0.0;

    // TODO: 2017/2/22 数据类型还是应该封装到类中
    public static final double errorValue = Double.NEGATIVE_INFINITY;
    public static final double emptyValue = Double.NaN;

    NumRepo numRepo;

    public NumAddIn() {
    }

    public NumAddIn(DataRepo numRepo,AddInObserver addInObserver) {
        this.numRepo = (NumRepo) numRepo;
        this.addWatcher(addInObserver);
//        updateUI();
    }

    /**
     * NOTE:计划是在这里更新界面,可能在这里用不到这个函数
     */
    @Override
    public void updateUI() {
        System.out.println("更新addIn的UI");
    }

    /**
     * 选出能构成前percent%权重的数据集
     * @param percent
     */
    @Override
    public void selectTopNPercentData(double percent) {
        numRepo.selectTopNPercentData(percent);
        updateUI();
        notifyWatchers();
    }

    /**
     * 鼠标选中某级列tag，然后显示这几列的详情
     * @param indexs
     */
    @Override
    public void selectTagByIndex(List<Integer> indexs){
        this.selectedIndex=indexs;
    }

    /**
     * 在curDatas中仅保留选中的这几列
     */
    @Override
    public void keepOnlyCurSelectedIndex(){
        numRepo.keepOnlyCurSelectedIndex(selectedIndex);
        updateUI();
        notifyWatchers();
        clearSelectedIndex();
    }

    /**
     * 在curDatas中删除选中的这几列
     */
    @Override
    public void deleteCurSelectedIndex(){
        numRepo.deleteCurSelectedIndex(selectedIndex);
        updateUI();
        notifyWatchers();
        clearSelectedIndex();
    }

    /**
     * 替换当前选中tag列的值
     */
    @Override
    public void replaceValue(Object o){
        numRepo.replaceValue(o,selectedIndex);
        updateUI();
        notifyWatchers();
    }

    /**
     * 恢复原始数据
     * 注:原始数据可以通过保存而被覆盖
     */
    @Override
    public void resumeOriDatas(){
        numRepo.resumeOriDatas();
        updateUI();
        notifyWatchers();
        clearSelectedIndex();
    }
    /**
     * 每次操作之后，清空选中的tag列
     */
    @Override
    public void clearSelectedIndex() {
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

    @Override
    public void commandShow() {
        showCounterMap();

        System.out.println(String.format("total:%d valid:%d max:%f min:%f sum:%f ave:%f median:%f sd:%f",
                numRepo.getCurDatas().size(), numRepo.getValidCount(),numRepo.getMin(),numRepo.getMax(),
                numRepo.getSum(), numRepo.getAve(), numRepo.getMedian(), numRepo.getStandardDeviation()));
    }

    /**
     * 绘制词频柱状图，默认从高到低排列
     */
    private void showCounterMap() {
        for (int i = 0; i < numRepo.getSortedDatas().size(); i++) {
            // TODO: 2017/2/21 图形界面show
            System.out.println(numRepo.getSortedDatas().get(i));
        }
    }

    public List<Integer> getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(List<Integer> selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
