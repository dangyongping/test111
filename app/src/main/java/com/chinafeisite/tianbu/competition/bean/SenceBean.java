package com.chinafeisite.tianbu.competition.bean;

/*
 *  @项目名：  RK3188-BP 
 *  @包名：    com.chinafeisite.tianbu.competition.bean
 *  @文件名:   SenceBean
 *  @创建者:   Administrator
 *  @创建时间:  2016/12/26 20:07
 *  @描述：    TODO
 */
public class SenceBean
{
    private static final String TAG = "SenceBean";
    public String sceneName	;		//场景名
    public String  sceneImg;			//	场景图
    public String  id;			//
    public String distance;
    Boolean isSelect;
    public boolean isSelect() {
        return isSelect;
    }
    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

}
