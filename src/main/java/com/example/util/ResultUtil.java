package com.example.util;

import com.example.codeEnum.ResultEnum;
import com.example.model.Result;

/**
 * @author yangrui
 * @date 2021年09月27日 20:27
 */
public class ResultUtil {

    /**
     * @param @param  object
     * @param @return 设定文件
     * @return Result 返回类型
     * @Title: success
     * @Description: 成功有返回
     */
    public static Result success(Object object) {
        Result result = new Result(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
        result.setData(object);
        return result;
    }

    /**
     * @param @return 设定文件
     * @return Result 返回类型
     * @Title: success
     * @Description: 成功无返回
     */
    public static Result success() {
        return ResultUtil.success(null);
    }

    /**
     * @param @param  object
     * @param @return 设定文件
     * @return Result 返回类型
     * @Title: success
     * @Description: 参数不合法返回
     */
    public static Result paramError(Object object) {
        Result result = new Result(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        result.setData(object);
        return result;
    }

    /**
     * @return Result 返回类型
     * @Title: paramError
     * @Description: 参数不合法返回
     */
    public static Result paramError() {
        return new Result(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
    }

    /**
     * 返回自定义状态码及msg
     * @param code
     * @param msg
     * @return
     */
    public static Result customResultParam(String code, String msg) {
        return new Result(code, msg);
    }

    /**
     * @param @param  code
     * @param @param  msg
     * @param @return 设定文件
     * @return Result 返回类型, 错误返回时状态码为 9999
     * @Title: error
     * @Description: 失败方法返回
     */
    public static Result error(String code, String msg) {
        Result result = new Result(code, msg);
        return result;
    }
}
