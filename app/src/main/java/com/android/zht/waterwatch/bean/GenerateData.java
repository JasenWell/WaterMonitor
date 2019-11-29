package com.android.zht.waterwatch.bean;

import com.android.zht.waterwatch.util.Utils;
import com.hjh.baselib.entity.MD5;
import com.hjh.baselib.entity.ResponseJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/4/1.
 */

public class GenerateData {

    public static void  generateUserInfo(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setUserAccount("zht");
        userInfo.setUserPassword(Utils.KL((MD5.getMD5("123456"))));
        userInfo.setUserName("张三");
        userInfo.setOpenTime(System.currentTimeMillis());
        userInfo.setUserPhone("13999999999");
        userInfo.setUserType(1);

        DepartmentInfo departmentInfo = new DepartmentInfo();
        departmentInfo.setId(1);
        departmentInfo.setName("单位名称");
        SchoolInfo schoolInfo = new SchoolInfo();
        schoolInfo.setId(1);
        schoolInfo.setDepartmentId(departmentInfo.getId());
        schoolInfo.setDepartmentName(departmentInfo.getName());
        schoolInfo.setName("学校名称");
        schoolInfo.setContactUser("联系人");
        schoolInfo.setAddress("学校地址");
        schoolInfo.setPhone("13611112222");
        schoolInfo.setTotal("120");
        schoolInfo.setAveravge("10");
        schoolInfo.setTodayWarningNumber(12);
        schoolInfo.setWarningNumber(2);
        WaterEffectInfo effectInfo = new WaterEffectInfo();
        effectInfo.setId(1);
        effectInfo.setSchoolId(schoolInfo.getId());
        effectInfo.setDate("2019年1月");
        List<ClassifyInfo> classifyInfoList = new ArrayList<>();
        ClassifyInfo classifyInfo = new ClassifyInfo();
        classifyInfo.setTypeName("办公楼");
        classifyInfo.setTypeProportion(100);
        classifyInfoList.add(classifyInfo);
        effectInfo.setClassifyInfoList(classifyInfoList);
        List<WaterEffectInfo> effectInfoList = new ArrayList<>();
        effectInfoList.add(effectInfo);
        schoolInfo.setEffectInfoList(effectInfoList);
        List<WaterMeterInfo> waterMeterInfoList = new ArrayList<>();
        WaterMeterInfo waterMeterInfo = new WaterMeterInfo();
        waterMeterInfo.setSchoolId(schoolInfo.getId());
        waterMeterInfo.setSort(1);
        waterMeterInfo.setName("一级表");
        waterMeterInfo.setTotalNumber(10);
        waterMeterInfo.setOnlineNumber(8);
        waterMeterInfoList.add(waterMeterInfo);
        schoolInfo.setMeterInfoList(waterMeterInfoList);
        if(userInfo.getUserType() == 1) {
            departmentInfo.setSchoolInfo(schoolInfo);
        }else {
            List<SchoolInfo> schoolInfoList = new ArrayList<>();
            schoolInfoList.add(schoolInfo);
            departmentInfo.setSchoolList(schoolInfoList);
        }

        if(userInfo.getUserType() == 3){
            List<DepartmentInfo> departmentInfoList = new ArrayList<>();
            departmentInfoList.add(departmentInfo);
            userInfo.setDepartmentInfoList(departmentInfoList);
        }else {
            userInfo.setDepartmentInfo(departmentInfo);
        }

        ResponseJson<UserInfo> responseJson = new ResponseJson();
        responseJson.setData(null);
        responseJson.setInfo("账号或密码不对");
        responseJson.setStatus(0);

        String json = ResponseJson.objectToJson(responseJson);
        if(json == null){

        }
    }

    public static void  generateDepartmentInfoList(){
        UserInfo userInfo = new UserInfo();
        DepartmentInfo departmentInfo = new DepartmentInfo();
        departmentInfo.setId(1);
        departmentInfo.setUserId(1);
        departmentInfo.setName("单位名称");
        List<DepartmentInfo> departmentInfoList = new ArrayList<>();
        departmentInfoList.add(departmentInfo);
        userInfo.setDepartmentInfoList(departmentInfoList);

        ResponseJson<UserInfo> responseJson = new ResponseJson();
        responseJson.setData(userInfo);
        responseJson.setInfo("获取单位列表成功");
        responseJson.setStatus(1);

        String json = ResponseJson.objectToJson(responseJson);
        if(json == null){

        }
    }

