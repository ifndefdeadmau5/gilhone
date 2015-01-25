package com.SeongMin.GoodProduct.global;

import android.app.Application;
import android.util.Log;

import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by SeongMin on 2015-01-22.
 */
public class ExcelData extends Application {
    private int result = -1;
    String[][] data = null;
    private db dbAdapter;



    public int getResult() {
        return result;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("mytag", "DatabaseTest :: onCreate()");

        String resultString = "default";
        this.dbAdapter = new db(this);

        dbAdapter.open();
        if( dbAdapter.isEmpty() ) {
            // DB 파일이 없다면 엑셀로부터 생성
            Log.i("mytag", "비어쓰");

            copyExcelDataToDatabase();
        }
        dbAdapter.close();
    }
    /*
     *                                  Copy
     *        Excel  -------------------------------------------> SQLite
     *
     */
    private void copyExcelDataToDatabase() {
        Log.w("ExcelToDatabase", "copyExcelDataToDatabase()");

        Workbook workbook = null;
        Sheet sheet = null;


        try {
            InputStream is = getBaseContext().getResources().getAssets().open("grcompany1412031.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {
                sheet = workbook.getSheet(0);

                if (sheet != null) {

                    int nMaxColumn = sheet.getColumns();
                    int nRowStartIndex = 0;
                    int nRowEndIndex = sheet.getColumn(nMaxColumn - 1).length - 1;
                    int nColumnStartIndex = 2;
                    int nColumnEndIndex = sheet.getRow(2).length - 1;

                    dbAdapter.open();
                    for (int nRow = nRowStartIndex; nRow <= nRowEndIndex; nRow++) {
                        String code = sheet.getCell(nColumnStartIndex, nRow).getContents();
                        String comp = sheet.getCell(nColumnStartIndex + 8, nRow).getContents();
                        Log.i("excel",code+" "+comp);
                        dbAdapter.createNote(code, comp);
                    }
                    dbAdapter.close();
                } else {
                    System.out.println("Sheet is null!!");
                }
            } else {
                System.out.println("WorkBook is null!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

}