package com.crawling.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class News {

    private NewsInfo newsInfo;
    private List<NewsDetail> newsDetails;

}