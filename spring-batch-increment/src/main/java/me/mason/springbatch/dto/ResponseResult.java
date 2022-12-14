package me.mason.springbatch.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.mason.springbatch.exception.UniExceptionEnums;
import me.mason.springbatch.exception.UniRuntimeException;

/**
 *  统一返回类
 * @author mason
 */
@ApiModel(value="返回类")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseResult<T> {
	@ApiModelProperty(value="返回状态")
	private boolean success;
	@ApiModelProperty(value="错误信息代码")
	private String code;
	@ApiModelProperty(value="错误显示信息")
	private String message;
	@ApiModelProperty(value="错误信息")
	private String errMsg;
	@ApiModelProperty(value = "返回数据",notes = "若返回错误信息，此数据为null或错误信息对象")
	private T data;

	public static ResponseResult of(boolean success,Object resultData,String errCode,String errShowMsg, String errMsg) {
		return new ResponseResult(success,errCode, errShowMsg,errMsg,resultData);
	}

	public static ResponseResult ok(Object resultData) {
		return of(true,resultData,"200","操作成功",null);
	}

	public static ResponseResult err(String errCode,String errShowMsg,String errMsg) {
		return of(false,null, errCode,errShowMsg,errMsg);
	}

	public static ResponseResult err(UniExceptionEnums uniExceptionEnums) {
		return of(false,null, uniExceptionEnums.getErrCode(),uniExceptionEnums.getErrShowMsg(),null);
	}

	public static ResponseResult err(UniExceptionEnums uniExceptionEnums,Exception e) {
		return of(false,null, uniExceptionEnums.getErrCode(),uniExceptionEnums.getErrShowMsg(),e.getMessage());
	}

	public static ResponseResult err(UniRuntimeException e) {
		return of(false,null, e.getErrCode(),e.getErrShowMsg(),e.getMessage());
	}
}
