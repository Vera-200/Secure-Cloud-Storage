package com.bupt.controller;

import com.bupt.pojo.File;
import com.bupt.pojo.Group;
import com.bupt.pojo.User;
import com.bupt.service.FileService;
import com.bupt.service.GroupService;
import com.bupt.service.UserService;
import com.bupt.tools.EncryptAndDecrypt;
import com.bupt.tools.FiletoByte;
import com.bupt.uitils.JsonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    //Controller层调Service层
    @Autowired
    @Qualifier("GroupServiceImpl")
    private GroupService groupService;

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService uservice;

    @Autowired
    @Qualifier("FileServiceImpl")
    private FileService fileservice;


    @RequestMapping("/list")
    public String showGroups(HttpServletRequest request){

        System.out.println("进入了grouplist");
        HttpSession session = request.getSession();
        User userInfo = (User) session.getAttribute("UserInfo");

        List<Group> GroupList = null;
        try {
            GroupList = groupService.getGroups(userInfo.getUserId());
            System.out.println(GroupList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (GroupList != null){
            return JsonUtils.getJson(GroupList);
        }else{
            return JsonUtils.getJson("empty");
        }

    }

    @PostMapping("/filelist")
    @ResponseBody
    public String showupload(@RequestBody Group group,HttpServletRequest request)
    {
        System.out.println("进入获取文件列表");

        int testGroupId = group.getGroupId();
        Group realGroup = groupService.getGroupById(testGroupId);
        System.out.println(realGroup);

        HttpSession session = request.getSession();
        session.setAttribute("group",realGroup);


        Group test1 = (Group) session.getAttribute("group");
        System.out.println("test1==="+test1);
//        User me=uservice.queryUserById(userInfo.getUserId());
        List<File> fileList = groupService.GetFileByGroup(testGroupId);
        System.out.println(testGroupId);
        System.out.println(JsonUtils.getJson(fileList));
        return JsonUtils.getJson(fileList);

    }
    @RequestMapping("/manageList")
    public String showGroupsManage(HttpServletRequest request){

        System.out.println("进入了managerlist");
        HttpSession session = request.getSession();
        User userInfo = (User) session.getAttribute("UserInfo");

        List<Group> GroupManageList = groupService.getGroupsManage(userInfo.getUserId());

        return JsonUtils.getJson(GroupManageList);

    }

    @RequestMapping("/registerGroup")
    public String RegisterGroup(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User userInfo = (User) session.getAttribute("UserInfo");

//        随机生成7位随机数作为群Id
        int groupId = (int)(Math.random()*(9999999-1000000+1))+1000000;
//        检验此群Id是否已经使用，若已经使用，则重新生成群Id
        int test = groupService.getGroupNumById(groupId);
        while(test == 1){
            groupId = (int)(Math.random()*(9999999-1000000+1))+1000000;
            test = groupService.getGroupNumById(groupId);
        }
//        创建群组
        int result1 = groupService.registerGroup(userInfo.getUserId(), groupId);
//        群组管理员在创建成功群组后加入群组
        int result2 = groupService.addGroup(userInfo.getUserId(), groupId);
        if(result1 == 1 && result2 == 1) {
            java.io.File file1=new java.io.File("C:\\Users\\darkerg\\Desktop\\savefile"+"\\"+userInfo.getUserName()+"\\"+groupId);
            if (!file1.exists()){
                file1.mkdir();
            }

            return JsonUtils.getJson(groupId);
        }
        return JsonUtils.getJson("Fail");
    }


    @RequestMapping("/addGroup")
    public String AddGroup(@RequestBody Group group, HttpServletRequest request){

        //直接获取userId和通过输入获得用户要加入的群组的groupId
        HttpSession session = request.getSession();
        User userInfo = (User) session.getAttribute("UserInfo");
        int groupId = group.getGroupId();
        int groupExist = groupService.getGroupNumById(groupId);

        if(groupExist == 0){
            return JsonUtils.getJson("noGroup");
        }
//        int groupIn = groupService.judgeGroupById(userInfo.getUserId(),groupId);
//        if(groupIn != 0){
//            return JsonUtils.getJson("inGroup");
//        }
        int ad = groupService.addGroup(userInfo.getUserId(),groupId);
        if(ad == 1)
            return JsonUtils.getJson("Success");
        return JsonUtils.getJson("Fail");
    }

    @PostMapping("/upload")
    @ResponseBody
    public String UpLoad(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception
    {

        //得到文件名
        int result = 0;
        String name = file.getOriginalFilename();
        int indexOfPoint = name.lastIndexOf('.');
        if(indexOfPoint == -1) {
            indexOfPoint = name.length();
        }
        String fileName = name.substring(0,indexOfPoint);
        String fileType = name.substring(indexOfPoint+1);

        //获得user数据
        HttpSession session = request.getSession();
        User userInfo = (User)session.getAttribute("UserInfo");
        String ownerStr = userInfo.getUserName();
        int owner = userInfo.getUserId();
        User u=uservice.queryUserById(owner);
        System.out.println(u.getUserName());
        //获得群组
        Group group =(Group) session.getAttribute("group");
        int groupId = group.getGroupId();
        System.out.println("getgroupId==="+groupId);
        File exist_file = groupService.GetFileById(fileName,fileType,groupId,owner);

        double size = (double)file.getSize()/1000000;

        if(exist_file != null) {
            return JsonUtils.getJson("already exist");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String time = sdf.format(now);

        String hash = DigestUtils.md5Hex(file.getBytes());
        System.out.println("准备上传");
        String path = "C:\\Users\\darkerg\\Desktop\\savefile"+"\\"+u.getUserName()+"\\"+groupId+"\\"+name;        //需要改
        if(group.getGroupUsage()+size > group.getGroupStorage())
        {
            result = 0;
            return JsonUtils.getJson("storage is not enough");
        }
        groupService.AddStorage(groupId, size);
        //java.io.File uploadpath = new java.io.File(path);
        EncryptAndDecrypt.DESEncrypt(file, path, u.getUserKey());
        result = groupService.InsertFileInfo(fileName, fileType, size, time, owner, groupId, hash, path);
        System.out.println("upload result="+result);
        return "" + (result == 1);
    }



    @PostMapping ("/delete")
    public String DeleteFile(@RequestBody File file, HttpServletRequest request)
    {
        
//        //获取user数据
//        HttpSession session = request.getSession();
//        User user =(User) session.getAttribute("UserInfo");
//        Group group = (Group)session.getAttribute("group");
//        int userId = user.getUserId();
        //通过Session获取groupId
        HttpSession session = request.getSession();
        Group group =(Group) session.getAttribute("group");
        //拿到fileId
        int fileId = file.getFileId();
        System.out.println("FileId是："+fileId);
        //通过fileId找到gruopId,先拿到这个targetFile
        File targetFile = fileservice.GetGroupFileById(fileId,group.getGroupId());
        System.out.println("目标文件名是："+targetFile);
        //通过File获得groupId
        int groupId = targetFile.getFileGroup();
        //获取groupId后，拿到这个详细信息中的,groupManagerId
        Group targetGroup = groupService.getGroupInfoById(groupId);
        //拿到groupManagerId
        int groupManagerId = targetGroup.getGroupManagerId();
        //获取SaveFile下第一级文件名：groupManager的名字
        User user = uservice.queryUserById(groupManagerId);
        String groupManagerName = user.getUserName();

        //文件大小
        double file_size = targetFile.getFileSize();

//        if(file_size > group1.getGroupUsage()) {
//            file_size = group1.getGroupUsage();
//        }
        if(groupService.SubStorage(groupId, file_size) == 0)
            return JsonUtils.getJson("failed1");
        if(groupService.DeleteFile(targetFile.getFileId()) == 0)
            return JsonUtils.getJson("failed2");

        String path = "C:\\Users\\darkerg\\Desktop\\savefile"+"\\"+groupManagerName+"\\"+groupId+"\\"+file.getFileName()+"."+file.getFileType();
        java.io.File local_file = new java.io.File(path);
        if(!local_file.delete())
            return JsonUtils.getJson("failed3");
        return JsonUtils.getJson("success");
    }

    @RequestMapping(value="/download",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"}) //匹配的是href中的download请求
    public ResponseEntity<byte[]> DownloadFile(HttpServletRequest request , @RequestBody File file) throws Exception {
    ResponseEntity<byte[]> responseEntity = null;

//        //获取用户数据
//        HttpSession session = request.getSession();
//        Group group=(Group)session.getAttribute("group");
//        int groupId = group.getGroupId();
////        File select_file = groupService.GetFileById();
//        File select_file = groupService.GetFileById2(file.getFileName(), file.getFileType(), group.getGroupId());
//
//        User user = groupService.getUserByGroupFile(groupId,file.getFileId());
//
////        int userId = user.getUserId();
////        File select_file =groupService.GetFileById(file.getFileName(),file.getFileType(),groupId,userId);
//        String fileName = select_file.getFileName() + "." + select_file.getFileType();
    //通过Session获取groupId
    HttpSession session = request.getSession();
    Group group =(Group) session.getAttribute("group");
    //拿到fileId
    int fileId = file.getFileId();
    System.out.println("FileId是："+fileId);
    //通过fileId找到gruopId,先拿到这个targetFile
    File targetFile = fileservice.GetGroupFileById(fileId,group.getGroupId());
    System.out.println("目标文件名是："+targetFile);
    //通过File获得groupId
    int groupId = targetFile.getFileGroup();
    //获取groupId后，拿到这个详细信息中的,groupManagerId
    Group targetGroup = groupService.getGroupInfoById(groupId);
    //拿到groupManagerId
    int groupManagerId = targetGroup.getGroupManagerId();
    //获取SaveFile下第一级文件名：groupManager的名字
    User user_manager = uservice.queryUserById(groupManagerId);
    String groupManagerName = user_manager.getUserName();
        System.out.println(groupId);
        System.out.println(groupManagerName);

    //获取这个文件是谁他么上传的
    int file_ownerId = targetFile.getFileOwner();
    User owner_user = uservice.queryUserById(file_ownerId);

    String allFileName = targetFile.getFileName()+"."+targetFile.getFileType();

    String targetDownloadFile=new String(allFileName.getBytes("utf-8"),"iso-8859-1");

    String path = "C:\\Users\\darkerg\\Desktop\\savefile" +"\\"+ groupManagerName + "\\" +groupId+"\\" +targetDownloadFile;
        System.out.println(path);
    java.io.File file1 = new java.io.File(path);
    if(!file1.exists()) {
        return null;
    }
    java.io.File outFile = new java.io.File("C:\\Users\\darkerg\\Desktop\\savefile\\" + groupManagerName +"\\"+groupId+ "\\" + "temp");
    if(!outFile.exists()) {
        outFile.mkdir();
    }
    String outPath = "C:\\Users\\darkerg\\Desktop\\savefile" +"\\"+ groupManagerName +"\\"+groupId+ "\\" + "temp\\" + targetDownloadFile;		//解密后文件存放路径
        System.out.println(groupManagerName);
        System.out.println(groupId);
    System.out.println(outPath);
    EncryptAndDecrypt.DESDecrypt(file1, outPath, owner_user.getUserKey());
    byte[] hash = FiletoByte.getBytesByFile(outPath);
    HttpHeaders headers=new HttpHeaders();
    headers.setContentDispositionFormData("attachment", targetDownloadFile);
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    java.io.File de_file = new java.io.File(outPath);
    responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(de_file),headers, HttpStatus.CREATED);
    de_file.delete();
    return responseEntity;
}

    @RequestMapping("/entergroup")
    private String enterGroup(@RequestBody Group group,HttpServletRequest request){
        int groupId = group.getGroupId();
        System.out.println("++++++++++++++++"+groupId);
        Group realGroup = groupService.getGroupById(groupId);

        HttpSession xmrsession = request.getSession();
        xmrsession.setAttribute("xmrgroup", realGroup);

        return JsonUtils.getJson(realGroup);
    }
    @RequestMapping("/getgroupinfo")
    public String getGroupInfo(HttpServletRequest request){

        HttpSession session = request.getSession();
        Group group =(Group) session.getAttribute("xmrgroup");

        return JsonUtils.getJson(group);
    }

}
