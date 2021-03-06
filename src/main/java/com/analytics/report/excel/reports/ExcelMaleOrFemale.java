package com.analytics.report.excel.reports;

import com.analytics.entity.client.QueryClient;
import com.analytics.report.dao.MaleOrFemaleDAO;
import com.analytics.report.entity.report.MaleOrFemale;
import com.analytics.report.excel.StyleExcel;
import com.analytics.report.excel.CreateExcelReport;
import org.apache.poi.hssf.util.AreaReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ExcelMaleOrFemale implements FillingExcel {
    public ArrayList<MaleOrFemale> maleOrFemales;
    public static final int MALE_OR_FEMALE = 14;
    public static final int MALE_OR_FEMALE_VISITED = 15;
    public static final int MALE_OR_FEMALE_CONVERSATION_PER = 16;
    public static final String MALE_OR_FEMALE_RN = "maleOrFemale";
    public static final String MALE_OR_FEMALE_VISITED_RN = "maleOrFemaleVisited";
    public static final String MALE_OR_FEMALE_CONVERSATION_PER_RN = "maleOrFemaleConverstionPer";
    public static final String MALE_OR_FEMALE_COLUMN = "O";
    public static final String MALE_OR_FEMALE_VISITED_COLUMN = "P";
    public static final String MALE_OR_FEMALE_CONVERSATION_PER_COLUMN = "Q";
    public static final String WHO_MORE_ON_SITE = "whoMoreOnSite";
    public static final String WHICH_CONVERSATION_MORE = "whichConversationMore";
    public static final String FEMALE_CONVERSTAION_PER = "femaleConversation";
    public static final String MALE_CONVERSTAION_PER = "maleConversationPer";
    private ExcelRecommendation excelRecommendation;
    private DecimalFormat decimalFormat;

    public ExcelMaleOrFemale(QueryClient queryClient, ExcelRecommendation excelRecommendation) {
        this.excelRecommendation = excelRecommendation;
        decimalFormat = new DecimalFormat("##0.00");
        this.maleOrFemales = new MaleOrFemaleDAO().maleOrFemaleList(queryClient);
        fillListToExcel(CreateExcelReport.sheetData);
    }

    @Override
    public void fillListToExcel(XSSFSheet sheet) {
        double maleVisited = 0.0;
        double femaleVisited = 0.0;
        double maleConversation = 0.0;
        double femaleConversation = 0.0;

        for(int i = 0; i < maleOrFemales.size(); i++){
            Row row = sheet.getRow(i + 1);
            if(row == null){
                row = sheet.createRow(i + 1);
            }
            Cell nameCell = row.createCell(MALE_OR_FEMALE);
            nameCell.setCellValue(maleOrFemales.get(i).getMaleOrFemale());

            Cell visitedCell = row.createCell(MALE_OR_FEMALE_VISITED);
            visitedCell.setCellValue(maleOrFemales.get(i).getMaleOrFemaleVisited());

            Cell conversationCell = row.createCell(MALE_OR_FEMALE_CONVERSATION_PER);
            conversationCell.setCellValue(maleOrFemales.get(i).getMaleOrFemaleConversation());
            if(maleOrFemales.get(i).getMaleOrFemale().equals("мужской")){
                maleVisited += maleOrFemales.get(i).getMaleOrFemaleVisited();
                maleConversation += maleOrFemales.get(i).getMaleOrFemaleConversation();
            }
            if(maleOrFemales.get(i).getMaleOrFemale().equals("женский")){
                femaleVisited += maleOrFemales.get(i).getMaleOrFemaleVisited();
                femaleConversation += maleOrFemales.get(i).getMaleOrFemaleConversation();
            }
        }

        changeRange(2, maleOrFemales.size() + 1, MALE_OR_FEMALE_COLUMN, MALE_OR_FEMALE_RN);
        changeRange(2, maleOrFemales.size() + 1, MALE_OR_FEMALE_VISITED_COLUMN, MALE_OR_FEMALE_VISITED_RN);
        changeRange(2, maleOrFemales.size() + 1, MALE_OR_FEMALE_CONVERSATION_PER_COLUMN, MALE_OR_FEMALE_CONVERSATION_PER_RN);

        if(maleVisited > femaleVisited){
            changeCellFromRange(WHO_MORE_ON_SITE, "На сайте больше мужчин");
        }else{
            changeCellFromRange(WHO_MORE_ON_SITE, "На сайте больше женщин");
        }
        double maleConversationPer = (maleConversation / maleVisited)*100;
        double femaleConversationPer = (femaleConversation / femaleVisited)*100;

        if(maleConversationPer > femaleConversationPer){
            changeCellFromRange(WHICH_CONVERSATION_MORE, "Коэф. конверсии выше у мужчин");
        }else {
            changeCellFromRange(WHICH_CONVERSATION_MORE, "Коэф. конверсии выше у женщин");
        }
        changeCellFromRange(FEMALE_CONVERSTAION_PER, femaleConversationPer);
        changeCellFromRange(MALE_CONVERSTAION_PER, maleConversationPer);
        excelRecommendation.setMaleOrFemaleRecommendation(getRecommendation(maleConversationPer, femaleConversationPer));
    }

    @Override
    public void changeRange(int start, int end, String column, String rangeName) {
        XSSFName rangeCell = CreateExcelReport.book.getName(rangeName);
        String reference = CreateExcelReport.sheetData.getSheetName() + "!$" + column + "$" + start + ":$" + column + "$" + (end);
        rangeCell.setRefersToFormula(reference);
    }

    @Override
    public void changeCellFromRange(String rangeName, String changeValue) {
        int namedCellIdx = CreateExcelReport.book.getNameIndex(rangeName);
        Name aNamedCell = CreateExcelReport.book.getNameAt(namedCellIdx);
        AreaReference aref = new AreaReference(aNamedCell.getRefersToFormula());
        CellReference[] cells = aref.getAllReferencedCells();
        Cell c = null;
        Sheet s = CreateExcelReport.book.getSheet(cells[0].getSheetName());
        Row r =  s.getRow(cells[0].getRow());
        c = r.getCell(cells[0].getCol());
        c.setCellValue(changeValue);
        if(rangeName.equals(FEMALE_CONVERSTAION_PER) || rangeName.equals(MALE_CONVERSTAION_PER)){
            c.setCellStyle(StyleExcel.STYLE_DESCRIPTION_CENTER);
        }else {
            c.setCellStyle(StyleExcel.STYLE_DESCRIPTION);
        }
    }

    public void changeCellFromRange(String rangeName, double changeValue) {
        int namedCellIdx = CreateExcelReport.book.getNameIndex(rangeName);
        Name aNamedCell = CreateExcelReport.book.getNameAt(namedCellIdx);
        AreaReference aref = new AreaReference(aNamedCell.getRefersToFormula());
        CellReference[] cells = aref.getAllReferencedCells();
        Cell c = null;
        Sheet s = CreateExcelReport.book.getSheet(cells[0].getSheetName());
        Row r =  s.getRow(cells[0].getRow());
        c = r.getCell(cells[0].getCol());
        c.setCellValue(changeValue);
        if(rangeName.equals(FEMALE_CONVERSTAION_PER) || rangeName.equals(MALE_CONVERSTAION_PER)){
            c.setCellStyle(StyleExcel.STYLE_DESCRIPTION_CENTER);
        }else {
            c.setCellStyle(StyleExcel.STYLE_DESCRIPTION);
        }
    }

    private String getRecommendation(double maleConversationRate, double femaleConversationRate){
        String recommendation = "";
        if(maleConversationRate > femaleConversationRate){
            recommendation = "Повысить ставки для Мужской аудитории";
        }
        if(maleConversationRate < femaleConversationRate){
            recommendation = "Повысить ставки для Женской аудитории";
        }
        return recommendation;
    }

}