package com.chinafeisite.tianbu;

/**
 * 锻炼信息类
 */
public class TreadmillsInfo 
{
	/**
	 * 用户 ID, 登陆后由服务端返回 
	 */
	private int m_nUserId = -1;

	/**
	 * 家庭成员 ID, 登陆后由服务端返回 
	 */
	private int m_nMemberId = 0;

	/**
	 * 账号
	 */
	private String m_strAccount = "";

	/**
	 * 家庭成员, 0: 客人, 1: 爷爷, 2: 奶奶, 3: 爸爸, 4: 妈妈, 5: 哥哥, 6: 姐姐, 7: 弟弟, 8: 妹妹
	 */
	private int m_nMemberTypeName = 0;

	/**
	 * 上传时间  yyyy-MM-dd HH:mm:ss
	 */
	private String m_strUploadTime = "";	

	/**
	 * 锻炼时长, 单位: 分
	 */
	private int m_nExerciseDuration = 0;

	/**
	 * 跑步距离, 单位: 米
	 */
	private double m_dRunningDistance = 0;	

	/**
	 * 跑步步数
	 */
	private int m_nRunningSteps = 0;

	/**
	 * 消耗卡路里
	 */
	private int m_nConsumeCalorie = 0;

	/**
	 * 获取用户 id
	 * @return
	 */
	public int getNUserId() { return m_nUserId; }
	
	/**
	 * 设置用户 id
	 * @param nUserId
	 */
	public void setNUserId(int nUserId) { m_nUserId = nUserId; }

	/**
	 * 获取家庭成员 id
	 * @return
	 */
	public int getNMemberId() { return m_nMemberId; }
	
	/**
	 * 设置家庭成员 id
	 * @param nMemberId
	 */
	public void setNMemberId(int nMemberId) { m_nMemberId = nMemberId; }

	/**
	 * 获取账号
	 * @return
	 */
	public String getStrAccount() { return m_strAccount; }
	
	/**
	 * 设置账号
	 * @param strAccount
	 */
	public void setStrAccount(String strAccount) { m_strAccount = strAccount; }

	/**
	 * 获取家庭成员
	 * @return
	 */
	public int getNMemberTypeName() { return m_nMemberTypeName; }
	
	/**
	 * 设置家庭成员
	 * @param nMemberTypeName
	 */
	public void setNMemberTypeName(int nMemberTypeName) { m_nMemberTypeName = nMemberTypeName; }

	/**
	 * 获取上传时间
	 * @return
	 */
	public String getStrUploadTime() { return m_strUploadTime; }
	
	/**
	 * 设置上传时间
	 * @param strUploadTime
	 */
	public void setStrUploadTime(String strUploadTime) { m_strUploadTime = strUploadTime; }

	/**
	 * 获取锻炼时长, 单位: 分
	 * @return
	 */
	public int getNExerciseDuration() { return m_nExerciseDuration; }
	
	/**
	 * 设置锻炼时长, 单位: 分
	 * @param nExerciseDuration
	 */
	public void setNExerciseDuration(int nExerciseDuration) { m_nExerciseDuration = nExerciseDuration; }

	/**
	 * 获取跑步距离, 单位: 米
	 * @return
	 */
	public double getDRunningDistance() { return m_dRunningDistance; }
	
	/**
	 * 设置跑步距离, 单位: 米
	 * @param dRunningDistance
	 */
	public void setDRunningDistance(double dRunningDistance) { m_dRunningDistance = dRunningDistance; }

	/**
	 * 获取跑步步数
	 * @return
	 */
	public int getNRunningSteps() { return m_nRunningSteps; }
	
	/**
	 * 设置跑步步数
	 * @param nRunningSteps
	 */
	public void setNRunningSteps(int nRunningSteps) { m_nRunningSteps = nRunningSteps; }

	/**
	 * 获取消耗卡路里
	 * @return
	 */
	public int getNConsumeCalorie() { return m_nConsumeCalorie; }
	
	/**
	 * 设置消耗卡路里
	 * @param nConsumeCalorie
	 */
	public void setNConsumeCalorie(int nConsumeCalorie) { m_nConsumeCalorie = nConsumeCalorie; }
}
