package com.lgblog.util.copyBeanUtil;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class  BeanCopy {
    private BeanCopy(){
    }

    /**
     *
     * @param source 源类对象
     * @param target 复制目标类对象
     * @return
     * @param <S> 源类
     * @param <T> 复制目标类
     * 该方法使用BeanUtils类将一个类的一部分属性赋值给另外一个类内部的相同字段
     */
    public static<S,T> T copyByBean(S source,Class<T> target){
        T result =null;
        try {
            result=target.newInstance();//通过反射来实现拷贝
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        BeanUtils.copyProperties(source,result);
        return result;
    }
    public static <S,T> List<T> copyListByBean(List<S> sourceList, Class<T> targetClass){
        return sourceList.stream().map(s -> copyByBean(s,targetClass))
                         .collect(Collectors.toList());
    }

    // public static void main(String[] args) {
    //     Article article = new Article();
    //     article.setViewCount(11L);
    //     article.setId(12L);
    //     article.setTitle("亚视了你");
    //     Article article2 = new Article();
    //     article2.setViewCount(11L);
    //     article2.setId(12L);
    //     article2.setTitle("亚视了8");
    //     List<Article> articles=new ArrayList<>();
    //     articles.add(article);
    //     articles.add(article2);
    //     System.out.println(copyListByBean(articles, topArticleVo.class));
    // }
}
