 /*此类用于存放一些系统中常用的基本配置数据*/


 /**静态变量声明*/
 var comm = Ext.create("Ext.util.MixedCollection");

 /**声明主控制器*/
 var coreApp = null;
 var coreApplication = null;

 /**应用发布的目录，应用程序发布则为空，虚拟目录发布为虚拟目录名如:/jwtest */
var pathName = document.location.pathname;
 var index = pathName.substr(1).indexOf("/");
 var result = pathName.substr(0, index + 1);

 comm.add("baseUrl", result);
 comm.add("minWidth",1000);

 //** Excel文件导出目录*/
 comm.add("uploadUrl","e:\\workspace\\jw\\jw-web\\src\\main\\webapp\\static\\upload\\tem\\");

 /**表单必填项样式*/
 comm.add('required', '<span style="color:red;font-weight:bold;padding:0px 2px;" data-qtip="必填项">*</span>');
 

    
 //暂时屏蔽
 /**持久化登录用户信息*/
 /*
 Ext.Ajax.request({
     url: comm.get('baseUrl') + "/login/getCurrentUser",
     method: "POST",
     async: false,
     timeout: 4000,
     success: function(response) {
         data = Ext.decode(Ext.value(response.responseText, '{}'));
         if (data.success) {
             comm.add("currentUser", data.obj);
         }
     }
 });*/
 
 /**持久登录信息*/
 /*
 Ext.Ajax.request({
     url: comm.get('baseUrl') + "/login/login",
     method: "POST",
     async: false,
     timeout: 4000,
     success: function(response) {

     }
 });*/
 


 var iconData = Ext.create("Ext.util.MixedCollection");
 iconData.add("txt", "/core/css/image/fileimg/txt.png");
 iconData.add("doc", "/core/css/image/fileimg/doc.png");
 iconData.add("docx", "/core/css/image/fileimg/docx.png");
 iconData.add("gif", "/core/css/image/fileimg/jpg.png");
 iconData.add("png", "/core/css/image/fileimg/gif.png");
 iconData.add("jpg", "/core/css/image/fileimg/jpg.png");
 iconData.add("png", "/core/css/image/fileimg/jpg.png");
 iconData.add("jpeg", "/core/css/image/fileimg/jpeg.png");
 iconData.add("pdf", "/core/css/image/fileimg/pdf.png");
 iconData.add("pptx", "/core/css/image/fileimg/pptx.png");
 iconData.add("wait", "/core/css/image/fileimg/wait.png");
 iconData.add("ppt", "/core/css/image/fileimg/ppt.png");
 iconData.add("xls", "/core/css/image/fileimg/xls.png");
 iconData.add("xlsx", "/core/css/image/fileimg/xlsx.png");
 iconData.add("zip", "/core/css/image/fileimg/zip.png");
 comm.add("FILE_ICON", iconData);