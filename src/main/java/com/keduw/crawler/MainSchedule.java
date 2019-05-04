package com.keduw.crawler;

import com.keduw.service.NovelService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 小说爬虫，包含小说内容爬取，章节爬取，章节内容更新日常检查
 * @author hsfeng
 * @date 2019-04-07
 */
@Component
@PropertySource("classpath:application.properties")
public class MainSchedule {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private NovelService novelServicel;
    @Value("${mq.config.exchange}")
    private String novelExchange;
    @Value("${mq.config.routing.novel.key}")
    private String novelRouting;
    @Value("${mq.config.routing.chapter.update.key}")
    private String chapterUpdateRouting;

    //每天凌晨3点启动爬取小说
    @Scheduled(cron = "0 36 23 * * ?")
    public void novelCollect() throws Exception{
        /*NovelCrawler crawl = new NovelCrawler("crawl", true, amqpTemplate, novelExchange, novelRouting);
        crawl.start(1);*/
    }

    //每天0点检查连载中小说的章节更新情况
    @Scheduled(cron = "0 3 0 * * ?")
    public void infoCheck() throws Exception{
        //小说总数
        /*int counts = novelServicel.getNovelCount();
        List<Novel> novelList = null;
        for(int i = 1; i <= counts; i++){
            novelList = novelServicel.getNovelList(i, SIZE);
            for(Novel novel : novelList){
                amqpTemplate.convertAndSend(novelExchange, chapterUpdateRouting, JsonUtils.objectToJson(novel));
            }
        }*/
    }

    private static final int SIZE = 100;
}
