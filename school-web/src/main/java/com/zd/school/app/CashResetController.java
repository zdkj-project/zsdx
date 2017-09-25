package com.zd.school.app;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.school.cashier.model.CashExpensedetail;
import com.zd.school.cashier.model.CashExpenseserial;
import com.zd.school.cashier.service.CashExpenseserialService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author luoyibo
 * @version 0.1
 * @projectName zsdx_githubTest
 * Description:
 * date: 2017-09-25 15:48
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/app/cashReset")
public class CashResetController extends FrameWorkController<CashExpenseserial> implements Constant {

    @Resource
    private CashExpenseserialService billService;
    /**
     * 将消费单据入库
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @RequestMapping("/doAddConsumeBill")
    public void doAddConsumeBill(HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String consumeBill = request.getParameter("consumeBill");
        String billDetail = request.getParameter("billDetail");

        String returnData = null;
        try {
            CashExpenseserial billEntity = (CashExpenseserial) jsonBuilder.fromJson(consumeBill, CashExpenseserial.class);
            List<CashExpensedetail> listBillDetail = (List<CashExpensedetail>) jsonBuilder.fromJsonArray(billDetail, CashExpensedetail.class);
            boolean b = billService.doSaveConsumeBill(billEntity,listBillDetail);
            if(b)
                returnData = jsonBuilder.returnSuccessJson("\"单据入库成功\"");
            else
                returnData = jsonBuilder.returnFailureJson("\"单据入库失败\"");

            writeAppJSON(response, returnData);
        } catch (InvocationTargetException e) {
            returnData = jsonBuilder.returnFailureJson(e.getMessage());
            writeAppJSON(response, returnData);
        } catch (IllegalAccessException e) {
            returnData = jsonBuilder.returnFailureJson(e.getMessage());
            writeAppJSON(response, returnData);
        }
    }
}
