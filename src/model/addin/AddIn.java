package model.addin;

import java.util.List;

/**
 * 悬浮在column上方的插件
 * Created by zsf on 2017/2/21.
 */
public interface AddIn {
    void commandShow();

//    // 下面3条可以想办法提出来
//    void updateValidCount();
//    void updateDataInfo();
//    void updateSortedDatas();

    void selectTopNPercentData(double percent);

    void clearSelectedIndex();
    void selectTagByIndex(List<Integer> indexs);
    void keepOnlyCurSelectedIndex();
    void deleteCurSelectedIndex();

    void replaceValue(Object o);

    void resumeOriDatas();
}
