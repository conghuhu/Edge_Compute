package cn.cislc.dockerservice.handler;

import cn.cislc.cloudcommon.result.JsonResult;
import cn.cislc.cloudcommon.result.ResultCode;
import cn.cislc.cloudcommon.result.ResultTool;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author conghuhu
 * @create 2022-03-05 18:39
 */
@RestControllerAdvice
public class GlobalException {


    @ExceptionHandler(IOException.class)
    public JsonResult ioExceptionHandler(IOException e) {
        e.printStackTrace();
        return ResultTool.fail(ResultCode.CONNECT_ERROR);
    }

    @ExceptionHandler(InterruptedException.class)
    public JsonResult interruptedExceptionHandler(InterruptedException e) {
        e.printStackTrace();
        return ResultTool.fail("异常中断");
    }

    @ExceptionHandler(ExecutionException.class)
    public JsonResult executionExceptionHandler(ExecutionException e){
        e.printStackTrace();
        return ResultTool.fail("线程错误");
    }

    /**
     * 进行异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public JsonResult exception(Exception e) {
        e.printStackTrace();
        return ResultTool.fail();
    }
}
