package com.zd.dwr.push;

import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.Browser;
import java.util.Collection;  
  
import org.directwebremoting.ScriptBuffer;  
  
import org.directwebremoting.ScriptSession;  
  
import org.directwebremoting.WebContext;  
  
import org.directwebremoting.WebContextFactory;  
  
import org.directwebremoting.proxy.dwr.Util;  


public class DwrPush
{

    @SuppressWarnings("deprecation")
	public void addMessage(String text,HttpServletRequest request)
    {
       
    WebContext contex=WebContextFactory.get();  
  
    Collection<ScriptSession> sessions=contex.getScriptSessionsByPage(request.getContextPath()+"/login/desktop");  
  
    Util util=new Util(sessions);  
  
    ScriptBuffer sb=new ScriptBuffer();  
  
    sb.appendScript("show(");  
  
    sb.appendData(text);   
  
    sb.appendScript(")");  
  
    util.addScript(sb);  
    }


}
