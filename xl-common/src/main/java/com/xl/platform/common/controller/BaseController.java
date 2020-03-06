package com.xl.platform.common.controller;

import com.xl.platform.common.exception.XlApiException;
import com.xl.platform.core.response.RespInfo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

import static com.xl.platform.core.constants.ConstansBase.SUCCESS_MSG;

/**
 * <p>
 * TODO
 * </p>
 * <pre> Created: 2020/2/20 14:30  </pre>
 *
 * @author caimingshi
 * @version 1.0
 * @since JDK 1.8
 */
public class BaseController {

    /**
     * 參數校驗
     * @param errors
     */
    protected void checkParams(Errors errors){
        List<ObjectError> errorList = errors.getAllErrors();
        if(errorList != null && errorList.size() > 0){
            throw new XlApiException(errorList.get(0).getDefaultMessage());
        }
    }

    /**
     * 成功返回
     * @param data
     * @return
     */
    protected RespInfo success(Object data){
        return RespInfo.builder().code(HttpStatus.OK.value()).message(SUCCESS_MSG).Data(data).build();
    }
}
