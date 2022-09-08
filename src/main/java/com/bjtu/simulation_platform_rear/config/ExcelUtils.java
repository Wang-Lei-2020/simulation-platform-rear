package com.bjtu.simulation_platform_rear.config;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Summer_DM
 * @Summary TODO  操作Excel的工具类
 * @Version 1.0
 * @Date 2021/9/16 下午 03:44
 **/
public class ExcelUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
    //Map<String,Object> map = new HashMap<String, Object>();

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    private final static String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 根据文件后缀名类型获取对应的工作簿对象
     * @param inputStream 读取文件的输入流
     * @param fileType 文件后缀名类型（xls或xlsx）
     * @return 包含文件数据的工作簿对象
     * @throws IOException
     */
    public static Workbook getWorkbook(InputStream inputStream, String fileType) throws Exception {
        Workbook workbook = null;
        //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
        if (fileType.equalsIgnoreCase(XLS)) {
            //2003
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileType.equalsIgnoreCase(XLSX)) {
            //2007及以上
            workbook = new XSSFWorkbook(inputStream);
        }else {
            throw new Exception("请上传excel文件！");
        }
        return workbook;
    }

    /**
     * 读取Excel文件内容
     * @param fileName 要读取的Excel文件所在路径
     * @return 读取结果列表，读取失败时返回null
     */
    public static List<List<String>> readExcel(String fileName) {

        Workbook workbook = null;
        FileInputStream inputStream = null;

        try {
            // 获取Excel后缀名
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            // 获取Excel文件
            File excelFile = new File(fileName);
            if (!excelFile.exists()) {
                logger.warn("指定的Excel文件不存在！");
            }

            // 获取Excel工作簿
            inputStream = new FileInputStream(excelFile);
            workbook = getWorkbook(inputStream, fileType);

            // 读取excel中的数据

            return parseExcel(workbook);
        } catch (Exception e) {
            logger.warn("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
            return null;
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.warn("关闭数据流出错！错误信息：" + e.getMessage());
                return null;
            }
        }
    }

    /**
     * 读取Excel文件内容
     * @param file 上传的Excel文件
     * @return 读取结果列表，读取失败时返回null
     */
    public static List<List<String>> readExcel(MultipartFile file) {

        Workbook workbook = null;

        try {
            //判断文件是否存在
            if(null == file){
                logger.warn("解析Excel失败，文件不存在！");
                return null;
            }
            // 获取Excel后缀名
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty() || fileName.lastIndexOf(".") < 0) {
                logger.warn("解析Excel失败，因为获取到的Excel文件名非法！");
                return null;
            }
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

            // 获取Excel工作簿
            workbook = getWorkbook(file.getInputStream(), fileType);

            return parseExcel(workbook);
        } catch (Exception e) {
            logger.warn("解析Excel失败，文件名：" + file.getOriginalFilename() + " 错误信息：" + e.getMessage());
            return null;
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
            } catch (Exception e) {
                logger.warn("关闭数据流出错！错误信息：" + e.getMessage());
                return null;
            }
        }
    }


    /**
     * 解析Excel数据
     * @param workbook Excel工作簿对象
     * @return 解析结果
     */
    private static List<List<String>> parseExcel(Workbook workbook) {
//        List resultDataList = new ArrayList<>();
        //获取所有的工作表的的数量
//        int numOfSheet = workbook.getNumberOfSheets();
//        System.out.println(numOfSheet+"--->numOfSheet");
        int numOfSheet = 1;
        // 解析sheet,此处会读取所有脚页的行数据，若只想读取指定页，不要for循环，直接给sheetNum赋值，
        // 脚页从0开始（通常情况Excel都只有一页，所以此处未进行进一步处理）
        List<List<String>> rowList=new ArrayList<>();
        for (int sheetNum = 0; sheetNum < numOfSheet; sheetNum++) {
            //获取一个sheet也就是一个工作本。
            Sheet sheet = workbook.getSheetAt(sheetNum);

            // 校验sheet是否合法
            if (sheet == null) {
                continue;
            }
            //获取一个sheet有多少Row
            //int lastRowNum = sheet.getLastRowNum();
            //if(lastRowNum == 0) {
            //    continue;
            //}
            // 获取第一行数据
            int firstRowNum = sheet.getFirstRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            if (null == firstRow) {
                logger.warn("解析Excel失败，在第一行没有读取到任何数据！");
            }

            // 解析每一行的数据，构造数据对象
            int rowStart = firstRowNum; //获取第几行
            //获得当前行的列数
            int rowEnd = sheet.getPhysicalNumberOfRows();

            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row row = sheet.getRow(rowNum);

                if (null == row) {
                    continue;
                }
                //列List
                List<String> cellList=new ArrayList<>();
                    //转换为List数组
                for (int cellNum=0;cellNum< row.getLastCellNum();cellNum++){
                    cellList.add(convertCellValueToString(row.getCell(cellNum)));
                }
                    rowList.add(cellList);
            }

//                List resultData = convertRowToData(row);
//                if (null == resultData) {
//                    logger.warn("第 " + row.getRowNum() + "行数据不合法，已忽略！");
//                    continue;
//                }
//                resultDataList.add(resultData);
//            }
        }

        return rowList;
    }

    /**
     * 将单元格内容转换为字符串
     * @param cell
     * @return
     */
    private static String convertCellValueToString(Cell cell) {
        String returnValue = null;
        if(cell==null){
            return returnValue;
        }
        //如果当前单元格内容为日期类型，需要特殊处理
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if(dataFormatString.equals("m/d/yy")){
            returnValue = new SimpleDateFormat(DATE_FORMAT).format(cell.getDateCellValue());
            return returnValue;
        }
        switch (cell.getCellType()) {
            case NUMERIC:   //数字
                Double doubleValue = cell.getNumericCellValue();

                // 格式化科学计数法，取一位整数
                DecimalFormat df = new DecimalFormat("0");
                returnValue = df.format(doubleValue);
                break;
            case STRING:    //字符串
                returnValue = cell.getStringCellValue();
                break;
            case BOOLEAN:   //布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                returnValue = booleanValue.toString();
                break;
            case BLANK:     // 空值
                break;
            case FORMULA:   // 公式
                returnValue = cell.getCellFormula();
                break;
            case ERROR:     // 故障
                break;
            default:
                break;
        }
        return returnValue;
    }

    /**
     * 提取每一行中需要的数据，构造成为一个结果数据对象
     *将excel的内容赋给实体类model
     * 当该行中有单元格的数据为空或不合法时，忽略该行的数据
     *
     * @param row 行数据
     * @return 解析后的行数据对象，行数据错误时返回null
     */
