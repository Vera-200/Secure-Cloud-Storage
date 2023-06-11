package com.bupt.service;

import com.bupt.dao.ManagerMapper;
import com.bupt.pojo.Apply;
import com.bupt.pojo.Group;
import com.bupt.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.util.List;

public class ManagerServiceImpl implements ManagerService{
    @Resource
    private ManagerMapper managerMapper;

    public void setManagerMapper(ManagerMapper managerMapper){
        this.managerMapper = managerMapper;
    }

    @Override
    public List<User> getAllUsers(){

        return managerMapper.getAllUsers();

    }

    @Override
    public List<Group> getAllGroups() {
        return managerMapper.getAllGroups();
    }


    @Override
    public List<Apply> getAllApplies() {

        return managerMapper.getAllApplies();
    }

    @Override
    public List<User> getApplyUsers() {

        return managerMapper.getApplyUsers();
    }


    @Override
    public int SetUserStorage(int ID, double size) {
        return managerMapper.setUserStorage(ID,size);
    }

    @Override
    public int SetGroupStorage(int ID, double size) {
        return managerMapper.setGroupStorage(ID,size);
    }

    @Override
    public User GetUserbyid(int id) {
        return managerMapper.GetUserbyid(id);
    }

    @Override
    public Group GetGroupbyid(int id) {
        return managerMapper.GetGroupbyid(id);
    }

    @Override
    public int AgreeApply(int applyRequestId, double size) {

        return managerMapper.agreeUserApply(applyRequestId,size)*managerMapper.agreeUser(applyRequestId,size) ;

    }

    @Override
    public int RefuseApply(int applyRequestId) {

        return managerMapper.refuseUserApply(applyRequestId)*managerMapper.refuseUser(applyRequestId);

    }

    @Override
    public int DeleteApply(int applyID) {
        return managerMapper.DeleteApply(applyID);
    }

}
