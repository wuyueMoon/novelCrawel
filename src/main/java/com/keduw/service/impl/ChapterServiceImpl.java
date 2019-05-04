package com.keduw.service.impl;

import com.keduw.dao.ChapterMapper;
import com.keduw.model.Chapter;
import com.keduw.service.ChapterService;
import com.keduw.utils.JedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: novelSpider
 * @Package: com.novel.service.impl
 * @ClassName: ChapterServiceImpl
 * @Description: 小说信息
 * @Author: 林浩东
 */
@Service("chapterService")
@PropertySource("classpath:cache.properties")
public class ChapterServiceImpl implements ChapterService {

    private static Logger Log =  (Logger) LoggerFactory.getLogger(ChapterServiceImpl.class);
    @Autowired
    private ChapterMapper chapterMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("chapter")
    private String keys;

    //插入章节信息
    @Override
    public void insertChapter(List<Chapter> chapterList) {
        try{
            if(chapterList == null && chapterList.size() == 0){
                return;
            }
            chapterMapper.insertChapter(chapterList);
            //清除对应小说的章节缓存
            int novelId = chapterList.get(0).getnId();
            String field = keys + novelId;
            jedisClient.del(field);
        }catch (Exception e){
            Log.error("insertChapterError", e.getMessage());
        }
    }


    //更新章节列表
    @Override
    @Transactional
    public void updateChapter(List<Chapter> chapterList) {
        try{
            if(chapterList == null && chapterList.size() == 0){
                return;
            }
            List<Chapter> newList = new ArrayList<>();  //存放新的章节
            List<String> titleList = new ArrayList<>(); //已存在的章节名称集合
            for(Chapter chapter : chapterList){
                int novelId = chapter.getnId();
                List<Chapter> currList = chapterMapper.selectInfoByNovelId(novelId);
                for(Chapter currChapter : currList){
                    titleList.add(currChapter.getName());
                }
                //是否存在该章节
                boolean flag = titleList.contains(chapter.getName());
                if(!flag){
                    newList.add(chapter);
                    //清除对应小说的章节缓存
                    String field = keys + novelId;
                    if(jedisClient.get(field) != null){
                        jedisClient.del(field);
                    }
                }
            }
            chapterMapper.insertChapter(newList);
        }catch (Exception e){
            Log.error("updateChapterError", e.getMessage());
        }
    }

    //更新章节内容
    @Override
    @Transactional
    public void updateChapterContent(Chapter chapter) {
        try{
            if(chapter == null){
                return;
            }
            chapterMapper.updateChapter(chapter);
            int novelId = chapter.getnId();
            String field = keys + novelId;
            if(jedisClient.get(field) != null){
                jedisClient.del(field);
            }
        }catch (Exception e){
            Log.error("updateChapterContentError", e.getMessage());
        }
    }

    //查询章节内容为空的数据
    @Override
    public List<Chapter> getChapterList(int index, int size) {
        int start = index * size;
        List<Chapter> list = chapterMapper.selectInfoByContent(start, size);
        return list;
    }

    @Override
    public int getInfoCounts() {
        return chapterMapper.selectCounts();
    }

}
