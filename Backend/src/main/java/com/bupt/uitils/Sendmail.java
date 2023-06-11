package com.bupt.uitils;

import com.bupt.pojo.User;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//网站3秒原则:用户体验
//多线程提高用户体验！（异步处理）
public class Sendmail extends Thread{

    //用于给用户发送邮件的邮箱
    private String from = "1064078506@qq.com";
    //邮箱的用户名
    private String username = "1064078506@qq.com";
    //邮箱的授权码
    private String password = "jaimgxpepzfvbajb";
    //发送邮件的服务器地址
    private String host = "smtp.qq.com";

    private User user;
    private String sjs;
    public Sendmail(User user,String sjs){
        this.user = user;
        this.sjs = sjs;
    }

    //重写run方法，在run方法中发送邮件给指定的用户


    @Override
    public void run() {
        try{
            Properties prop = new Properties();
            prop.setProperty("mail.host",host);//设置QQ邮件服务器
            prop.setProperty("mail.transport.protocol","smtp");//邮件发送协议
            prop.setProperty("mail.smtp.auth","true");//需要验证用户名和密码

            //关于QQ邮箱，还要设置SSL加密，加上以下代码即可,其他邮箱不需要
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.enable","true");
            prop.put("mail.smtp.ssl.socketFactory",sf);

            //使用JavaMail发送邮件的5个步骤

            //1.创建定义整个应用程序所需的环境信息的Session对象
            //QQ才有！其他邮箱就不用
            Session session =  Session.getDefaultInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username,password);
                }
            });

            session.setDebug(true);

            //2.通过session得到transport对象
            Transport ts = session.getTransport();

            //3.使用邮箱的用户名和授权码连接上邮件服务器
            ts.connect(host,username,password);

            //4.创建邮件：写邮件
            //注意需要传递session
            MimeMessage message = new MimeMessage(session);

            //指明邮件的发件人
            message.setFrom(new InternetAddress(from));

            //指明邮件的收件人，现在发件人和收件人是一样的，那就是给自己发
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(user.getUserEmail()));

            //邮件的标题
            message.setSubject("用户找回密码邮件");

            //邮件的文本内容
            String info = "您的验证码为："+sjs;
            message.setContent(info,"text/html;charset=UTF-8");

            //5.发送邮件
            ts.sendMessage(message,message.getAllRecipients());

            //6.关闭连接
            ts.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
