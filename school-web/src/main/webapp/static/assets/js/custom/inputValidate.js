function inputValidate(dom,validateName){
	var reg='';
	if(validateName=="money"){
		reg=/((^[1-9]{1}[0-9]{0,7})|(^[0]{1}))(0?\.\d{1,2})?$/;
	}

    if(!reg.test(dom.value)){                           
        dom.nextSibling.style.display='inline';	//验证的文字信息
        return false;
    }else{
    	dom.nextSibling.style.display='none';
    	return true;	
    }
}

function inputValueMax(dom,value){
	var val1=Number(dom.value);
	var val2=Number(value);

    if(val1>val2){                           
        dom.nextSibling.style.display='inline';	//验证的文字信息
        return false;
    }
    return true;
}

function moneyCount(index,count,input_prefix,outputId){
//"paymoneyInput_"
//"count_paymoneyGrid"
	console.log(document.getElementById(input_prefix+index));
}

function removeStore(dom,ref,index){   
    var view = Ext.ComponentQuery.query('dataview[ref='+ref+']')[0];
    var store=view.getStore(); 

    var selectTrainItemCount=document.getElementById("selectTrainItemCount");
    var countMoney=0;
    if(selectTrainItemCount!=null){
        countMoney = Number(selectTrainItemCount.innerHTML)-store.getAt(index-1).get("iptemPay");
    }

    store.removeAt(index-1);
    view.refresh();

    if(selectTrainItemCount!=null){   
        document.getElementById("selectTrainItemCount").innerHTML=countMoney.toFixed(2);
    }
    
}