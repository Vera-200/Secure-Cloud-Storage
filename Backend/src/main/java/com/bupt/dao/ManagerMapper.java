package com.bupt.dao;

import com.bupt.pojo.Apply;
import com.bupt.pojo.Group;
import com.bupt.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerMapper {
    public List<User> getAllUsers();
    public List<Group> getAllGroups();
    public List<Apply> getAllApplies();
    public List<User> getApplyUsers();

    public int setUserStorage(@Param("userId")int ID, @Param("Size")double size);
    public int setGroupStorage(@Param("groupId")int ID, @Param("Size")double size);

    public User GetUserbyid(@Param("userId")int id);
    public Group GetGroupbyid(@Param("groupId")int id);

    public int refuseUserApply(@Param("userId")int userId);
    public int refuseUser(@Param("userId")int userId);

    public int agreeUserApply(@Param("userId")int userId,@Param("Size")double size);
    public int agreeUser(@Param("userId")int userId,@Param("Size")double size);

    public int DeleteApply(@Param("userId")int userId);

}
