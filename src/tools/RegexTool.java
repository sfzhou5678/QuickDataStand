package tools;

/**
 * Created by zsf on 2017/2/21.
 */
public class RegexTool {
    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
}