//    private static T convertRowToData(Row row) {
//        T resultData;
//
//        Cell cell;
//        int cellNum = 0;
//
//        //获取书籍名称
//        cell = row.getCell(cellNum++);
//        String bookName = convertCellValueToString(cell);
//        if (null == bookName || "".equals(bookName)) {
//            // 书籍名称为空
//            resultData.setBookname(bookName);
//        } else {
//            resultData.setBookname(bookName);
//        }
//
//        // 获取书籍作者
//        cell = row.getCell(cellNum++);
//        String author = convertCellValueToString(cell);
//        if (null == author || "".equals(author)) {
//            // 书籍作者为空
//            resultData.setAuthor(author);
//        } else {
//            resultData.setAuthor(author);
//        }
//
//        // 获取书籍类型
//        cell = row.getCell(cellNum++);
//        String type = convertCellValueToString(cell);
//        if (null == type || "".equals(type)) {
//            // 书籍类型为空
//            resultData.setBooktype(type);
//        } else {
//            resultData.setBooktype(type);
//        }
//
//        //获取出版单位
//        cell = row.getCell(cellNum++);
//        String publisher = convertCellValueToString(cell);
//        if (null == publisher || "".equals(publisher)) {
//            // 出版单位为空
//            resultData.setPublisher(publisher);
//        } else {
//            resultData.setPublisher(publisher);
//        }
//
//        //获取出版时间
//        cell = row.getCell(cellNum++);
//        String publicationdate = convertCellValueToString(cell);
//        if (null == publicationdate || "".equals(publicationdate)) {
//            // 出版时间为空
//            resultData.setPublicationdate(publicationdate);
//        } else {
//            resultData.setPublicationdate(publicationdate);
//        }
//
//        //获取价格
//        cell = row.getCell(cellNum++);
//        String price = convertCellValueToString(cell);
//        if (null == price || "".equals(price)) {
//            // 价格为空
//            resultData.setPrice(Integer.parseInt(price));
//        } else {
//            resultData.setPrice(Integer.parseInt(price));
//        }
//
//        //获取借阅状态
//        cell = row.getCell(cellNum++);
//        String bookstate = convertCellValueToString(cell);
//        if (null == bookstate || "".equals(bookstate)) {
//            // 借阅状态为空
//            resultData.setBookstate(bookstate);
//        } else {
//            resultData.setBookstate(bookstate);
//        }
//
//        //获取备注
//        cell = row.getCell(cellNum++);
//        String comment = convertCellValueToString(cell);
//        if (null == comment || "".equals(comment)) {
//            // 备注为空
//            resultData.setComment(comment);
//        } else {
//            resultData.setComment(comment);
//        }
//        return resultData;
//    }

    /**
     * 生成Excel文件
     * @param outputStream
     */
    public void writeExcel(OutputStream outputStream){


        /**
         * 这个outputstream可以来自与文件的输出流，
         * 也可以直接输出到response的getOutputStream()里面
         * 然后用户就可以直接解析到你生产的excel文件了
         */
        Workbook wb = new SXSSFWorkbook(100);
        //创建一个工作本
        Sheet sheet = wb.createSheet("sheet");

        //通过一个sheet创建一个Row
        Row row = sheet.createRow(0);

        for(int i =0;i<10;i++){
            //通过row创建一个cell
            Cell  cell = row.createCell(i);
            cell.setCellValue("这是第"+i+"个cell");
        }
        try {
            wb.write(outputStream);
            wb.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
