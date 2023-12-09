package cn.wule.letter.util;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 00:52

import cn.wule.letter.model.ResponseModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class JsonUtil
{
    ObjectMapper om = new ObjectMapper();
    //TODO 将对象转换为json字符串
    //TODO 将json字符串转换为对象
    public String createResponseModelJsonByString(String code, String msg, String data){
        ResponseModel<String> responseModel = ResponseModel.<String>builder()
                .code(code)
                .msg(msg)
                .data(data)
                .build();
        try {
            return om.writeValueAsString(responseModel);
        } catch (JsonProcessingException e) {
            log.error("util.JsonUtil.createResponseModelJson() error:{}",e.getMessage());
        }
        return null;
    }

    public void writeStringJsonToResponse(HttpServletResponse response, String code, String msg,String data){
        ResponseModel<String> responseModel = ResponseModel.<String>builder()
                .code(code)
                .msg(msg)
                .data(data)
                .build();
        try {
            writeJsonToResponse(response,om.writeValueAsString(responseModel));
        } catch (JsonProcessingException e) {
            log.error("util.JsonUtil.writeStringJsonToResponse() error:{}",e.getMessage());
        }
    }

    /**
     * 将json字符串写入response
     */
    private boolean writeJsonToResponse(HttpServletResponse response, String responseModelJson){
        boolean result = false;
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(responseModelJson);
            printWriter.flush();
            result = true;
        } catch (IOException e) {
            log.error("util.JsonUtil.writeJson() error:{}",e.getMessage());
        }
        return result;
    }
}