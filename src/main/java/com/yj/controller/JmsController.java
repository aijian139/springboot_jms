package com.yj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.annotation.XmlElementRef;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Controller
@RequestMapping("email")
public class JmsController {


    @Autowired
    JavaMailSender jms;

    @Autowired
    private TemplateEngine templateEngine; //thymeleaf 模板引擎


    @Value("${spring.mail.username}")
    private String from;

    private String to = "1053923619@qq.com";

    /**
     * SimpleMailMessage 发送简单邮件
     * @return
     */
    @GetMapping("send")
    @ResponseBody
    public String sendSimpleEmail(){
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(from);
            message.setSubject("你是猪吗？");
            message.setText("易健是不是很厉害！！！");
            message.setTo("2641349779@qq.com"); // 接受的地址
            message.setTo("1053923619@qq.com"); // 接受的地址

            jms.send(message);

            return "发送成功";
        } catch (MailException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 发送 带html 样式的邮件
     * @return
     * @throws Exception
     */
    @GetMapping("sendMessage")
    @ResponseBody
    public String sendMessage() throws Exception {
        MimeMessage message = jms.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("鹿小葵，加油~"); // 标题

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("<h1 style='color:red'>乾坤未定，你我皆黑马~~~</h1>");

            helper.setText(stringBuffer.toString(),true); // true 代表支持html格式

            jms.send(message);
            return "发送成功！";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    /**
     * 带附件 发送
     * @return
     * @throws Exception
     */
    @GetMapping("sendAttachment")
    @ResponseBody
    public String sendAttachmentsMail() throws Exception {
        MimeMessage mimeMessage = jms.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true); //支持文件上传
        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("带附件的邮件");
            helper.setText("详情参见附近");
            helper.addAttachment("Java.docx", new FileSystemResource(new File("C:/Users/yijian/Desktop/Java.docx")));

            jms.send(mimeMessage);
            return "发送成功！";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

     @GetMapping("sendInlineMail")
     @ResponseBody
     public String sendInlineMail() throws Exception {
         MimeMessage mimeMessage = jms.createMimeMessage();

         MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);

         try {
             helper.setFrom(from);
             helper.setTo(to);
             helper.setSubject("发送带静态资源的邮件");
             helper.setText("<html><body><img src='cid:img'/></body></html>",true);
             helper.addInline("img", new File("C:/Users/yijian/Desktop/1.jpg"));

             jms.send(mimeMessage);
             return "发送带静态资源的文件成功！";
         } catch (Exception e) {
             e.printStackTrace();
             return e.getMessage();
         }

     }

     @GetMapping("sendTemplateEmail")
     @ResponseBody
    public String sendTemplateEmail(String code) throws Exception {
         System.out.println(code);
         MimeMessage mimeMessage = jms.createMimeMessage();

         MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

         try {
             helper.setFrom(from);
             helper.setTo(to);
             helper.setSubject("带模板的邮件测试");
             Context context = new Context();
             context.setVariable("code", code);
             String process = templateEngine.process("index", context);

             helper.setText(process, true);
             jms.send(mimeMessage);
             return "发送成功！";
         } catch (Exception e) {
             e.printStackTrace();
             return e.getMessage();
         }

     }
}
