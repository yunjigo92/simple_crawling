package com.crawling.object;

public enum NewsEnum {

    all("001","전체"),
    polictic("100","정치"),
    economy("101","경제"),
    social("102","사회"),
    culture("103","문화"),
    world("104","세계"),
    it("105","IT"),

    URL_BREAK("break","네이버 실시간 속보입니다."),
    URL_HOT("hot","네이버 실시간 랭킹 뉴스입니다.");


    private String code;
    private String type;
    
    NewsEnum(String code, String type){
        this.code =code;
        this.type =type;
    }

    public String getCode(){
        return this.code;
    }

    public String getType(){
        return this.type;
    }

    
}