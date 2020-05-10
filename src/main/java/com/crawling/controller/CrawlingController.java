package com.crawling.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.crawling.object.NewsEnum;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * naver news 속보, 스크래핑하기
 * 
 * created by yunji
 * 2020.05.10
 */


@RestController
public class CrawlingController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");


   @GetMapping(value="/news/break/{type}")
   public Object getPublic(@PathVariable("type") String type) throws IOException{

        List<HashMap<String,String>> lists = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> info = new  HashMap<String,String>();
        String date = sdf.format(Calendar.getInstance().getTime());


        //네이버 뉴스 속보 url
        String url ="https://news.naver.com/main/list.nhn?listType=title&mode=LSD&mid=sec&sid1="
                    + NewsEnum.valueOf(type).getCode() + "&date="+ date;

        Connection.Response response = Jsoup.connect(url)
                                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")
                                        .method(Method.GET)
                                        .execute();

       info.put("요청URL", url);
       info.put("조회날짜",date);
       info.put("뉴스타입",NewsEnum.valueOf(type).getType());

       
       lists.add(info);
    
       Document document = response.parse();
       // 파싱할 elements 들을 가져온다.
       // https://try.jsoup.org/ 홈페이지 참고하면 좋음
       Elements elements  =  document.select("div[class='list_body newsflash_body'] > ul > li").removeAttr("i");
       Elements urls = elements.select("a[href]");

       
       for(Element element : elements ){
            HashMap<String,String> map = new  HashMap<String,String>();
            String href = urls.attr("abs:href");
            //필요없는 정보 없애기
            String title = element.text().replace("포토", "");

            map.put("url",href);
            map.put("title", title);
            lists.add(map);
       }

       return  lists;
   }
}

