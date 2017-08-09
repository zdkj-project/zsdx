package com.zd.school.app;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.ImageUtil;
import com.zd.school.plartform.baseset.model.BaseAttachment;
import com.zd.school.plartform.baseset.service.BaseAttachmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/app/attachment")
public class APPAttachmentController extends FrameWorkController<BaseAttachment> implements Constant {

    @Resource
    private BaseAttachmentService attachmentService;

    @RequestMapping(value = { "/doUpload" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void doUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try{
            if (!file.isEmpty()) {
                String imageTitle = request.getParameter("imageTitle ");
                String imageIntro = request.getParameter(" imageIntro");
                String actId = request.getParameter("actId");
                String ids = request.getParameter("uuid");
                String entityName = "";
                switch (imageTitle){
                    case "TrainClass":
                        entityName = "TrainClasstrainee";
                        break;
                    case "OaMeeting":
                        entityName = "OaMeetingemp";
                        break;
                }

                // 重命名上传后的文件名
                String myFileName = file.getOriginalFilename();
                String type = myFileName.substring(myFileName.lastIndexOf(".")).trim();
                if(!type.equalsIgnoreCase(".png")&&!type.equalsIgnoreCase(".jpg")&&!type.equalsIgnoreCase(".jpeg")){
                    writeAppJSON(response, "{ \"success\" : false, \"msg\":\"上传失败,请选择PNG|JPG|JPEG类型的图片文件！\"}");
                    return;
                }

                String fileName = String.valueOf(System.currentTimeMillis()) + type;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String url = "/static/upload/checkPhoto/" + sdf.format(System.currentTimeMillis()) + "/";
                String rootPath = request.getSession().getServletContext().getRealPath("/");
                rootPath = rootPath.replace("\\", "/");

                // 定义上传路径
                String path = rootPath + url + fileName;
                File localFile = new File(path);

                if (!localFile.exists()) { // 判断文件夹是否存在
                    localFile.mkdirs(); // 不存在则创建
                }

                file.transferTo(localFile);

                //压缩图片
                BufferedImage bufferedImage = ImageIO.read(localFile);
                int width=bufferedImage.getWidth();
                int height=bufferedImage.getHeight();
                int mode=0;
                if(width>height){
                    mode= ImageUtil.DefineWidth;
                }else{
                    mode=ImageUtil.DefineHeight;
                }
                ImageUtil imageUtil=ImageUtil.getInstance();
                imageUtil.resize(mode, path,path, 800, 600, 0, 0);

                BaseAttachment saveEntity = new BaseAttachment();
                saveEntity.setRecordId(ids);
                saveEntity.setAttachType(imageTitle);
                saveEntity.setAttachUrl(url + fileName);
                saveEntity.setAttachName(fileName);
                saveEntity.setAttachIsMain(0);
                saveEntity.setAttachSize((long) 0);
                saveEntity.setEntityName(entityName);
                saveEntity.setCreateUser("APP考勤");

                attachmentService.merge(saveEntity);
            }

            writeAppJSON(response, "{ \"success\" : true, \"msg\":\"上传图片成功！\"}");
        }catch(Exception e){
            writeAppJSON(response, "{ \"success\" : false, \"msg\":\"上传失败,请联系管理员！\"}");
        }
    }
}
