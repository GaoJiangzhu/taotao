package com.taotao.manage.service;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.BasePojo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public abstract class BaseService<T extends BasePojo> {


    //在spring4之后支持mapper泛型注入@autowired
    @Autowired
    private Mapper<T> mapper;
    /**
     * 谁继承baseService就要重写抽象方法然后给出具体mapper
     * public abstract Mapper<T> getMapper();
     */

    /**
     * 方法：
     * 1、queryById
     * 2、queryAll
     * 3、queryOne
     * 4、queryListByWhere
     * 5、queryPageListByWhere
     * 6、save
     * 7、update
     * 8、deleteById
     * 9、deleteByIds
     * 10、deleteByWhere
     */

    /**
     * 根据主键查询某个对象
     * @param id
     * @return
     */
    public T queryById(Long id){

        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有
     * @return
     */
    public List<T> queryAll(){

        return mapper.select(null);
    }

    /**
     * 根据条件查询一个对象
     * @param record
     * @return
     */
    public T queryOne(T record){

        return mapper.selectOne(record);
    }

    /**
     * 根据条件查询列表
     * @return
     */
    public List<T> queryListByWhere(T record){

        return mapper.select(record);
    }

    /**
     * 根据条件分页查询
     * @param pageNum
     * @param pageSize
     * @param record
     * @return
     */
    public PageInfo<T> queryPageListByWhere(Integer pageNum, Integer pageSize, T record){

        PageHelper.startPage(pageNum, pageSize);
        //根据条件查询列表
        List<T> list = this.queryListByWhere(record);
        return new PageInfo<T>(list);
    }

    /**
     * 添加所有   字段没有值即Null
     * @return
     */
    public Integer save(T record){
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return mapper.insert(record);
    }

    /**
     * 有选择的添加
     * @param record
     * @return
     */
    public Integer saveSelective(T record){

        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return mapper.insertSelective(record);
    }

    /**
     * 根据主键更新
     * 更新所有,原先有值的可能会被更新成null
     */
    public Integer update(T record){
        record.setUpdated(new Date());
        return mapper.updateByPrimaryKey(record);
    }

    /**
     * 根据主键更新,有选择的更新
     * @param record
     * @return
     */
    public Integer updateSelective(T record){
        record.setUpdated(new Date());
        return mapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    public Integer deleteById(Long id){

        return mapper.deleteByPrimaryKey(id);
    }

    /**
     *批量删除
     * @return
     */
    public Integer deleteByIds(Class<T> clazz, String propertyName, List<Object> ids){

        //创建一个example做高级查询
        Example example = new Example(clazz);
        //设置查询条件   andin
        example.createCriteria().andIn(propertyName, ids);
        return mapper.deleteByExample(example);
    }

    /**
     * 根据条件删除
     * @param record
     * @return
     */
    public Integer deleteByWhere(T record){
        return mapper.delete(record);
    }
}
