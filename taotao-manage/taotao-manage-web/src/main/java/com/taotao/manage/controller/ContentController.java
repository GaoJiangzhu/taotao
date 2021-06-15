package com.taotao.manage.controller;

import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.pojo.EasyUIResult;
import com.taotao.manage.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addContent(Content content){

        try {
            this.contentService.addContent(content);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryContentsByCgId(@RequestParam("categoryId") Long categoryId,
                                                            @RequestParam("page") Integer pageNum,
                                                            @RequestParam("rows") Integer pageSize){

        try {
            EasyUIResult easyUIResult = this.contentService.queryContentsByCgId(pageNum, pageSize, categoryId);
            if(easyUIResult.getRows().isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
