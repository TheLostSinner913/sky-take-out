package com.sky;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/7 19:56
 */
public class ExcelTest {
    public static void write() throws IOException {
        XSSFWorkbook excel = new XSSFWorkbook();
        XSSFSheet sheet = excel.createSheet("TheLostSinner");
        XSSFRow row0 = sheet.createRow(0);
        row0.createCell(1).setCellValue("姓名");
        row0.createCell(2).setCellValue("年龄");
        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(1).setCellValue("陶大胖");
        row1.createCell(2).setCellValue("18");
        FileOutputStream fos=new FileOutputStream(new File("D:\\Java\\sky-take-out\\sky-server\\src\\main\\resources\\static\\TheLostSinner.xlsx"));
        excel.write(fos);
        fos.flush();
        fos.close();
        excel.close();
    }
    public static  void  read() throws IOException {
        FileInputStream fis=new FileInputStream(new File("D:\\Java\\sky-take-out\\sky-server\\src\\main\\resources\\static\\TheLostSinner.xlsx"));
        XSSFWorkbook excel = new XSSFWorkbook(fis);

    }

    public static void main(String[] args) {

    }
}
