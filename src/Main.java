import model.Column;
import model.addin.AddIn;
import model.addin.NumAddIn;
import model.addin.StringAddIn;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String filePath="QDS DATA\\★★Airbnb New User Bookings\\test_users.csv";
        Column oriColumn=readFile(filePath);
//        oriColumn.commandShow();

        // 选择分隔符进行分割
        List<Column> newColumns=oriColumn.splitCol(",",0);
        // 展示分割完的数据
        for (Column column:newColumns){
//            column.commandShow();
        }

        // 选中某一列进行查看，commandShowAddIn()全都可以删除
        Column curSelectedCol=newColumns.get(5);
//        Column curSelectedCol=newColumns.get(14);
        curSelectedCol.commandShowAddIn();

        // region # AddIn功能测试
//        AddIn addIn=curSelectedCol.getAddIn();
//
//        System.out.println("=======选择构成前80%权重的数据tag=======");
//        addIn.selectTopNPercentData(0.9);
//        curSelectedCol.commandShowAddIn();
//
//        // 选择某几列index
//        List<Integer> indexs=new ArrayList<Integer>();
//        indexs.add(0);
////        indexs.add(1);
//
//        System.out.println("=======删除选中列的tag=======");
//        addIn.selectTagByIndex(indexs);
//        addIn.deleteCurSelectedIndex();
//        curSelectedCol.commandShowAddIn();
//
//        System.out.println("=======转换类型=======");
////        if(curSelectedCol.changeType(Column.TYPE_NUM)){
////            System.out.println("转换类型成功");
////            addIn=curSelectedCol.getAddIn();
////        }else {
////            System.out.println("转换类型失败，请检查数据内容是否正确");
////        }
//        System.out.println("=======仅保留选中列的tag=======");
//        addIn.selectTagByIndex(indexs);
//        addIn.keepOnlyCurSelectedIndex();
//        curSelectedCol.commandShowAddIn();
//
//        System.out.println("=======替换选中tag列的值=======");
//        addIn.selectTagByIndex(indexs);
//        Object replaceValue=null;
//        if (curSelectedCol.getDataType().equals(Column.TYPE_NUM)){
//            replaceValue=new Double(0.5);
//        }else{
//            replaceValue=new String("Abc");
//        }
//        addIn.replaceValue(replaceValue);
//        curSelectedCol.commandShowAddIn();
//
////        // FIXME: 2017/2/22 这个应该不属于addin？
//        System.out.println("=======还原原始数据=======");
//        addIn.resumeOriDatas();
//        curSelectedCol.commandShowAddIn();
        // endregion

        // region # Column功能测试
        // TODO: 2017/2/23 必须保证index是升序的
        List<Integer> rowIndexs=new ArrayList<Integer>();
        rowIndexs.add(0);
        rowIndexs.add(1);
        curSelectedCol.commandShow();

        curSelectedCol.selectRowByIndex(rowIndexs);
//        curSelectedCol.callPBESystem();

//        System.out.println("=======删除选中行=======");
//        curSelectedCol.deleteRowsByIndex();
//        curSelectedCol.commandShow();

//        System.out.println("=======保留选中行=======");
//        curSelectedCol.keepRowsByIndex();
//        curSelectedCol.commandShow();

//
//        Object replaceRowValue=null;
//        if (curSelectedCol.getDataType().equals(Column.TYPE_NUM)){
//            replaceRowValue=new Double(0.5);
//        }else{
//            replaceRowValue=new String("Abc");
//        }
//        System.out.println("=======保留选中行=======");
//        curSelectedCol.changeRowsByIndex(replaceRowValue);
//        curSelectedCol.commandShow();
        // endregion

    }

    /**
     * 从原始文件中读取数据，并展示成col形式
     * @param filePath
     */
    private static Column readFile(String filePath) {

        Column column=null;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

            column=new Column();
            List<String> datas=new ArrayList<String>();
            int count=0;
            String str = "";
            while ((str = bufferedReader.readLine()) != null) {
                datas.add(str);
                if (count++>=100){
                    break;
                }
            }
            if (hasColHeader(datas)){
                column.setColHeader(datas.get(0));
                column.setHasColHeader(true);
                datas.remove(0);
                column.setDatas(datas);
            }
        } catch (FileNotFoundException e) {
            System.out.println("找不到指定文件");
        } catch (IOException e) {
            System.out.println("读取文件失败");
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return column;
    }

    /**
     * 判断index=0和1两句话的相似度，相似度较高就返回true
     * 因为如果是colHeader,通常会是这种格式：Name,Sex,Age，
     * 而实际数据则会是：Mr. Owen Harris",male,22
     * @param datas
     * @return
     */
    private static boolean hasColHeader(List<String> datas) {
        return true;
    }
}
