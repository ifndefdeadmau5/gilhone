package com.SeongMin.GoodProduct.activity.util;



import java.io.File;
import java.io.FileInputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by SeongMin on 2015-01-22.
 */
public class ExcelParser {
    private int result = -1;

    public int getResult() {
        return result;
    }

    public ExcelParser() {

        result= 0;

    }

    public String readExcel(){
        String resultString = "default";
        String[][] data = null;
        Sheet sheet = null;
        Workbook workbook = null;
        try {
            FileInputStream is = new FileInputStream(new File("C:\\Users\\SeongMin\\AndroidStudioProjects\\StandardTech\\GoodProduct\\GR\\src\\main\\res\\raw\\grcompany1412031.xls"));//File filePath = new File("");
            workbook = Workbook.getWorkbook(is);
            result = 3;
            if (workbook != null) {
                sheet = workbook.getSheet(0);
                result = 1;
                if (sheet != null) {
                    int rowLength = sheet.getRows();
                    int columnLength = sheet.getColumns();

                    data = new String[rowLength][columnLength];

                    for (int i = 00; i < rowLength; i++) {
                        for (int j = 0; j < columnLength; j++) {
                            Cell cell = sheet.getCell(j, i);
                            data[i][j] = cell.getContents();
                        }
                    }
                }
            }

            //result = 4;
        } catch (Exception e) {
            e.printStackTrace();

            resultString = e.toString();
        } finally {
            if (workbook != null) {
                workbook.close();
            }

        }
        return resultString;
    }
}
