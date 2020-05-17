package com.crawling.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.crawling.model.News;
import com.crawling.model.NewsDetail;
import com.crawling.model.NewsInfo;
import com.crawling.object.NewsEnum;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * naver news 속보, 스크래핑하기
 * 
 * created by yunji
 * 2020.05.17
 * 
 **/

@RestController
@Api(value = "CrawlingController v1")
@RequestMapping("/v1/news")
public class CrawlingController {
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
   
   @ApiOperation(value = "네이버 뉴스 속보 스크래핑", notes = "속보입니다.")
   @GetMapping(value="/break/{type}")
   public News getBreakNews(
             @ApiParam(value = "뉴스타입", required = true, example = "it") @PathVariable("type") String type)
             throws IOException {

        String date = sdf.format(Calendar.getInstance().getTime());
        //네이버 뉴스 속보 url
        String url ="https://news.naver.com/main/list.nhn?listType=title&mode=LSD&mid=sec&sid1="
                    + NewsEnum.valueOf(type).getCode() + "&date="+ date;

        Connection.Response response = Jsoup.connect(url)
                                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                                        .method(Method.GET)
                                        .execute();

          NewsInfo newsInfo = new NewsInfo();                              
          newsInfo.setDate(date);
          newsInfo.setUrl(url);
          newsInfo.setNewsType(NewsEnum.valueOf(type).getType());
          newsInfo.setDesc(NewsEnum.URL_BREAK.getType());               

          Document document = response.parse();
          // 파싱할 elements 들을 가져온다.
          // https://try.jsoup.org/ 홈페이지 참고하면 좋음
          Elements elements  =  document.select("div[class='list_body newsflash_body'] > ul > li").removeAttr("i");
          Elements urls = elements.select("a[href]");

          ArrayList<NewsDetail> newsDetails = new ArrayList<NewsDetail>();
          int cnt = 0;
          for(Element element : elements ){
               NewsDetail newsDetail = new NewsDetail();
               newsDetail.setUrl(urls.get(cnt++).attr("abs:href"));
               newsDetail.setTitle(element.text().replace("포토", ""));
               newsDetails.add(newsDetail);
          }

          News news = new News();
          news.setNewsInfo(newsInfo);
          news.setNewsDetails(newsDetails);

       return news;
   }



   @ApiOperation(value = "네이버 뉴스 랭킹 스크래핑", notes = "인기 뉴스 입니다.")
   @GetMapping(value="/hot/{type}")
   public News getHotNews(@PathVariable("type") String type) throws IOException {

        String date = sdf.format(Calendar.getInstance().getTime());
       

        //네이버 랭킹뉴스 url
        String url ="https://news.naver.com/main/ranking/popularDay.nhn?rankingType=popular_day&sectionId="
                    + NewsEnum.valueOf(type).getCode() + "&date="+ date;
          NewsInfo newsInfo = new NewsInfo();       
          newsInfo.setDate(date);
          newsInfo.setUrl(url);
          newsInfo.setNewsType(NewsEnum.valueOf(type).getType());
          newsInfo.setDesc(NewsEnum.URL_HOT.getType()); 
          
          
          
        Connection.Response response = Jsoup.connect(url)
                                             .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                                             .method(Method.GET).execute();

          Document document = response.parse();
          Elements elements  =  document.select("div[class='ranking_headline'] > a");
          ArrayList<NewsDetail> newsDetails = new ArrayList<NewsDetail>();
          for(Element element : elements ){
               NewsDetail newsDetail = new NewsDetail();
               newsDetail.setUrl(element.attr("abs:href"));
               newsDetail.setTitle(element.text());
               newsDetails.add(newsDetail);
          }
     
          News news = new News();
          news.setNewsInfo(newsInfo);
          news.setNewsDetails(newsDetails);

       return  news;
   }

}

