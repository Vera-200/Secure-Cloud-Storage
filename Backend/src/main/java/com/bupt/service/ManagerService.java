package com.bupt.service;

import com.bupt.pojo.Apply;
import com.bupt.pojo.Group;
import com.bupt.pojo.User;

import java.util.List;

public interface ManagerService {
    //获取用户信息列表
    public List<User> getAllUsers();

    //获取群组信息列表
    public List<Group> getAllGroups();

    //获取申请列表
    public List<Apply> getAllApplies();

    public List<User> getApplyUsers();

    //设置存储空间
    public int SetUserStorage(int ID, double size);
    public int SetGroupStorage(int ID, double size);

    public User GetUserbyid(int id);
    public Group GetGroupbyid(int id);

    //同意请求
    public int AgreeApply(int applyRequestId,double size);

    //拒绝请求
    public int RefuseApply(int applyRequestId);

    //删除请求
    public int DeleteApply(int applyRequestId);

}
