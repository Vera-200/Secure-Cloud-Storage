package com.bupt.service;

import org.apache.ibatis.annotations.Param;

import com.bupt.pojo.File;
import com.bupt.pojo.User;

public interface FileService
{
    public int InsertFileInfo(String name,String type,double size,String time,int owner,String hash,String path);
    public int SubStorage(int owner,double size);
    public int DeleteFile(int delete);
    public int AddStorage(int owner,double size);
    File GetFileById(int fileId);
    File GetFileByName(String fileName,String fileType,int ownerId);
    File GetGroupFileById(int fileId,int groupId);
    User GetOwnerByFile(int fileId);
    int ChangeFileName(int fileId,String new_filename,String new_filepath);

}
