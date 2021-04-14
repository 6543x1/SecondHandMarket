package com.jessie.SHMarket.entity;


public class Result<T>
{
    int code;
    boolean status;
    String msg;
    T data;
    /*
    * {
    "timestamp": 1617713474510,
    "status": 403,
    "error": "Forbidden",
    "message": "",
    "path": "/user/isLogin"
}*/
    @Override
    public String toString()
    {
        return "Result{" +
                "code=" + code +
                ", status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static <T> Result<T> success(String msg)
    {
        return new Result<T>(msg, 200);

    }

    public static <T> Result<T> success(String msg, T data)
    {
        return new Result<T>(msg, data, true);
    }

    public static <T> Result<T> error(String msg)
    {
        return new Result<T>(msg);
    }

    public static <T> Result<T> error(String msg, int code)
    {
        return new Result<T>(msg, code);
    }

    public static <T> Result<T> msg(String msg, T data, boolean status)
    {
        return new Result<T>(msg, data, status);
    }

    private Result(T data)
    {
        this.code = 200;
        this.msg = "success";
        this.status = true;
        this.data = data;

    }

    private Result(String msg)
    {
        if (msg == null) return;
        this.code = 400;
        this.status = false;
        this.msg = msg;
    }

    private Result(String msg, int code)
    {
        if (msg == null) return;
        this.msg = msg;
        this.code = code;
        status = code == 200;
    }

    private Result(String msg, T data, boolean status)
    {
        this.code = status ? 200 : 400;
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public boolean isStatus()
    {
        return status;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}
