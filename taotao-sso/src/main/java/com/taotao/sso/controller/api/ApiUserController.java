package com.taotao.sso.controller.api;

import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("user")
public class ApiUserController {

    @Autowired
    private UserService userService;

    /**
     * 根据token查询信息
     * @return
     */
    @RequestMapping(value = "{token}", method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByToken(@PathVariable("token") String token){
        for(int i = 0;i < 10;i++){
                System.out.println("我进来了");
        }
        try {
            User user = this.userService.queryUserByToken(token);
            if(null == user){
                for(int i = 0;i < 10;i++){
                    System.out.println("我进来了");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
