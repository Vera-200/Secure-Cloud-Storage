package com.bupt.service;

import com.bupt.pojo.File;
import com.bupt.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {

    int register(String userName,String userEmail,String userPassword) throws NoSuchAlgorithmException;
    User login(String user,String userPassword);
    User queryUserById( int userId);
    User queryUserByName(String userName);
    User queryUserByEmail(String userEmail);
    int checkName(String userName);
    int checkEmail(String userEmail);
    int apply(double applySize,int userId,String userName);
    List<File> getFileById(int userId);
    int changePwd(String userEmail,String userPassword) ;
}
