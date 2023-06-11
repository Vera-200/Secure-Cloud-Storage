package com.bupt.dao;

import com.bupt.pojo.File;
import com.bupt.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface FileMapper {

    //修改存储量

    int InsertFileInfo(@Param("name")String name, @Param("type")String type, @Param("size")double size,
                       @Param("time")String time, @Param("owner")int owner, @Param("hash")String hash, @Param("path")String path);
    int AddStorage(@Param("owner")int owner, @Param("size")double size);
    int SubStorage(@Param("ownerId")int owner, @Param("size")double size);
    int DeleteFile(@Param("file_id")int file_id);
    File GetFileById(@Param("file_id")int fileId);
    File GetFileByName(@Param("file_name")String fileName,@Param("file_type")String fileType,@Param("owner_id")int ownerId);
    User GetOwnerByFile(@Param("file_id")int fileId);
    int ChangeFileName(@Param("file_id")int fileId, @Param("file_name")String new_filename, @Param("file_path")String new_filepath);
    File GetGroupFileById(@Param("file_id")int fileId,@Param("group_id")int groupId);
}
