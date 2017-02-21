package model.addin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsf on 2017/2/21.
 */
public class BasicBarAddIn extends AddIn {

    // TODO: 2017/2/21 注意str以外格式的数据(num、date等等)，num1.234444和1.2344555若取小数点后2位应为同样数据，但str无法判断
    List<String> datas=new ArrayList<String>();
    List<String> validDatas=new ArrayList<String>();

    List<String> sortedDatas=new ArrayList<String>();

    public BasicBarAddIn(List<String> datas) {
        this.datas = datas;
        for (String data:datas){
            if (!data.isEmpty()){
                validDatas.add(data);
            }
        }
    }

    @Override
    public void commandShow() {
        // FIXME: 2017/2/21 现在默认全都是str格式的数据
    }
}
