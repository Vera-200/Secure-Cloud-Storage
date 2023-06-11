package com.bupt.dao;

import com.bupt.pojo.File;
import com.bupt.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
     int register(@Param("userName")String userName,@Param("userEmail")String userEmail,@Param("userPassword")String userPassword,@Param("userKey")String userKey);
     User login(@Param("User")String user,@Param("userPassword")String userPassword);
     User queryUserById(@Param("userId") int userId);
     User queryUserByName(@Param("userName")String userName);
     User queryUserByEmail(@Param("userEmail")String userEmail);
     int checkName(@Param("userName")String userName);
     int checkEmail(@Param("userEmail")String userEmail);
     int applyApply(@Param("applySize") double applySize,@Param("applyRequestId") int applyRequestId);
     int applyUser(@Param("userName") String userName);
     List<File> getFileById(@Param("userId")int userId);
     int changePwd(@Param("userEmail")String userEmail,@Param("userPassword")String userPassword);
}