    public static void  generateSchoolInfoList(){
        DepartmentInfo departmentInfo = new DepartmentInfo();
        departmentInfo.setId(1);
        departmentInfo.setUserId(1);
        departmentInfo.setName("单位名称");
        ResponseJson<DepartmentInfo> responseJson = new ResponseJson();
        SchoolInfo schoolInfo = new SchoolInfo();
        schoolInfo.setId(1);
        schoolInfo.setDepartmentId(departmentInfo.getId());
        schoolInfo.setDepartmentName(departmentInfo.getName());
        schoolInfo.setName("学校名称");
        schoolInfo.setContactUser("联系人");
        schoolInfo.setAddress("学校地址");
        schoolInfo.setPhone("13611112222");
        schoolInfo.setTotal("120");
        schoolInfo.setAveravge("10");
        schoolInfo.setTodayWarningNumber(12);
        schoolInfo.setWarningNumber(2);
        List<SchoolInfo> schoolInfoList = new ArrayList<>();
        schoolInfoList.add(schoolInfo);
        departmentInfo.setSchoolList(schoolInfoList);

        responseJson.setData(departmentInfo);
        responseJson.setInfo("获取学校列表成功");
        responseJson.setStatus(1);

        String json = ResponseJson.objectToJson(responseJson);
        if(json == null){

        }
    }

    public static void  generateEffectInfo(){
        SchoolInfo schoolInfo = new SchoolInfo();
        WaterEffectInfo effectInfo = new WaterEffectInfo();
        effectInfo.setId(1);
        effectInfo.setSchoolId(1);
        effectInfo.setDate("2019年1月");
        List<ClassifyInfo> classifyInfoList = new ArrayList<>();
        ClassifyInfo classifyInfo = new ClassifyInfo();
        classifyInfo.setTypeName("办公楼");
        classifyInfo.setTypeProportion(100);
        classifyInfoList.add(classifyInfo);
        effectInfo.setClassifyInfoList(classifyInfoList);
        List<WaterEffectInfo> effectInfoList = new ArrayList<>();
        effectInfoList.add(effectInfo);
        schoolInfo.setEffectInfoList(effectInfoList);
        List<WaterMeterInfo> waterMeterInfoList = new ArrayList<>();
        WaterMeterInfo waterMeterInfo = new WaterMeterInfo();
        waterMeterInfo.setSchoolId(schoolInfo.getId());
        waterMeterInfo.setSort(1);
        waterMeterInfo.setName("一级表");
        waterMeterInfo.setTotalNumber(10);
        waterMeterInfo.setOnlineNumber(8);
        waterMeterInfoList.add(waterMeterInfo);
        schoolInfo.setMeterInfoList(waterMeterInfoList);
        ResponseJson<SchoolInfo> responseJson = new ResponseJson();
        responseJson.setData(schoolInfo);
        responseJson.setInfo("获取成功");
        responseJson.setStatus(1);

        String json = ResponseJson.objectToJson(responseJson);
        if(json == null){

        }
    }

    public static void generateWarningList(){
        WarningInfo warningInfo = new WarningInfo();
        warningInfo.setId(1);
        warningInfo.setSchoolId(1);
        warningInfo.setSchoolName("学校名称");
        warningInfo.setPosition("告警位置");
        warningInfo.setStatus(1);
        warningInfo.setTime(System.currentTimeMillis());
        warningInfo.setWarningCount(2);
        warningInfo.setYear(2019);
        warningInfo.setMonth(1);
        warningInfo.setContent("告警内容");
        warningInfo.setData("告警数据");
        List<WarningInfo> warningInfoList = new ArrayList<>();
        warningInfoList.add(warningInfo);
        ResponseData responseData = new ResponseData();
        responseData.setWarningList(warningInfoList);
        ResponseJson<ResponseData> responseJson = new ResponseJson<>();
        responseJson.setInfo("获取成功");
        responseJson.setStatus(1);
        responseJson.setData(responseData);
        String json = ResponseJson.objectToJson(responseJson);
        if(json == null){

        }
    }

    public static void generateWarningStatistics(){
        WarningInfo warningInfo = new WarningInfo();
        warningInfo.setId(1);
        warningInfo.setSchoolId(1);
        warningInfo.setSchoolName("学校名称");
        warningInfo.setPosition("告警位置");
        warningInfo.setStatus(1);
        warningInfo.setTime(System.currentTimeMillis());
        warningInfo.setWarningCount(2);
        warningInfo.setYear(2019);
        warningInfo.setMonth(1);
        warningInfo.setContent("告警内容");
        warningInfo.setData("告警数据");
        List<WarningInfo> warningInfoList = new ArrayList<>();
        warningInfoList.add(warningInfo);
        ResponseData responseData = new ResponseData();
        responseData.setWarningList(warningInfoList);
        ResponseJson<ResponseData> responseJson = new ResponseJson<>();
        responseJson.setInfo("获取成功");
        responseJson.setStatus(1);
        responseJson.setData(responseData);
        String json = ResponseJson.objectToJson(responseJson);
        if(json == null){

        }
    }

