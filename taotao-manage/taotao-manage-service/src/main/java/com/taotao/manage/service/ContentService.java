package com.taotao.manage.service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.mapper.ContentMapper;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.pojo.EasyUIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService extends BaseService<Content> {

    @Autowired
    private ContentMapper contentMapper;


    public void addContent(Content content) {

        content.setId(null);
        super.saveSelective(content);
    }

    public EasyUIResult queryContentsByCgId(Integer pageNum, Integer pageSize, Long categoryId) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        //设置查询条件
        Example example = new Example(Content.class);
        example.createCriteria().andEqualTo("categoryId", categoryId);
        //设置排序条件
        example.setOrderByClause("updated DESC");
        List<Content> contentList = this.contentMapper.selectByExample(example);
        PageInfo<Content> pageInfo = new PageInfo<>(contentList);

        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
