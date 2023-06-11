package com.bupt.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bupt.tools.EncryptAndDecrypt;
import com.bupt.tools.FiletoByte;
import com.bupt.uitils.JsonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bupt.pojo.File;
import com.bupt.pojo.User;
import com.bupt.service.FileService;
import com.bupt.service.UserService;

@RestController
@RequestMapping("/file")
public class FileController
{
    @Resource
    @Autowired
    private FileService fileservice;
    @Resource
    @Autowired
    private UserService uservice;

    @GetMapping("/list")
    @ResponseBody
    public String showupload(HttpServletRequest request)
    {
        System.out.println("进入获取文件列表");

        HttpSession session = request.getSession();
        User userInfo = (User) session.getAttribute("UserInfo");

        User me=uservice.queryUserById(userInfo.getUserId());
        List<File> fileList=uservice.getFileById(userInfo.getUserId());

        System.out.println(JsonUtils.getJson(fileList));
        return JsonUtils.getJson(fileList);

    }

    @PostMapping("/upload")
    @ResponseBody
    public String UpLoad(@RequestParam("file")MultipartFile file,HttpServletRequest request) throws Exception
    {
        int result = 0;
        String name = file.getOriginalFilename();
        int indexOfPoint = name.lastIndexOf('.');
        if(indexOfPoint == -1) {
            indexOfPoint = name.length();
        }
        String fileName = name.substring(0,indexOfPoint);
        String fileType = name.substring(indexOfPoint+1);

        HttpSession session = request.getSession();
        User userInfo = (User)session.getAttribute("UserInfo");

        String ownerStr = userInfo.getUserName();
        int owner = userInfo.getUserId();
        User u=uservice.queryUserById(owner);


        System.out.println(fileName);
        System.out.println(fileType);
        System.out.println(ownerStr);
        File exist_file = fileservice.GetFileByName(fileName,fileType,owner);
        System.out.println(exist_file);

        double size = (double)file.getSize()/1000000;

        if(exist_file != null) {
            return "File already exists motherxxxxx";
//            double file_size = exist_file.getFileSize();
//
//            if(u.getUserUsage() - file_size + size > u.getUserStorage())
//                return "storage is not enough";
//
//            if(file_size > u.getUserUsage()) {
//                file_size = u.getUserUsage();
//            }
//            if(fileservice.SubStorage(u.getUserId(), file_size) == 0)
//                return "false";
//            if(fileservice.DeleteFile(exist_file.getFileID()) == 0)
//                return "false";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String time = sdf.format(now);

        String hash = DigestUtils.md5Hex(file.getBytes());
        System.out.println("准备上传");
        String path = "C:\\Users\\darkerg\\Desktop\\savefile"+"\\"+u.getUserName()+"\\"+name;        //需要改
        if(u.getUserUsage()+size > u.getUserStorage())
        {
            result = 0;
            return "storage is not enough";
        }
        fileservice.AddStorage(owner, size);
        //java.io.File uploadpath = new java.io.File(path);
        EncryptAndDecrypt.DESEncrypt(file, path, u.getUserKey());
        result = fileservice.InsertFileInfo(fileName, fileType, size, time, owner, hash, path);
        System.out.println("upload result="+result);
        return "" + (result == 1);
    }

    @PostMapping ("/delete")
    public String DeleteFile(@RequestBody File file, HttpServletRequest request)
    {

        HttpSession session = request.getSession();
        User user =(User) session.getAttribute("UserInfo");
        int userId = user.getUserId();
        int fileId = file.getFileId();
        File targetFile = fileservice.GetFileById(fileId);
//        File targetFile = fileservice.GetFileByName(file.getFileName(), file.getFileType(), userId);

        double file_size = targetFile.getFileSize();
//        if(file_size<=0) return JsonUtils.getJson("delete failed!!");
//        if(file_size > user.getUserUsage()) {
//            file_size = user.getUserUsage();
//        }
        if(fileservice.SubStorage(userId, file_size) == 0)
            return JsonUtils.getJson("change user's storage failed!!");
        if(fileservice.DeleteFile(targetFile.getFileId()) == 0)
            return JsonUtils.getJson("delete from mysql failed!!");

        String path = "C:\\Users\\darkerg\\Desktop\\savefile"+"\\"+user.getUserName()+"\\"+file.getFileName()+"."+file.getFileType();
        java.io.File local_file = new java.io.File(path);
        if(!local_file.delete())
            return JsonUtils.getJson("delete from service failed!!");
        return JsonUtils.getJson("Delete Successfully!!");
    }

//    @GetMapping("/changefilename")
//    @ResponseBody
//    public String ChangeFileName(HttpServletRequest request)
//    {
//        int fileId = 10;
//        //int fileId = Integer.parseInt(request.getParameter("FileID"));
//        String new_filename = "abc";
////       String new_filename = request.getParameter("newFileName");
//        File file = fileservice.GetFileById(fileId);
//        if(file == null) {
//            return "failed...try again?";
//        }
//        if(new_filename.equals(file.getFileName()))
//            return "file name repeat!";
//        File exist_file = fileservice.GetFileByName(new_filename,file.getFileType(), file.getFileOwner()+"");
//        if(exist_file!=null) {
//            return new_filename+" already exists!";
//        }
//        int index = file.getFilePath().lastIndexOf("\\");
//        String new_filepath = file.getFilePath().substring(0, index+1)+new_filename+"."+file.getFileType();
//        java.io.File f = new java.io.File(file.getFilePath());
//        if(!f.exists()) {
//            return "file dose not exist!";
//        }else {
//            java.io.File nameto = new java.io.File(new_filepath);
//            f.renameTo(nameto);
//        }
//
//        if(fileservice.ChangeFileName(fileId,new_filename,new_filepath) == 0)
//            return "change from sql failed...";
//        return "true";
//    }
//
    @RequestMapping(value="/download",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"}) //匹配的是href中的download请求
    public ResponseEntity<byte[]> DownloadFile(HttpServletRequest request ,@RequestBody File file) throws Exception {
        ResponseEntity<byte[]> responseEntity = null;

        //得到file类
        HttpSession session = request.getSession();
        User user =(User) session.getAttribute("UserInfo");
        int userId = user.getUserId();

        File select_file =fileservice.GetFileByName(file.getFileName(),file.getFileType(),userId);
        String fileName = select_file.getFileName() + "." + select_file.getFileType();

        String myFileName=new String(fileName.getBytes("utf-8"),"iso-8859-1");

        int index = select_file.getFilePath().lastIndexOf("\\");
        String username = select_file.getFilePath().substring(34, index);       //按下面路径（209行）数数
        System.out.println("username是："+username);
        User u=uservice.queryUserByName(username);
        String path = "C:\\Users\\darkerg\\Desktop\\savefile" +"\\"+ u.getUserName() + "\\" + fileName;
        java.io.File file1 = new java.io.File(path);
        if(!file1.exists()) {
            return null;
        }
        java.io.File outFile = new java.io.File("C:\\Users\\darkerg\\Desktop\\savefile\\" + u.getUserName() + "\\" + "temp");
        if(!outFile.exists()) {
            outFile.mkdir();
        }
        String outPath = "C:\\Users\\darkerg\\Desktop\\savefile" +"\\"+ u.getUserName() + "\\" + "temp\\" + fileName;		//解密后文件存放路径
        EncryptAndDecrypt.DESDecrypt(file1, outPath, u.getUserKey());
        byte[] hash = FiletoByte.getBytesByFile(outPath);
        System.out.println(hash);
        System.out.println(select_file.getFileHash().getBytes());
        HttpHeaders headers=new HttpHeaders();
        headers.setContentDispositionFormData("attachment", myFileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        java.io.File de_file = new java.io.File(outPath);
        responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(de_file),headers,HttpStatus.CREATED);
        de_file.delete();
        return responseEntity;
    }
//
//    @RequestMapping("/download2")
//    public String downloads(@RequestBody File file,HttpServletResponse response,HttpServletRequest request) throws Exception{
//
//        HttpSession session = request.getSession();
//        User user =(User) session.getAttribute("UserInfo");
//        int userId = user.getUserId();
//        File select_file = fileservice.GetFileByName(file.getFileName(), file.getFileType(), userId);
//
//        String username = user.getUserName();
//        User u=uservice.queryUserByName(username);
//
//        String fileName = select_file.getFileName() + "." + select_file.getFileType();
//
//        //要下载图片的地址
//        String path = "C:\\Users\\darkerg\\Desktop\\savefile" + u.getUserName() + "\\" + fileName;
//
//        //创建一个新文件夹
//        java.io.File outFile = new java.io.File("C:\\Users\\darkerg\\Desktop\\savefile\\" + u.getUserName() + "\\" + "temp");
//        if(!outFile.exists()) {
//            outFile.mkdir();
//        }
//
//
//        //1.设置response响应头
//        response.reset();//设置页面不缓存，清空buffer
//        response.setCharacterEncoding("UTF-8");//字符编码
//        response.setContentType("multipart/form-data");//二进制传输数据
//
//        //设置响应头
//        response.setHeader("Content-Disposition",
//                "attachment;fileName="+ URLEncoder.encode(fileName,"UTF-8"));
//
//        java.io.File targetFile = new java.io.File(path, fileName);
//
//
//        String outPath = "C:\\Users\\darkerg\\Desktop\\savefile\\" + u.getUserName() + "\\" + "temp\\" + fileName;
//        EncryptAndDecrypt.DESDecrypt(new java.io.File(outPath), outPath, u.getUserKey());
//
//        //2、读取文件------输入流
//        FileInputStream input = new FileInputStream(outPath);
//
//        //3.写出文件----输出流
//        OutputStream out = response.getOutputStream();
//
//
//
//        byte[] buff = new byte[1024];
//        int index = 0;
//
//        //4。执行  写出操作
//        while ((index=input.read(buff))!=-1){
//            out.write(buff,0,index);
//            out.flush();
//        }
//        out.close();
//        input.close();
//        return null;
//    }
//
//    @RequestMapping("sharefile")
//    @ResponseBody
//    public String sharefile(HttpServletRequest request)
//    {
//        String share_str = "";
//        String file_id = request.getParameter("FileId");
//        share_str = EncryptAndDecrypt.EncryptFileId(file_id, "sharefile!");
//        return share_str;
//    }
//
//    @RequestMapping("getsharedfile")
//    @ResponseBody
//    public String GetSharedFile(HttpServletRequest request) throws Exception
//    {
//        String share_code = request.getParameter("ShareCode");
//        String file_id_str = EncryptAndDecrypt.DecryptFileId(share_code, "sharefile!");
//        int file_id = 0;
//        try {
//            file_id = Integer.valueOf(file_id_str);
//        }catch (NumberFormatException e){
//            return "false";
//        }
//        File f = fileservice.GetFileById(file_id);
//        if(f == null)
//            return "false";
//        int index = f.getFilePath().lastIndexOf("\\");
//        String username = f.getFilePath().substring(10, index);
//        return f.getFileID()+" "+f.getFileName()+" "+f.getFileType()+" "+f.getFileSize()+" "+username;
//    }
//
//    @RequestMapping("getsharedfile2me")
//    @ResponseBody
//    public String GetSharedFileToMe(HttpServletRequest request) throws Exception
//    {
//        int my_id = Integer.parseInt(request.getSession().getAttribute("ID").toString());
//        int file_id = Integer.valueOf(request.getParameter("fileID"));
//        File f = fileservice.GetFileById(file_id);
//
//        if(f == null)
//            return "false";
//        if(my_id == f.getFileOwner()) {
//            return "same";
//        }
//        User src_user = fileservice.GetOwnerByFile(file_id);
//        User dst_user = uservice.queryUserById(my_id);
//
//        File same_f = fileservice.GetFileByName(f.getFileName(),f.getFileType(), my_id+"");
//        if(same_f!=null) {
//            return "name";
//        }
//
//        String hash = f.getFileHash();
//        String src_path = "C:\\webpan\\"+src_user.getUserName();
//        String dst_path = "C:\\webpan\\"+dst_user.getUserName();
//        java.io.File src_file = new java.io.File(f.getFilePath());
//        double size = (double)src_file.length()/1000000;
//        if(dst_user.getUserUsage()+size<dst_user.getUserStorage())
//        {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date now = new Date();
//            String time = sdf.format(now);
//            EncryptAndDecrypt.DESDecrypt(src_file, src_path+"\\tmp\\"+f.getFileName()+"."+f.getFileType(), src_user.getUserKey());
//            EncryptAndDecrypt.DESEncrypt(src_path+"\\tmp\\"+f.getFileName()+"."+f.getFileType(), dst_path+"\\"+f.getFileName()+"."+f.getFileType(), dst_user.getUserKey());
//            fileservice.AddStorage(dst_user.getUserId(), size);
//            fileservice.InsertFileInfo(f.getFileName(), f.getFileType(), size, time, dst_user.getUserId(), hash, dst_path+"\\"+f.getFileName()+"."+f.getFileType());
//            java.io.File temp_file = new java.io.File(src_path+"\\tmp\\"+f.getFileName()+"."+f.getFileType());
//            temp_file.delete();
//            return "true";
//        }
//        return "false";
//    }
}