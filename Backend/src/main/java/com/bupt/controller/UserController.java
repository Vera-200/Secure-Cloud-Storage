package com.bupt.controller;


import com.bupt.pojo.Apply;
import com.bupt.pojo.Code;
import com.bupt.pojo.User;
import com.bupt.service.UserService;
import com.bupt.uitils.JsonUtils;
import com.bupt.uitils.JwtUtil;
import com.bupt.uitils.Sendmail;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UserController {

    //Controller层调Service层
    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @RequestMapping("/login")
    public String Login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response){

        System.out.println("进入了登录方法！！！");
        String testName = user.getUserName();
        String testPassword = user.getUserPassword();
        String testEmail = user.getUserEmail();

        if(testName == null && testEmail == null)
            return JsonUtils.getJson("accountEmpty!");
        if(testPassword == null)
            return JsonUtils.getJson("passwordEmpty!");

        User me = userService.queryUserByName(testName);
        if(me == null)
            me = userService.queryUserByEmail(testEmail);
        if(me == null)
            return JsonUtils.getJson("noUser");

        if(me != null && me.getUserPassword().length() == 48 && verify(testPassword, me.getUserPassword())){
            HttpSession session = request.getSession();
            session.setAttribute("UserInfo",me);
            User userInfo =(User) session.getAttribute("UserInfo");

            //token
            //添加token，后续做验证
            //注册
            String token = JwtUtil.sign(me.getUserName(), me.getUserPassword());

            if (token !=null){
                session.setAttribute("token",token);
                System.out.println(session.getAttribute("token"));
//                Cookie cookie = new Cookie("_COOKIE_NAME", token);
//                cookie.setMaxAge(3600);
//                cookie.setPath("/");
//                response.addCookie(cookie);
            }
            System.out.println("token:"+token);
            System.out.println("Cookie的信息：");
            Cookie[] cookies = request.getCookies();
//            for (int i = 0; i < cookies.length; i++) {
//                System.out.println(cookies[i]);
//            }
            System.out.println(userInfo.toString());


            System.out.println(userInfo.toString());
            return JsonUtils.getJson("true");
        }
        return JsonUtils.getJson("passwordFalse");
    }

    @PostMapping("test")
    public String getsessionId(@RequestBody User user, HttpServletRequest request){
        HttpSession session = request.getSession();

        User testuser = userService.queryUserByName(user.getUserName());

        session.setAttribute("ID",testuser);

        System.out.println("进入了sessionID测试方法！");

        Object userId = session.getAttribute("ID");
        System.out.println(userId);

        return JsonUtils.getJson("true");
    }

    @RequestMapping("/test2")
    public void testSessionID(HttpServletRequest request){

        System.out.println("进入了test2测试方法！");
        HttpSession session = request.getSession();


        Object userId = session.getAttribute("ID");
        System.out.println(userId);
    }


    @PostMapping("/logout")
    @ResponseBody
    public String Logout(HttpServletRequest request)
    {
        request.getSession().invalidate();
//        Cookie[] cookies = request.getCookies();
//        for (int i = 0; i < cookies.length; i++) {
//            if (cookies[i].getName().equals("_COOKIE_NAME")){
//                cookies[i].setMaxAge(0);
//                break;
//            }
//        }
        return JsonUtils.getJson("Logout");
    }


    @PostMapping("/register")
    public String Register(@RequestBody User user,HttpServletRequest request) throws NoSuchAlgorithmException
    {
        System.out.println("进入了注册方法！");
        System.out.println(user.toString());
        String testName = user.getUserName();
        String testPassword = user.getUserPassword();
        String testEmail = user.getUserEmail();

//        String testName = "A";
//        String testPassword = "12345678";
//        String testEmail = "ABC@bupt.cn";

        int CheckName = userService.checkName(testName);
        int CheckEmail = userService.checkEmail(testEmail);

        String regEx = "[ `~!@#$%^&*()+=|{}':;',/\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(testName);
        if(m.find() || testName.indexOf("\\") >=0)
        {
            return JsonUtils.getJson("userNameIllegal");
        }
        if (!testEmail.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+"))
        {
            return JsonUtils.getJson("emailIllegal");
        }
        else if( CheckName != 0)
        {
            return JsonUtils.getJson("nameRepeat");
        }else if( CheckEmail != 0)
        {
            return JsonUtils.getJson("emailRepeat");
        }else if (testPassword.length()<6||testPassword.length()>16)
        {
            return JsonUtils.getJson("passwordIllegal");
        }
        m = p.matcher(testPassword);
        if(m.find() || testPassword.indexOf("\\") >=0)
        {
            return JsonUtils.getJson("passwordIllegal");
        }
        System.out.println("都合法！");
        String md5Pass = generate(testPassword);
        System.out.println("加密后的密码："+md5Pass);
        int result = userService.register(testName,testEmail,md5Pass);
        System.out.println(result);

        if(result==1) {
            System.out.println("准备创建文件夹");

            File file1=new File("C:\\Users\\darkerg\\Desktop\\savefile"+"\\"+testName);
            if (!file1.exists()){
                file1.mkdir();
            }



            return JsonUtils.getJson("registerSuccess");
        }
        return JsonUtils.getJson("registerFalse");
    }

    @PostMapping("/apply")
    public String Apply(@RequestBody Apply apply, HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        User userInfo = (User) session.getAttribute("UserInfo");

        int userId = userInfo.getUserId();

        double testApplySize = apply.getApplySize();

        User me=userService.queryUserById(userId);
        String testUserName = me.getUserName();
//        判断是否有未处理的内存申请消息，0表示无，1表示有
        if(me.isUserStatus()) {
            return JsonUtils.getJson("alreadyApply");
        }
        int result = userService.apply(testApplySize,userId,testUserName);
        if(result == 1)
        {
            return JsonUtils.getJson("applySuccess");
        }
        return JsonUtils.getJson("applyFalse");
    }

    @PostMapping("/forgetpwd")
    public String forgetpwd(@RequestBody User user,HttpServletRequest request){
        String userEmail = user.getUserEmail();
        User user1 = userService.queryUserByEmail(userEmail);

        if (user1==null){
            return JsonUtils.getJson("not exist");
        }else {
            //生产六位随机数
            String sjs=getRandomString(5);
            //放到session中
            HttpSession session = request.getSession();
            session.setAttribute("checkNum",sjs);
            session.setAttribute("UserInfo",user1);
            //给用户发送一封邮件
            //我们使用线程来专门发送邮件，防止出现耗时，和网站注册人数过多的情况

            Sendmail send = new Sendmail(user1,sjs);
            //启动线程，线程启动之后，就会执行Run方法来发送邮件
            send.start();
            return JsonUtils.getJson("success");
        }
    }
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    @PostMapping("verifynum")
    public String verifynum(@RequestBody Code code, HttpServletRequest request){

        String sjs = code.getSjs();
        System.out.println(sjs);
        HttpSession session = request.getSession();
        String checknum = (String) session.getAttribute("checkNum");
        System.out.println(checknum);
        if(!sjs.equals(checknum)){
            return JsonUtils.getJson("false");
        }
        return JsonUtils.getJson("success");
        //验证
    }

    @RequestMapping("changepwd")
    public String changePwd(@RequestBody User user, HttpServletRequest request){
        String newPwd = user.getUserPassword();

        HttpSession session = request.getSession();
        User userInfo = (User) session.getAttribute("UserInfo");
        String userEmail = userInfo.getUserEmail();

        String md5Pass = generate(newPwd);

        int i = userService.changePwd(userEmail, md5Pass);
        System.out.println(i);
        return JsonUtils.getJson("success");

    }

    @RequestMapping("/getusername")
    public String getUserName(HttpServletRequest request){
        HttpSession session = request.getSession();
        User userInfo =(User) session.getAttribute("UserInfo");
        User realUser = userService.queryUserByName(userInfo.getUserName());

        return JsonUtils.getJson(realUser);
    }

    @GetMapping("xmr")
    @ResponseBody
    public String xmrsb(){

        User user1 = userService.queryUserById(1);
        User user2 = userService.queryUserById(2);
        User user3 = userService.queryUserById(3);
        User user4 = userService.queryUserById(4);
        User user5 = userService.queryUserById(5);

        List<User> userList = new ArrayList<>();

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);

        return JsonUtils.getJson(userList);

    }


    /** 加盐MD5 */
    public String generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /** 校验加盐后是否和原文一致 */
    public boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }

    /** 获取十六进制字符串形式的MD5摘要 */
    private String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }
}
