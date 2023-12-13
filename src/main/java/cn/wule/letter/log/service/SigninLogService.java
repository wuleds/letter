package cn.wule.letter.log.service;
//汉江师范学院 数计学院 吴乐创建于2023/12/12 01:16

import cn.wule.letter.model.log.SigninLog;

public interface SigninLogService
{
    void insertLog(SigninLog signinLog);
}