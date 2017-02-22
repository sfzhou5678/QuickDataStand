package model;

import model.addin.AddIn;
import model.addin.NumAddIn;
import model.addin.StringAddIn;

import java.util.ArrayList;
import java.util.List;

/**
 * 每一列数据
 * Created by zsf on 2017/2/21.
 */
public class Column {
    public static final String TYPE_ABC = "TYPE_ABC";
    public static final String TYPE_NUM = "TYPE_NUM";
    public static final String TYPE_DATE = "TYPE_DATE";
    public static final String TYPE_TIME = "TYPE_TIME";
    public static final String TYPE_URL = "TYPE_URL";
    public static final String TYPE_LOCATION = "TYPE_LOCATION";

    private boolean hasColHeader = false;
    private String colHeader = "";

    /**
     * 数据类型，暂定包括abc、num、日期、时间、url、location
     */
    private String dataType = "";

    List<AddIn> addIns = new ArrayList<AddIn>();
    List<String> datas = new ArrayList<String>();

    /**
     * 在命令行显示此列具体内容
     */
    public void commandShow() {
        if (hasColHeader) {
            System.out.println(colHeader);
        }
        for (String data : datas) {
            System.out.println(data);
        }
        // TODO: 2017/2/21 1. show各行数据
        // TODO: 2017/2/21 2. showAddIn
    }

    /**
     * 预期根据某个char拆分所有文本
     * 比如Mr. Owen Harris",male,22 可按,拆分成3列
     *
     * @param character
     */
    public List<Column> splitCol(String character, int selectedIndex) {
        List<Column> newColumns = new ArrayList<Column>();
        try {
            int newColSize = datas.get(selectedIndex).split(character).length;
            if (newColSize > 0) {
                // 存储拆分之后的各列数据
                List<List<String>> splitedDataList = new ArrayList<List<String>>();
                for (int i = 0; i < newColSize; i++) {
                    List<String> dataList = new ArrayList<String>();
                    splitedDataList.add(dataList);
                }

                for (String data : datas) {
                    // 拆分每一行data进各新的col中去
                    String[] splitedData = data.split(character);
                    for (int i = 0; i < newColSize; i++) {
                        splitedDataList.get(i).add(splitedData[i]);
                    }
                }
                String[] newHeaders = null;
                if (hasColHeader) {
                    newHeaders = colHeader.split(character);
                }
                for (int i = 0; i < newColSize; i++) {
                    Column newColumn = new Column();
                    if (hasColHeader) {
                        newColumn.setHasColHeader(true);
                        newColumn.setColHeader(newHeaders[i]);
                    }
                    newColumn.setDatas(splitedDataList.get(i));
                    if (i==5){
                        // FIXME: 2017/2/21 特例：第五列刚好是age
                        newColumn.setDataType(analyzeDataType(splitedDataList.get(i)));
                        newColumn.generateAddInByType();
                    }else {
                        newColumn.setDataType(TYPE_ABC);
                        newColumn.generateAddInByType();
                    }
                    newColumns.add(newColumn);
                }
            } else {
                // TODO: 2017/2/21 无法分割，给出提示
            }
        } catch (Exception e) {
            // TODO: 2017/2/21 可能会出现某行数据拆分失败导致数组越界的情况
            e.printStackTrace();
        }
        return newColumns;
    }

    /**
     * 在命令行中显示addIn
     */
    public void commandShowAddIn() {
       for (AddIn addin:addIns){
           addin.commandShow();
       }
    }

    /**
     * 根据当前的数据类型生成type
     */
    private void generateAddInByType() {
        if (!dataType.isEmpty()) {
            AddIn addIn = null;
            if (dataType.equals(TYPE_NUM)) {
                addIn = new NumAddIn(datas);
            }else if (dataType.equals(TYPE_ABC)){
                addIn=new StringAddIn(datas);
            }
            addIns.add(addIn);
        } else {
            // TODO: 2017/2/21 未知type
        }
    }

    /**
     * 分析某一组数据属于那种类别
     * 具体分析的方法：
     *
     * @param strings
     * @return
     */
    private String analyzeDataType(List<String> strings) {
        return TYPE_NUM;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isHasColHeader() {
        return hasColHeader;
    }

    public void setHasColHeader(boolean hasColHeader) {
        this.hasColHeader = hasColHeader;
    }

    public String getColHeader() {
        return colHeader;
    }

    public void setColHeader(String colHeader) {
        this.colHeader = colHeader;
    }

    public List<AddIn> getAddIns() {
        return addIns;
    }

    public void setAddIns(List<AddIn> addIns) {
        this.addIns = addIns;
    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }
}
