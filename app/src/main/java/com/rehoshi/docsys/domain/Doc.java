package com.rehoshi.docsys.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Doc {

    /**
     * 分类
     */
    public interface Category{
        //政策
        String POLICY = "policy" ;
        //养老金
        String PENSION = "pension" ;
        //报道
        String NEWS = "news" ;
        //其他
        String OTHERS = "others" ;
    }

    /**
     * 文档id
     */
    private String id ;
    /**
     * 标题
     */
    private String title ;
    /**
     * 文档内容缓存
     */
    private String content ;
    /**
     * 文档url
     */
    private String docUrl ;

    /**
     * 原本的地址
     */
    private String originPath ;
    /**
     * 标签 按照，分割
     */
    private String tag ;

    /**
     * 文档分类 默认是政策文档
     */
    private String category = Category.POLICY ;

    /**
     * 创建人
     */
    private String creatorId ;
    private User creator ;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getOriginPath() {
        return originPath;
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }

    private static final Map<String, String> categoryMap = new HashMap<>() ;
    static {
        categoryMap.put("新闻", Doc.Category.NEWS) ;
        categoryMap.put("政策", Doc.Category.POLICY) ;
        categoryMap.put("养老金", Doc.Category.PENSION) ;
        categoryMap.put("其他", Doc.Category.OTHERS) ;

        categoryMap.put(Doc.Category.NEWS, "新闻") ;
        categoryMap.put(Doc.Category.POLICY, "政策") ;
        categoryMap.put(Doc.Category.PENSION, "养老金") ;
        categoryMap.put(Doc.Category.OTHERS, "其他") ;
    }
    public String getCategoryDesc(){
        return categoryMap.get(getCategory()) ;
    }
}
