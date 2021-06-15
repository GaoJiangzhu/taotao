package com.taotao.web.bean;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Item extends com.taotao.manage.pojo.Item {

    public String[] getImages(){
        //stringutils已经在内部进行了非空判断
        return StringUtils.split(super.getImage(), ",");
    }
}
