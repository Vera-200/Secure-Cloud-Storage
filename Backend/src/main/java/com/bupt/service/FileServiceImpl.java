package com.bupt.service;


import com.bupt.dao.FileMapper;
import com.bupt.pojo.File;
import com.bupt.pojo.User;

import javax.annotation.Resource;

public class FileServiceImpl implements FileService{

    @Resource
    public FileMapper fileMapper;

    public void setFileMapper(FileMapper fileMapper){
        this.fileMapper = fileMapper;
    }

    @Override
    public int InsertFileInfo(String name, String type, double size, String time, int owner, String hash, String path) {
        return fileMapper.InsertFileInfo(name, type, size, time, owner, hash, path);
    }

    @Override
    public int AddStorage(int owner, double size) {
        return fileMapper.AddStorage(owner, size);
    }

    @Override
    public int SubStorage(int owner, double size) {
        return fileMapper.SubStorage(owner, size);
    }

    public int DeleteFile(int file_id) {
        return fileMapper.DeleteFile(file_id);
    }

    @Override
    public File GetGroupFileById(int fileId, int groupId) {
        return fileMapper.GetGroupFileById(fileId,groupId);
    }

    public File GetFileById(int fileId) {
        return fileMapper.GetFileById(fileId);
    }
    public File GetFileByName(String fileName, String fileType, int ownerId){
        return fileMapper.GetFileByName(fileName,fileType,ownerId);
    }
    public User GetOwnerByFile(int fileId){
        return fileMapper.GetOwnerByFile(fileId);
    }
    public int ChangeFileName(int fileId,String new_filename,String new_filepath){
        return fileMapper.ChangeFileName(fileId,new_filename,new_filepath);
    }
}
