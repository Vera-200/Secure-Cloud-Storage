package com.bupt.service;

import com.bupt.dao.GroupMapper;
import com.bupt.dao.UserMapper;
import com.bupt.pojo.File;
import com.bupt.pojo.Group;
import com.bupt.pojo.User;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Resource;
import java.util.List;

public class GroupServiceImpl implements GroupService{


    @Resource
    private GroupMapper groupMapper;
    //私有属性groupMapper初始化
    public void setGroupMapper(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    public int registerGroup(int userId,int groupId){
        return groupMapper.registerGroup(userId,groupId);
    }
    public int addGroup(int userId,int groupId){
        return groupMapper.addGroup(userId,groupId);
    }

    //通过groupId判断是否存在这一个group
    public Group getGroupById(int groupId){
        return groupMapper.getGroupById(groupId);
    }
    public int getGroupNumById(int groupId){
        return groupMapper.getGroupNumById(groupId);
    }
    //将user所在的group都找出来
    public List<Group> getGroups(int userId){
        return groupMapper.getGroups(userId);
    }
    //将user管理的group都找出来
    public List<Group> getGroupsManage(int userId){
        return groupMapper.getGroupsManage(userId);
    }


    public Group getGroupInfoById(int groupId) { return groupMapper.getGroupInfoById(groupId); }

    public int InsertFileInfo(String name,String type,double size,String time,int owner,int group,String hash,String path){
        return groupMapper.InsertFileInfo(name, type, size, time, owner, group, hash, path);
    }

    public int SubStorage(int groupId,double size){
        return groupMapper.SubStorage(groupId,size);
    }
    public int DeleteFile(int file_id) {
        return groupMapper.DeleteFile(file_id);
    }
    public int AddStorage(int groupId,double size){
        return groupMapper.AddStorage(groupId,size);
    }
    //通过groupId判断user是否已经加入该群组
//    public int judgeGroupById(int userId,int groupId){
//        return groupMapper.judgeGroupById(userId,groupId);
//    }
//
//    public int addUserGroupFile(int userId, int groupId, int fileId){
//        return groupMapper.addUserGroupFile(userId, groupId, fileId);
//    }

    public File GetFileById(String fileName, String fileType, int groupId, int userId){
        return  groupMapper.GetFileById(fileName,fileType, groupId, userId);
    }
    public User getUserByGroupFile(int groupId, int fileId){
        return  groupMapper.getUserByGroupFile(groupId, fileId);
    }

    public List<File> GetFileByGroup(int groupId){
        return groupMapper.GetFileByGroup(groupId);
    }

    @Override
    public File GetFileById2(String fileName, String fileType, int groupId) {
        return groupMapper.GetFileById2(fileName,fileType,groupId);
    }
}
