package com.homvee.insuranceoctopus.utils;

import org.apache.commons.compress.utils.Lists;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    /**2003- 版本的excel*/
    private final static String EXCEL_V_2003_L =".xls";
    /**2007+ 版本的excel*/
    private final static String EXCEL_V_2007_U =".xlsx";


    /**
     * 描述：获取IO流中的数据，组装成List&lt;List&lt;Object&gt;&gt;对象
     * @param in,fileName
     * @return
     * @throws IOException
     */
    public static   List<List<Object>> getData(InputStream in, String fileName) throws Exception{
        List<List<Object>> list = null;

        //创建Excel工作薄
        Workbook work = getWorkbook(in,fileName);
        if(null == work){
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        org.apache.poi.ss.usermodel.Cell cell = null;

        list = Lists.newArrayList();
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if(sheet==null){continue;}

            //遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if(row==null||row.getFirstCellNum()==j){continue;}

                //遍历所有的列
                List<Object> li = Lists.newArrayList();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    li.add(getCellValue(cell));
                }
                list.add(li);
            }
        }
        work.close();
        return list;
    }

    public  static OutputStream download(String sheetName , List<Map<String,String>> data) throws IOException {
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet(sheetName);
        sheet.setDefaultColumnWidth(15);

        //表头行创建
        HSSFRow header = sheet.createRow(0);
        List<String>  titles = Lists.newArrayList(data.get(0).keySet().iterator());
        int column = 0;
        for (String title : titles){
            header.createCell(column++).setCellValue(title);
        }

        for (int r =0 ,rows = data.size(); r < rows; r ++){
            HSSFRow row = sheet.createRow(r + 1);
            Map<String,String> rowData = data.get(r);
            for (int c=0,len = titles.size() ; c < len ; c ++){
                row.createCell(c).setCellValue(rowData.getOrDefault(titles.get(c) , ""));
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        book.write(bos);
        return  bos;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * @param inStr,fileName
     * @return
     * @throws Exception
     */
    private static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception{
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(EXCEL_V_2003_L.equals(fileType)){
            wb = new HSSFWorkbook(inStr);
        }else if(EXCEL_V_2007_U.equals(fileType)){
            wb = new XSSFWorkbook(inStr);
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    private static   Object getCellValue(Cell cell){
        Object value = null;
        //格式化number String字符
        DecimalFormat df = new DecimalFormat("0");
        //日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        //格式化数字
        DecimalFormat df2 = new DecimalFormat("0.00");

        switch (cell.getCellType()) {
            case STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    value = df.format(cell.getNumericCellValue());
                }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
                    value = sdf.format(cell.getDateCellValue());
                }else{
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }
}
