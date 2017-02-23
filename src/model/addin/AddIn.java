package model.addin;

import java.util.ArrayList;
import java.util.List;

/**
 * 悬浮在column上方的插件
 * Created by zsf on 2017/2/21.
 */
public abstract class AddIn implements UnderObservation {
    public abstract void commandShow();

    public abstract void selectTopNPercentData(double percent);

    public abstract void clearSelectedIndex();
    public abstract void selectTagByIndex(List<Integer> indexs);
    public abstract void keepOnlyCurSelectedIndex();
    public abstract void deleteCurSelectedIndex();

    public abstract void replaceValue(Object o);

    public abstract void resumeOriDatas();

    public abstract void updateUI();

    private List<AddInObserver> observerList = new ArrayList<AddInObserver>();

    @Override
    public void addWatcher(AddInObserver addInObserver) {
        observerList.add(addInObserver);
    }

    @Override
    public void removeWatcher(AddInObserver addInObserver) {
        observerList.remove(addInObserver);
    }

    @Override
    public void notifyWatchers() {
        for (AddInObserver addInObserver :observerList){
            addInObserver.update();
        }
    }
}
