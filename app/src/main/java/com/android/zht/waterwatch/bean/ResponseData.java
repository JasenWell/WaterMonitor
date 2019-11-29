package com.android.zht.waterwatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @date    on 2019/4/2
 * @author  hjh
 * @org     hjh
 * @version
 * @description 数据容器
 */
public class ResponseData implements Serializable {
    private List<WarningInfo> warningList;
    private List<AreaInfo> areaInfoList;
    private List<ConsumeInfo> consumeInfoList;
    private VersionInfo versionInfo;

    public List<WarningInfo> getWarningList() {
        return warningList;
    }

    public void setWarningList(List<WarningInfo> warningList) {
        this.warningList = warningList;
    }

    public List<AreaInfo> getAreaInfoList() {
        return areaInfoList;
    }

    public void setAreaInfoList(List<AreaInfo> areaInfoList) {
        this.areaInfoList = areaInfoList;
    }

    public List<ConsumeInfo> getConsumeInfoList() {
        return consumeInfoList;
    }

    public void setConsumeInfoList(List<ConsumeInfo> consumeInfoList) {
        this.consumeInfoList = consumeInfoList;
    }

    public VersionInfo getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }
}
