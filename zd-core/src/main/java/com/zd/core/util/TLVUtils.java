package com.zd.core.util;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.bytecode.ByteArray;
/**
 * @author  zenglj 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
public class TLVUtils {
	private static final Logger logger = LoggerFactory.getLogger(TLVUtils.class);
	public static byte[] encode(List<TagLenVal> tlvs){
		int length=length(tlvs);
		byte[] bytes=new byte[length];
		int index=0;
		for(TagLenVal tlv:tlvs){
			if(tlv==null || tlv.type==0  )continue;
			byte[] twobyte=new byte[2];
			ByteArray.write16bit(tlv.getTag(), twobyte, 0);
			swapAndAppend(bytes,twobyte,index);
			index+=2;
			ByteArray.write16bit(tlv.len, twobyte, 0);
			swapAndAppend(bytes,twobyte,index);
			index+=2;
			if(tlv.type==1){
				putIntValuebyIndex(bytes,tlv,index);
			}else if(tlv.type==2){
				putTimeValuebyIndex(bytes,tlv,index);
			}else if(tlv.type==3){
				putStrValuebyIndex(bytes,tlv,index);
			}else if(tlv.type==4){
				putCardTypeValuebyIndex(bytes,tlv,index);
			}else if(tlv.type==5){
				putCardRateValuebyIndex(bytes,tlv,index);
			}else if(tlv.type==6){
				putIPValuebyIndex(bytes,tlv,index);
			}else if(tlv.type==7){
				putMACValuebyIndex(bytes,tlv,index);
			}else if(tlv.type==8){
				putSwitchTimeValuebyIndex(bytes,tlv,index);
			}else if(tlv.type==9){
				put4SwitchTimeValuebyIndex(bytes,tlv,index);
			}
			index+=tlv.len;
		}
		return bytes;
	}




	public static List<TagLenVal> decode(byte[] bytes,List<TagLenVal> tlvs){
		int index=0;
		while(index<bytes.length-1){
			byte[] twobyte=new byte[2];
			twobyte[0]=bytes[index+1];
			twobyte[1]=bytes[index];
			int tag= ByteArray.readU16bit(twobyte, 0);
			index+=2;
			twobyte[0]=bytes[index+1];
			twobyte[1]=bytes[index];
			int len= ByteArray.readU16bit(twobyte, 0);
			index+=2;
			byte[] valbts=ArrayUtils.subarray(bytes, index, index+len);
			TagLenVal tlv=findTlvbyTag(tlvs,tag);
			if(tlv==null){
				logger.error("定义数组里没有找到tag"+tag);
			}else{
				tlv.len=len;
				tlv.tag=tag;
				setValueToTlv(tlv,valbts);
			}
			index+=len;
		}
		return tlvs;
	}
	
	private static void setValueToTlv(TagLenVal tlv,byte[] valbts){
		if(tlv.type==1){
			ArrayUtils.reverse(valbts);
			if(tlv.len==1){
				tlv.valInt=valbts[0] & 0xff;
			}else if(tlv.len==2){
				tlv.valInt=ByteArray.readU16bit(valbts, 0);
			}else if(tlv.len==4){
				tlv.valInt=ByteArray.read32bit(valbts, 0);
			}
		}else if(tlv.type==2){//时间
			StringBuffer times=new StringBuffer();
			for(int i=0; i<valbts.length;i+=2){
				String xs=String.format("%02d", valbts[i]);
				String fz=String.format("%02d", valbts[i+1]);
				times.append(xs+":"+fz);
				if(i+1!=valbts.length-1){
					times.append("|");
				}
			}
			tlv.valStr=times.toString();
		}else if(tlv.type==3){//字符
			String s=new String(valbts);
			tlv.valStr=s.trim();
		}else if(tlv.type==4){//卡类
			//ArrayUtils.reverse(valbts);
			int temp=ByteArray.read32bit(valbts, 0);
			tlv.valStr=Integer.toBinaryString(temp);
			int i=32-tlv.valStr.length();
			for(;i>0;i--){
				tlv.valStr="0"+tlv.valStr;
			}
		}else if(tlv.type==5){//费率
			StringBuffer rate=new StringBuffer();
			for(int i=0; i<valbts.length;i+=2){
				byte[] bs=new byte[]{valbts[i+1],valbts[i]};
				int r=ByteArray.readU16bit(bs, 0);
				rate.append(r);
				if(i+1!=valbts.length-1){
					rate.append("|");
				}
			}
			tlv.valStr=rate.toString();
		}else if(tlv.type==6){//IP
			StringBuffer times=new StringBuffer();
			for(int i=0; i<valbts.length;i++){
				times.append(valbts[i] & 0xff);
				if(i!=valbts.length-1){
					times.append(".");
				}
			}
			tlv.valStr=times.toString();
		}else if(tlv.type==7){//MAC
			StringBuffer times=new StringBuffer();
			for(int i=0; i<valbts.length;i++){
				int t=valbts[i] & 0xff;
				times.append(Integer.toHexString(t).toUpperCase());
				if(i!=valbts.length-1){
					times.append("-");
				}
			}
			tlv.valStr=times.toString();
		}else if(tlv.type==8){//时间开关1路
			setSwitchTimeString(valbts, tlv);
		}else if(tlv.type==9){//时间开关4路
			for(int i=0;i<4;i++){
				byte[] temp=ArrayUtils.subarray(valbts, i*14, (i+1)*14);
				setSwitchTimeString(temp, tlv);
			}
		}
	}
	
	public static TagLenVal findTlvbyTag(List<TagLenVal> tlvs,int tag){
		for(TagLenVal tlv:tlvs){
			if(tag==tlv.tag){
				return tlv;
			}
		}
		return null;
	}
	private static  byte[] putIntValuebyIndex(byte[] bytes,TagLenVal tlv,int index){
		if(tlv.len==1){
			bytes[index]=(byte)tlv.valInt;
		}else if(tlv.len==2){
			byte[] temp=new byte[tlv.len];
			ByteArray.write16bit(tlv.valInt, temp, 0);
			swapAndAppend(bytes,temp,index);
		}else if(tlv.len==4){
			byte[] temp=new byte[tlv.len];
			ByteArray.write32bit(tlv.valInt, temp, 0);
			swapAndAppend(bytes,temp,index);
		}
		return bytes;
	}
	
	private static  void putStrValuebyIndex(byte[] bytes,TagLenVal tlv,int index){
		byte[] str=tlv.valStr.getBytes();
		byte[] temp=new byte[tlv.len-str.length];
		temp=ArrayUtils.addAll(temp, str);
		swapAndAppend(bytes,temp,index);
	}
	private static  void putTimeValuebyIndex(byte[] bytes,TagLenVal tlv,int index){
		String[] times=tlv.valStr.split("[|,:]");
		for(int i=0;i<times.length;i++){
			bytes[index+i]=Byte.valueOf(times[i]);
		}
	}
	
	private static  void putCardRateValuebyIndex(byte[] bytes,TagLenVal tlv,int index){
		String[] array=tlv.valStr.split("[|,:]");
		for(int i=0,j=0; i<array.length*2;i+=2){
			byte[] bt=new byte[2];
			int temp=Integer.parseInt(array[j]);
			ByteArray.write16bit(temp, bt, 0);
			bytes[index+i]=bt[1];
			bytes[index+i+1]=bt[0];
			j++;
		}
		int i=bytes.length;
	}
	
	private static  void putCardTypeValuebyIndex(byte[] bytes,TagLenVal tlv,int index){
		char[] array=tlv.valStr.toCharArray();
		int temp=transferCharArray2Number(array);
		byte[] bt=new byte[tlv.len];
		ByteArray.write32bit(temp, bt, 0);
		//swapAndAppend(bytes,bt,index);
		 for(int i=0;i<bt.length;i++){
			 bytes[index+i]=bt[i];
		 }
	}
	private static  void putIPValuebyIndex(byte[] bytes,TagLenVal tlv,int index){
		String[] array=tlv.valStr.split("[|,:.]");
		for(int i=0; i<array.length;i++){
			int temp=Integer.parseInt(array[i]);
			bytes[index+i]=(byte)temp;
		}
	}
	
	private static  void putMACValuebyIndex(byte[] bytes,TagLenVal tlv,int index){
		String[] array=tlv.valStr.split("[|,:-]");
		for(int i=0; i<array.length;i++){
			byte temp=(byte) (Integer.parseInt(array[i],16));
			bytes[index+i]=temp;
		}
	}
	
	
	private static int putSwitchTimeValuebyIndex(byte[] bytes, TagLenVal tlv, int index){
		String[] array=tlv.valStr.split("#");
		bytes[index]=Byte.parseByte(array[0]);
		bytes[index+1]=Byte.parseByte(array[1]);
		index+=2;
		String[] times=array[2].split("[|,:]");//时间
		String[] swtch=array[3].split("");//开关
		for(int i=0;i<swtch.length;i++){
			bytes[index]=Byte.parseByte(swtch[i]);
			index+=1;
			bytes[index]=Byte.parseByte(times[i+i]);
			index+=1;
			bytes[index]=Byte.parseByte(times[i+i+1]);
			index+=1;
		}
		return index;
	}
	private static void setSwitchTimeString(byte[] bytes, TagLenVal tlv) {
		StringBuffer sb=new StringBuffer();
		sb.append(bytes[0]);
		sb.append("#");
		sb.append(bytes[1]);
		sb.append("#");
		String xs=String.format("%02d", bytes[3]);
		sb.append(xs);
		sb.append(":");
		xs=String.format("%02d", bytes[4]);
		sb.append(xs);
		sb.append("|");
		xs=String.format("%02d", bytes[6]);
		sb.append(xs);
		sb.append(":");
		xs=String.format("%02d", bytes[7]);
		sb.append(xs);
		sb.append("|");
		xs=String.format("%02d", bytes[9]);
		sb.append(xs);
		sb.append(":");
		xs=String.format("%02d", bytes[10]);
		sb.append(xs);
		sb.append("|");
		xs=String.format("%02d", bytes[12]);
		sb.append(xs);
		sb.append(":");
		xs=String.format("%02d", bytes[13]);
		sb.append(xs);
		sb.append("#");
		sb.append(bytes[2]);
		sb.append(bytes[5]);
		sb.append(bytes[8]);
		sb.append(bytes[11]);
		if(tlv.valStr!=null&&tlv.valStr.trim().isEmpty()!=true){
			tlv.valStr=tlv.valStr+"&"+sb.toString();
		}else{
			tlv.valStr=sb.toString();
		}
		
	}
	private static void put4SwitchTimeValuebyIndex(byte[] bytes, TagLenVal tlv, int index) {
		String[] array=tlv.valStr.split("&");
		for(int i=0;i<array.length;i++){
			TagLenVal t=new TagLenVal();
			t.valStr=array[i];
			index=putSwitchTimeValuebyIndex(bytes, t, index);
		}
	}
	private static int length(List<TagLenVal> tlvs){
		int len=0;
		for(TagLenVal tlv:tlvs){
			if(tlv==null || tlv.type==0  )continue;
			len+=tlv.len;
			len+=2;
			len+=2;
		}
		return len;
	}
	 public static int transferCharArray2Number(char[] ch) {
	        if (ch.length != 32)  
	            throw new RuntimeException("数组大小必须为32位");  
	        String s = null;  
	        // 正数  
	        if (ch[0] == '0') {
	            s = new String(ch);  
	            return Integer.valueOf(s, 2);  
	        } else {  
	            for (int i = 0; i < 32; i++) {  
	                if (ch[i] == '0')  
	                    ch[i] = '1';  
	                else  
	                    ch[i] = '0';  
	            }  
	            s = "-" + new String(ch);  
	            // 结果修正  
	            return Integer.valueOf(s, 2) - 1;  
	        }  
	          
	 }
	 
	static private void swapAndAppend(byte[] bytes,byte[] sub,int index){
		 ArrayUtils.reverse(sub);
		 for(int i=0;i<sub.length;i++){
			 bytes[index+i]=sub[i];
		 }
	 }
}
