package com.bupt.dao;

import com.bupt.pojo.File;
import com.bupt.pojo.Group;
import com.bupt.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupMapper {


    int registerGroup(@Param("userId") int userId,@Param("groupId") int groupId);
    int addGroup(@Param("userId") int userId,@Param("groupId") int groupId);
    Group getGroupById(@Param("groupId") int groupId);
    List<Group> getGroups(@Param("userId") int userId);
    List<Group> getGroupsManage(@Param("userId")int userId);
    Group getGroupInfoById(@Param("groupId") int groupId);
    int InsertFileInfo(@Param("name")String name, @Param("type")String type, @Param("size")double size,
                       @Param("time")String time, @Param("owner")int owner, @Param("group")int group, @Param("hash")String hash, @Param("path")String path);
//    int judgeGroupById(@Param("userId") int userId,@Param("groupId") int groupId);
//    int addUserGroupFile(@Param("userId")int userId, @Param("groupId")int groupId, @Param("fileId")int fileId);
    File GetFileById(@Param("fileName")String fileName,@Param("fileType") String fileType,@Param("groupId")int groupId,@Param("userId")int userId);
    File GetFileById2(@Param("fileName")String fileName,@Param("fileType") String fileType,@Param("groupId")int groupId);

    User getUserByGroupFile(@Param("groupId") int groupId,@Param("fileId") int fileId);
    List<File> GetFileByGroup(@Param("groupId") int groupId);
    int getGroupNumById(@Param("groupId") int groupId);

    int SubStorage(@Param("groupId") int groupId,@Param("size") double size);
    int DeleteFile(@Param("file_id")int file_id);
    int AddStorage(@Param("groupId") int groupId,@Param("size") double size);

}
