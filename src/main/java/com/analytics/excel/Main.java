package com.analytics.excel;

import com.analytics.Constant;
import com.analytics.client.Client;
import com.analytics.client.QueryClient;
import com.analytics.dao.DynamicConversationDAO;

import java.util.ArrayList;

public class Main {
    public static Client client;
    public static QueryClient queryClient;

    public static void main(String[] args) {
        client = new Client();
        client.setMetricsID("38437860");
        client.setoAuthorID("AQAAAAASjEe8AAOfZ4L88v3m-U9PvgBL9J0AI-g");
        client.setDirectCompanyID(new ArrayList<>());
        client.getDirectCompanyID().add("22568139");
        client.getDirectCompanyID().add("22568167");
        client.getDirectCompanyID().add("22606160");
        client.getDirectCompanyID().add("22606166");

        client.setoAuthorIDDirect(Constant.QUATH_TOKEN_DIRECT);
        queryClient = new QueryClient("2016-10-28", "2016-11-27", client);

        CreateExcelReport createExcelTemplate = new CreateExcelReport();

       /* GetSummarySetDAO getSummarySetDAO = new GetSummarySetDAO();
        getSummarySetDAO.getSummaryStat(queryClient);*/

        //DayDAO dayDAO = new DayDAO();
        DynamicConversationDAO dynamicConversationDAO = new DynamicConversationDAO();
        System.out.println(dynamicConversationDAO.getList(Main.queryClient));

    }


}
