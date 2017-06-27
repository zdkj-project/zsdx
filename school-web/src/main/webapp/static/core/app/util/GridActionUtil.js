Ext.define("core.util.GridActionUtil", {
			/**
			 * 得到默认值对象
			 * 
			 * @param {}
			 *            defaultObj
			 */
			getDefaultValue : function(defaultObj) {
				var resObj={};
				for (var field in defaultObj) {
					var value = defaultObj[field];
					// @createTime@ @createUser@ @createDept@
					// if (value.indexOf("@") >= 0) {
					// 	var currentUser=comm.get("currentUser");
					// 	if (value == "@createUser@") {
					// 		value =currentUser.userCode ;
					// 	} else if (value == "@createUserName@") {
					// 		value = currentUser.username;
					// 	} else if (value == "@createDept@") {
					// 		value = currentUser.deptCode;
					// 	} else if (value == "@createDeptName@") {
					// 		value = currentUser.deptName;
					// 	} else if (value == "@createTime@") {
					// 		value = new Date();
					// 	}else if(value=="@LONGTIME@"){
					// 		value=new Date().getTime()+"";
					// 	}
					// }
					resObj[field]=value;					
				}
				return resObj;
			}
});