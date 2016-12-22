package com.analytics.excel.reports;

import com.analytics.client.QueryClient;
import com.analytics.dao.StatItemDAO;
import com.analytics.entity.response.ya.data.direct.StatItem;
import com.analytics.excel.ConfigExcel;
import com.analytics.excel.CreateExcelReport;
import org.apache.poi.hssf.util.AreaReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExcelSearchOrContext implements FillingExcel{
    private StatItem statItem;

    public static String YA_FIND_COST = "yaFindCost";
    public static String YA_FIND_SHOW = "yaFindShow";
    public static String YA_FIND_CLICK = "yaFindClick";
    public static String YA_FIND_CTR = "yaFindCTR";
    public static String YA_FIND_AVR_COST_CLICK = "yaFindAvrCostClick";
    public static String YA_FIND_GET_GOAL = "yaFindGetGoal";
    public static String YA_FIND_CONVERSATION = "yaFindConversation";
    public static String YA_FIND_COST_GOAL = "yaFindCostGoal";
    public static String YA_NET_COST = "yaNetCost";
    public static String YA_NET_SHOW = "yaNetShow";
    public static String YA_NET_CLICK = "yaNetClick";
    public static String YA_NET_CTR = "yaNetCTR";
    public static String YA_NET_AVR_COST_CLICK = "yaNetAvrCostClick";
    public static String YA_NET_GET_GOAL = "yaNetGetGoal";
    public static String YA_NET_CONVERSATION = "yaNetConversation";
    public static String YA_NET_COST_GOAL = "yaNetCostGoal";
    public static String COST = "cost";
    public static String COUNT_ADVERT = "countAdvert";
    public static String AVR_COST_OF_ADVERT = "averCostOfAvert";
    private ExcelRecommendation excelRecommendation;

    public ExcelSearchOrContext(QueryClient queryClient, ExcelRecommendation excelRecommendation) {
        this.excelRecommendation = excelRecommendation;
        statItem = new StatItemDAO().getSummaryStat(queryClient);
        double contextCost = statItem.getClicksContext() * statItem.getSumContext();
        double searchCost = statItem.getClicksSearch() * statItem.getSumSearch();
        double searchCtr = (statItem.getClicksSearch() / (double)statItem.getShowsSearch()) * 100;
        double contextCtr = (statItem.getClicksContext() / (double) statItem.getShowsSearch()) * 100;

        changeCellFromRange(YA_FIND_COST, searchCost);
        changeCellFromRange(YA_NET_COST, contextCost);
        changeCellFromRange(YA_FIND_SHOW, statItem.getShowsSearch());
        changeCellFromRange(YA_NET_SHOW, statItem.getShowsContext());
        changeCellFromRange(YA_FIND_CLICK, statItem.getClicksSearch());
        changeCellFromRange(YA_NET_CLICK, statItem.getClicksContext());
        changeCellFromRange(YA_FIND_CTR, searchCtr + "%");
        changeCellFromRange(YA_NET_CTR, contextCtr + "%");
        changeCellFromRange(YA_FIND_AVR_COST_CLICK, statItem.getSumSearch());
        changeCellFromRange(YA_NET_AVR_COST_CLICK, String.valueOf(statItem.getSumContext()));
        changeCellFromRange(YA_FIND_CONVERSATION, statItem.getGoalConversionSearch() + "%");
        changeCellFromRange(YA_NET_CONVERSATION, statItem.getGoalConversionContext() + "%");
        changeCellFromRange(YA_FIND_COST_GOAL, statItem.getGoalCostSearch());
        changeCellFromRange(YA_NET_COST_GOAL, statItem.getGoalCostContext());
        changeCellFromRange(YA_FIND_GET_GOAL, statItem.getGoalConversionSearch());
        changeCellFromRange(YA_NET_GET_GOAL, statItem.getGoalConversionContext());
        changeCellFromRange(COST, contextCost + searchCost);
        changeCellFromRange(COUNT_ADVERT, statItem.getShowsContext() + statItem.getShowsSearch());
        changeCellFromRange(AVR_COST_OF_ADVERT, (contextCost + searchCost) / (statItem.getShowsContext() + statItem.getShowsSearch()));
    }

    @Override
    public void fillListToExcel(XSSFSheet sheet) {
    }

    @Override
    public void changeRange(int start, int end, String column, String rangeName) {
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
        if(r.getRowNum() % 2 == 0){
            c.setCellStyle(ConfigExcel.STYLE_SEARCH_CONTEXT);
        }else {
            c.setCellStyle(ConfigExcel.STYLE_SEARCH_CONTEXT_SIMPLE);
        }
    }


    public void changeCellFromRange(String rangeName, double changeValue) {
        int namedCellIdx = CreateExcelReport.book.getNameIndex(rangeName);
        Name aNamedCell = CreateExcelReport.book.getNameAt(namedCellIdx);
        AreaReference aref = new AreaReference(aNamedCell.getRefersToFormula());
        CellReference[] cells = aref.getAllReferencedCells();
        Cell c = null;
        Sheet s = CreateExcelReport.book.getSheet(cells[0].getSheetName());
        Row r = s.getRow(cells[0].getRow());
        c = r.getCell(cells[0].getCol());
        c.setCellValue(changeValue);
        if(rangeName.equals(COST) || rangeName.equals(COUNT_ADVERT) || rangeName.equals(AVR_COST_OF_ADVERT)){
            c.setCellStyle(ConfigExcel.STYLE_ADVERT);
        }
        if (r.getRowNum() % 2 == 0) {
            c.setCellStyle(ConfigExcel.STYLE_SEARCH_CONTEXT);
        } else {
            c.setCellStyle(ConfigExcel.STYLE_SEARCH_CONTEXT_SIMPLE);
        }
    }
}
