package tools;

import java.util.List;

import static model.Column.TYPE_NUM;

/**
 * Created by zsf on 2017/2/21.
 */
public class RegexTool {
    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 分析某一组数据属于那种类别
     * 具体分析的方法：
     *
     * @param strings
     * @return
     */
    public static String analyzeDataType(List<String> strings) {
        // TODO: 2017/2/22 加入TYPE分析, 可以用机器学习
        return TYPE_NUM;
    }
}
