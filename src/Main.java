import model.Column;
import model.addin.NumAddIn;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String filePath="QDS DATA\\★★Airbnb New User Bookings\\test_users.csv";
        Column oriColumn=readFile(filePath);
//        oriColumn.commandShow();

        List<Column> newColumns=oriColumn.splitCol(",",0);
        for (Column column:newColumns){
//            column.commandShow();
        }

        newColumns.get(5).commandShowAddIn();
        NumAddIn numAddIn= (NumAddIn) newColumns.get(5).getAddIns().get(0);
        numAddIn.selectTopNPercentData(0.9);
        newColumns.get(5).commandShowAddIn();

    }

    /**
     * 从原始文件中读取数据，并展示成col形式
     * @param filePath
     */
    private static Column readFile(String filePath) {

        Column column=null;
        BufferedReader bufferedReader = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
        try {
            bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

            column=new Column();
            List<String> datas=new ArrayList<String>();
            String str = "";
            int count=0;
            while ((str = bufferedReader.readLine()) != null) {
                datas.add(str);
                if (count++>100){
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
