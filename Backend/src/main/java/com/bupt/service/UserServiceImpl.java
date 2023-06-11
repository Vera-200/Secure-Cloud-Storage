package com.bupt.service;

import com.bupt.dao.UserMapper;
import com.bupt.pojo.File;
import com.bupt.pojo.User;
import org.apache.commons.codec.binary.Hex;

import javax.annotation.Resource;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

public class UserServiceImpl implements UserService{

    @Resource
    private UserMapper userMapper;
    //私有属性userMapper初始化
    public void setUserMapper(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Override
    public int register(String name,String email,String pwd) throws NoSuchAlgorithmException{

        System.out.println("进入了注册业务");
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        System.out.println("keyGen"+keyGen);
        SecureRandom secRan = new SecureRandom();
        System.out.println("secRan"+secRan);
        keyGen.init(secRan);
        SecretKey secretKey = keyGen.generateKey();
        System.out.println("secretKey"+secretKey);
        byte[] keyBytes = secretKey.getEncoded();
        String key = Hex.encodeHexString(keyBytes);
        System.out.println("key"+key);


        int result = userMapper.register(name,email,pwd,key);
        System.out.println(result);
        if(result==0)
            return 0;//失败
        else
            return 1;
    }
    public User login(String user,String userPassword){
        return userMapper.login(user, userPassword);
    }
    public User queryUserById(int id) {
        //userMapper已经定义了，直接用
        //User user = userMapper.queryUserById(id)//alt+回车
        //System.out.println(user.getUserName());在业务层随意增加代码
        return userMapper.queryUserById(id);
    }
    public User queryUserByName(String name){
        return userMapper.queryUserByName(name);
    }
    public User queryUserByEmail(String email){
        return userMapper.queryUserByEmail(email);
    }

    public int checkName(String userName){
        return userMapper.checkName(userName);
    }
    public int checkEmail(String userEmail){
        return userMapper.checkEmail(userEmail);
    }

    public List<File> getFileById(int userId)
    {
        List<File> fileList = userMapper.getFileById(userId);
        return fileList;
    }
    public int apply(double applySize,int userId,String userName) {
        return userMapper.applyApply(applySize,userId)*userMapper.applyUser(userName);
    }

    public int changePwd(String userEmail,String userPassword) {
        return userMapper.changePwd(userEmail,userPassword);
    }

}
