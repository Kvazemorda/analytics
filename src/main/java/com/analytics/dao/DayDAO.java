package com.analytics.dao;

import com.analytics.client.QueryClient;
import com.analytics.entity.report.Week;
import com.analytics.entity.response.ya.data.metrics.DimensionData;
import com.analytics.entity.response.ya.data.metrics.SourceVisitedFromYaByTime;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;

public class DayDAO {
    public ArrayList<Week> getDayList(QueryClient queryClient){
        ArrayList<Week> list = new ArrayList<>();
        URI url2 = UriComponentsBuilder.fromUriString("https://api-metrika.yandex.ru/stat/v1/data/")
                .path("bytime")
                .queryParam("date1", queryClient.getDate1())
                .queryParam("date2", queryClient.getDate2())
                .queryParam("group", "day")
                .queryParam("accuracy", "full")
                .queryParam("dimensions", "ym:pv:hour")
                //.queryParam("metrics", "ym:s:anyGoalConversionRate")
                .queryParam("metrics", "ym:s:visits")
                .queryParam("ids", queryClient.getClient().getMetricsID())
                .queryParam("oauth_token", queryClient.getClient().getoAuthorID())
                .build()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
        SourceVisitedFromYaByTime sourceVisitedFromYaByTime = restTemplate.getForObject(url2, SourceVisitedFromYaByTime.class);
        System.out.println(sourceVisitedFromYaByTime);
        ArrayList<DimensionData> dimensionDatas = sourceVisitedFromYaByTime.getData();
        for(int j = 0; j < sourceVisitedFromYaByTime.getTime_intervals().size(); j++){

        }

        return list;
    }
}
