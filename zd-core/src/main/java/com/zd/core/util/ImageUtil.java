package com.zd.core.util;

import java.awt.image.BufferedImage;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.io.InputStream;  
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;  
  
  
/** 
 
 */  
public class ImageUtil {  
	
	private ImageUtil(){
		
	}
	
	private static ImageUtil instance = null;
	
	public static ImageUtil getInstance(){
		if (instance == null) {
			synchronized (ImageUtil.class) { //加入线程锁	
				if(instance==null)
					instance = new ImageUtil();
			}
		}
		return instance	;
	}
	
    /* 
     1、指定宽，高自适应，等比例缩放; 
     2、指定高， 宽自适应，等比例缩放; 
     3、指定最长边，短边自适应，等比例缩放; 
     4、指定最短边，长边自适应，等比例缩放; 
     5、指定最大宽高， 等比例缩放; 
     6、固定宽高， 居中裁剪） 
     */  
    public static int DefineWidth=1;   
    public static int DefineHeight=2;   
    public static int DefineLong=3;   
    public static int DefineShort=4;   
    public static int MaxWidthHeight=5;   
    public static int DefineWidthHeight=6;   
      
    /** 
     * 图片缩放的方法 
     *  
     * @param mode 
     1、指定宽，高自适应，等比例缩放; 
     2、指定高， 宽自适应，等比例缩放; 
     3、指定最长边，短边自适应，等比例缩放; 
     4、指定最短边，长边自适应，等比例缩放; 
     5、指定最大宽高， 等比例缩放; 
     6、固定宽高， 居中裁剪） 
     * @param src 源文件路径 
     * @param desc 目标文件路径 
     * @param width 指定宽 
     * @param height 指定高 
     * @param maxFrame 指定最长边 
     * @param minFrame 指定最短边 
     * @return 
     * @throws Exception 
     */  
    public  void resize(int mode, String src,String desc, int width, int height, int maxFrame, int minFrame) throws Exception {  
          
        //String str="";  
          
       
        // create command  
        ConvertCmd cmd = this.getCmd();  
        IMOperation op =null;  
        if( mode==ImageUtil.DefineWidth ){  
            op=this.resizeDefineWidth( src,desc, width, height);  
        }else if( mode==ImageUtil.DefineHeight ){  
            op=this.resizeDefineHeight( src,desc, width, height);  
        }else if( mode==ImageUtil.DefineLong ){  
            op=this.resizeDefineLong( src,desc, maxFrame);  
        }else if( mode==ImageUtil.DefineShort ){  
            op=this.resizeDefineShort( src,desc, minFrame);  
        }else if( mode==ImageUtil.MaxWidthHeight ){  
            op=this.resizeMaxWidthHeight( src,desc, width, height);  
        }else if( mode==ImageUtil.DefineWidthHeight ){  
            op=this.resizeDefineWidthHeight( src,desc, width, height);  
        }  
          
        cmd.run(op);  
          
        //return str;  
    }  
      
    //指定宽， 高自适应，等比例缩放;  
    public  IMOperation resizeDefineWidth(String src,String desc, int width, int height){  
        IMOperation op = new IMOperation();  
        op.addImage(src);  
        op.resize(width,null);  
        op.addImage(desc);        
        return op;  
    }  
      
    //指定高，  宽自适应，等比例缩放;  
    public  IMOperation resizeDefineHeight(String src,String desc, int width, int height){  
        IMOperation op = new IMOperation();  
        op.addImage(src);  
        op.resize(null,height);  
        op.addImage(desc);        
        return op;  
    }  
      
    //指定最长边，短边自适应，等比例缩放;  
    public  IMOperation resizeDefineLong(String src,String desc, int maxFrame) throws Exception{  
          
        InputStream is = new FileInputStream(src);//通过文件名称读取  
        BufferedImage buff = ImageIO.read(is);  
        int srcWidth=buff.getWidth();//得到图片的宽度  
        int srcHeight=buff.getHeight();  //得到图片的高度  
        is.close(); //关闭Stream  
          
        IMOperation op = new IMOperation();  
        op.addImage(src);  
        if( srcWidth>srcHeight ){  
            op.resize(maxFrame,null);  
        }else{  
            op.resize(null,maxFrame);  
        }  
          
        op.addImage(desc);        
        return op;  
    }  
      
    //指定最短边，长边自适应，等比例缩放;  
    public  IMOperation resizeDefineShort(String src,String desc, int minFrame) throws Exception {  
          
        InputStream is = new FileInputStream(src);//通过文件名称读取  
        BufferedImage buff = ImageIO.read(is);  
        int srcWidth=buff.getWidth();//得到图片的宽度  
        int srcHeight=buff.getHeight();  //得到图片的高度  
        is.close(); //关闭Stream        
       
        IMOperation op = new IMOperation();  
        op.addImage(src);  
        if( srcWidth<srcHeight ){  
            op.resize(minFrame,null);  
        }else{  
            op.resize(null,minFrame);  
        }  
        op.addImage(desc);        
        return op;  
    }  
      
    //指定最大宽高， 等比例缩放;  
    public  IMOperation resizeMaxWidthHeight(String src,String desc, int width, int height){  
        IMOperation op = new IMOperation();  
        op.addImage(src);  
        op.resize(width,height,'!');  
        op.addImage(desc);        
        return op;  
    }  
      
    //固定宽高， 居中裁剪  
    public  IMOperation resizeDefineWidthHeight(String src,String desc, int width, int height){  
        IMOperation op = new IMOperation();  
        op.addImage(src);  
        op.gravity("center").extent(width, height);    
        op.addImage(desc);        
        return op;  
    }     
      
    public  ConvertCmd getCmd(){  
        ConvertCmd cmd = new ConvertCmd(true); //set true, use GraphicsMagick  
        String path = "C://Program Files//GraphicsMagick-1.3.25-Q8"; //GraphicsMagick安装路径  
        cmd.setSearchPath(path);          
        return cmd;  
    }  
      
      
  
}  