    public static void generateAreaList(){
        AreaInfo areaInfo = new AreaInfo();
        areaInfo.setId(1);
        areaInfo.setSchoolId(1);
        areaInfo.setName("区域名称");
        List<AreaInfo> areaInfoList = new ArrayList<>();
        areaInfoList.add(areaInfo);
        ResponseData responseData = new ResponseData();
        responseData.setAreaInfoList(areaInfoList);
        ResponseJson<ResponseData> responseJson = new ResponseJson<>();
        responseJson.setInfo("获取成功");
        responseJson.setStatus(1);
        responseJson.setData(responseData);
        String json = ResponseJson.objectToJson(responseJson);
        if(json == null){

        }
    }

    public static void generateDayReportList(){
        List<ConsumeInfo> consumeInfoList = new ArrayList<>();
        for(int i = 1;i < 25;i++) {
            ConsumeInfo consumeInfo = new ConsumeInfo();
            consumeInfo.setId(1);
            consumeInfo.setAreaId(1);
            consumeInfo.setSchoolId(1);
            consumeInfo.setYear(2019);
            consumeInfo.setMonth(1);
            consumeInfo.setDay(1);
            consumeInfo.setTime(i);
            consumeInfo.setValue(23);
            consumeInfoList.add(consumeInfo);
        }


        ResponseData responseData = new ResponseData();
        responseData.setConsumeInfoList(consumeInfoList);
        ResponseJson<ResponseData> responseJson = new ResponseJson<>();
        responseJson.setInfo("获取成功");
        responseJson.setStatus(1);
        responseJson.setData(responseData);
        String json = ResponseJson.objectToJson(responseJson);
        if(json == null){

        }
    }

    public static void generateMonthReportList(){
        List<ConsumeInfo> consumeInfoList = new ArrayList<>();
        for(int i = 1;i < 32;i++) {
            ConsumeInfo consumeInfo = new ConsumeInfo();
            consumeInfo.setId(1);
            consumeInfo.setAreaId(1);
            consumeInfo.setSchoolId(1);
            consumeInfo.setYear(2019);
            consumeInfo.setMonth(1);
            consumeInfo.setDay(i);
            consumeInfo.setValue(220);
            consumeInfoList.add(consumeInfo);
        }


        ResponseData responseData = new ResponseData();
        responseData.setConsumeInfoList(consumeInfoList);
        ResponseJson<ResponseData> responseJson = new ResponseJson<>();
        responseJson.setInfo("获取成功");
        responseJson.setStatus(1);
        responseJson.setData(responseData);
        String json = ResponseJson.objectToJson(responseJson);
        if(json == null){

        }
    }

    public static void generateYearReportList(){
        List<ConsumeInfo> consumeInfoList = new ArrayList<>();
        for(int i = 1;i < 13;i++) {
            ConsumeInfo consumeInfo = new ConsumeInfo();
            consumeInfo.setId(1);
            consumeInfo.setAreaId(1);
            consumeInfo.setSchoolId(1);
            consumeInfo.setYear(2019);
            consumeInfo.setMonth(i);
            consumeInfo.setValue(3100);
            consumeInfoList.add(consumeInfo);
        }


        ResponseData responseData = new ResponseData();
        responseData.setConsumeInfoList(consumeInfoList);
        ResponseJson<ResponseData> responseJson = new ResponseJson<>();
        responseJson.setInfo("获取成功");
        responseJson.setStatus(1);
        responseJson.setData(responseData);
        String json = ResponseJson.objectToJson(responseJson);
        if(json == null){

        }
    }

    public static void generateVersionInfo(){
        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setApkPath("apk下载路径,例如 http://139.119.165.130:9092/apk/test.apk");
        versionInfo.setMajorCode(2);
        versionInfo.setUpdateTime("2019-03-21 12:22:22");
        versionInfo.setUpdateText("1、修复细节bug&2、修复登录异常");
        ResponseData responseData = new ResponseData();
        responseData.setVersionInfo(versionInfo);
        ResponseJson<ResponseData> responseJson = new ResponseJson<>();
        responseJson.setInfo("获取成功");
        responseJson.setStatus(1);
        responseJson.setData(responseData);
        String json = ResponseJson.objectToJson(responseJson);
        if(json == null){

        }
    }
}
