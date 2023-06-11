package com.bupt.service;

import com.bupt.pojo.File;

import com.bupt.pojo.Group;
import com.bupt.pojo.User;
import org.apache.ibatis.annotations.Param;

import javax.jws.soap.SOAPBinding;
import java.util.List;

public interface GroupService {

    //创建group，随机生成groupId
    int registerGroup(int userId,int groupId);

    //通过groupId添加
    int addGroup(int userId,int groupId);

    //通过groupId判断是否存在这一个group
    Group getGroupById(int groupId);
    int getGroupNumById(int groupId);
    //将user所在的group都找出来
    List<Group> getGroups(int userId);

    //将user管理的group都找出来
    List<Group> getGroupsManage(int userId);

    //    通过groupId获取整个group
    Group getGroupInfoById(int groupId);

    //插入文件数据进入数据库
    int InsertFileInfo(String name,String type,double size,String time,int owner,int group,String hash,String path);

    User getUserByGroupFile(int groupId, int fileId);


    //通过groupId判断user是否已经加入该群组
//    int judgeGroupById(int userId, int groupId);
//    int addUserGroupFile(int userId, int groupId, String filename ,String filetype);

    //在GUF表中get文件
    File GetFileById(String fileName, String fileType,int groupId,int userId);

    //test
    File GetFileById2(String fileName, String fileType, int groupId);

    List<File> GetFileByGroup(int groupId);

    public int SubStorage(int groupId,double size);
    public int DeleteFile(int delete);
    public int AddStorage(int groupId,double size);
}
