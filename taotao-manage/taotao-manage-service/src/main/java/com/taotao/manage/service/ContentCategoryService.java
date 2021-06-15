package com.taotao.manage.service;

import com.taotao.manage.pojo.ContentCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentCategoryService extends BaseService<ContentCategory> {

    /**
     * 添加内容分类
     * @param parentId
     * @param name
     * @return
     */
    public ContentCategory addContentCategory(Long parentId, String name) {

        ContentCategory category = new ContentCategory();
        category.setParentId(parentId);
        category.setIsParent(false);
        category.setName(name);
        category.setSortOrder(1);
        category.setStatus(1);
        super.saveSelective(category);


        //根据上级编号查询父节点isParent是否为true
        ContentCategory contentCategory = super.queryById(parentId);
        if(null != contentCategory && !contentCategory.getIsParent()){
            //将当前查询到的节点修改为相应的父节点
            contentCategory.setIsParent(true);
            super.updateSelective(contentCategory);
        }
        return category;
    }

    /**
     * 内容分类重命名
     * @param id
     * @param name
     */
    public void updateContentCategory(Long id, String name) {
        ContentCategory category = new ContentCategory();
        category.setId(id);
        category.setName(name);
        super.updateSelective(category);
    }

    /**
     * 删除内容分类
     * @param parentId
     * @param id
     */
    public void deleteContentCategory(Long parentId, Long id) {

        //定义一个集合,存放要删除节点的id
        ArrayList<Object> ids = new ArrayList<>();
        //先删除自己以及子节点
        ids.add(id);
        //查找要删除的子节点的Id..运用递归,自己在调用自己
        findSonIdAndDelete(id, ids);
        super.deleteByIds(ContentCategory.class, "id", ids);
        //修改父节点,,先查询父节点是否存在其他子节点,如果有不修改,没有就修改
        //根据父节点查找子节点
        ContentCategory category = new ContentCategory();
        category.setParentId(id);
        List<ContentCategory> categories = super.queryListByWhere(category);
        if(categories == null || categories.isEmpty() ){
            //修改
            ContentCategory record = new ContentCategory();
            record.setId(parentId);
            record.setIsParent(false);
            super.updateSelective(record);
        }
    }

    /**
     * 递归删除子节点
     * @param id
     * @param ids
     */
    private void findSonIdAndDelete(Long id, List<Object> ids) {
        //查找上级编号是当前节点的所有子节点
        ContentCategory category = new ContentCategory();
        category.setParentId(id);
        List<ContentCategory> categories = super.queryListByWhere(category);
        if(null != categories && !categories.isEmpty()){
            for(ContentCategory category1:categories){
                ids.add(category1.getId());
                //判断当前节点是否是父节点
                if(category1.getIsParent()){
                    findSonIdAndDelete(category1.getId(),ids);//递归查找
                }
            }
        }
    }
}
