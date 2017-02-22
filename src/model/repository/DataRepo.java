package model.repository;

import tools.Counter;

import java.util.*;

/**
 * 大列的数据仓库
 * NOTE:暂时还无法使用，等待高级的Valid技巧
 * Created by hasee on 2017/2/22.
 */
public abstract class DataRepo {
    protected boolean sortReverse = false;
    protected int validCount = 0;

    /**
     * 更新sortedDatas
     */
    public abstract void updateSortedDatas();

    /**
     * NOTE:目前的技术还没发将ValidCounter放到Repo中
     * 如果能做一个通用的Error和Empty标签和通用的Error和Emtpy检测方法出来就可以
     */
    public abstract void updateValidCount();


    public boolean isSortReverse() {
        return sortReverse;
    }

    public void setSortReverse(boolean sortReverse) {
        this.sortReverse = sortReverse;
    }

    public int getValidCount() {
        return validCount;
    }

    public void setValidCount(int validCount) {
        this.validCount = validCount;
    }
}
