package com.zd.core.controller.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Controller
@RequestMapping("/QrCode")
public class ZxingCodeController {
	@RequestMapping("/image")
    public void createImage(HttpServletRequest request, HttpServletResponse response) throws IOException, WriterException {
        /*
		response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/png");
        */
		String url=request.getParameter("url");
		
        ServletOutputStream out = response.getOutputStream();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        
        BitMatrix bitMatrix = multiFormatWriter.encode(url, BarcodeFormat.QR_CODE, 300, 300,hints);
   
        MatrixToImageWriter.writeToStream(bitMatrix, "png", out);

        try {
            out.flush();
        } finally {
            out.close();
        }
    }

}
