package com.bupt.controller;

import com.bupt.pojo.Apply;
import com.bupt.pojo.Group;
import com.bupt.pojo.User;
import com.bupt.service.GroupService;
import com.bupt.service.ManagerService;
import com.bupt.service.UserService;
import com.bupt.uitils.JsonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/mana")
public class ManagerController {
    //Controller层调Service层
    @Autowired
    @Qualifier("ManagerServiceImpl")
    private ManagerService managerService;

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @Autowired
    @Qualifier("GroupServiceImpl")
    private GroupService groupService;


    @RequestMapping("/queryalluser")
    //获取所有用户列表
    public String getAllUser() {

        List<User> User = managerService.getAllUsers();

        return JsonUtils.getJson(User);

    }

    @RequestMapping("/queryGroup")
    //获取所有群组列表
    public String getAllGroup() {

        List<Group> gro = managerService.getAllGroups();

        return JsonUtils.getJson(gro);

    }

    @RequestMapping("/queryallapply")
    //获取所有申请列表
    public String getAllApplies() {

        List<Apply> Apply = managerService.getAllApplies();

        return JsonUtils.getJson(Apply);

    }

    @RequestMapping("/setUserstorage")
    //设置用户的存储空间为size
    public String setUserStorage(@RequestBody User user) {

        double Size= user.getUserStorage();

        System.out.println(user);
        System.out.println(Size);

        //找到真实的用户
        User targetUser = userService.queryUserByName(user.getUserName());

        if (Size < targetUser.getUserUsage())
            return JsonUtils.getJson("error");
        //申请<已有

        int result = managerService.SetUserStorage(targetUser.getUserId(),Size);
        //否则，设置存储空间

        if (result != 0) {
            return JsonUtils.getJson("reset succeeded");
            //设置成功
        }
        else {
            return JsonUtils.getJson("reset failed");

        }

    }


    @RequestMapping("/agreeApply")
    //同意申请
    public String agreeApply(@RequestBody Apply apply) {
        System.out.println(apply);

        int requestID = apply.getApplyRequestId();
        double test_size = apply.getApplySize();

        //int result = managerService.AgreeApply(requestID,test_size)* managerService.DeleteApply(requestID);
        int result1 = managerService.AgreeApply(requestID, test_size);
        int result2 = managerService.DeleteApply(requestID);
        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result1*result2);

        if (result1*result2 == 1) {
            return JsonUtils.getJson("agree succeeded");
            //同意申请成功
        }
        else {
            return JsonUtils.getJson("agree failed");
            //同意申请失败
        }




    }


    @RequestMapping("/refuseApply")
    //拒绝申请
    public String RefuseApply(@RequestBody Apply apply) {
        System.out.println(apply);
        int requestID = apply.getApplyRequestId();

        int result = managerService.RefuseApply(requestID)* managerService.DeleteApply(requestID);

        if (result != 0) {
            return JsonUtils.getJson("refuse succeeded");
            //拒绝申请成功
        }
        else {
            return JsonUtils.getJson("refuse failed");
            //拒绝申请失败
        }

    }


}
