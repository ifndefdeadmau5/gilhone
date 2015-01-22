package com.SeongMin.GoodProduct.global;

import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by SeongMin on 2015-01-22.
 */
public class ExcelData extends Application {
    private int result = -1;
    String[][] data = null;
    public int getResult() {
        return result;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String resultString = "default";

        Sheet sheet = null;
        Workbook workbook = null;
        try {
            InputStream is = getBaseContext().getResources().getAssets().open("grcompany1412031.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {
                sheet = workbook.getSheet(0);
                if (sheet != null) {
                    int rowLength = sheet.getRows();
                    int columnLength = sheet.getColumns();

                    data = new String[rowLength][columnLength];

                    for (int i = 00; i < rowLength; i++) {
                        for (int j = 0; j < columnLength; j++) {
                            Cell cell = sheet.getCell(j, i);
                            data[i][j] = cell.getContents();
                            Log.i("excel", " "+data[i][j]+" ");
                        }
                        Log.i("excel", "\n");
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
        //return resultString;

    }

//    public String readExcel() {
//
//    }

}