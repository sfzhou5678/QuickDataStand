package model.addin;

/**
 * Created by hasee on 2017/2/23.
 */
public interface UnderObservation {
    void addWatcher(AddInObserver addInObserver);

    void removeWatcher(AddInObserver addInObserver);

    void notifyWatchers();
}
