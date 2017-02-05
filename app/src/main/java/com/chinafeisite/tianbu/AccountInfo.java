package com.chinafeisite.tianbu;

/**
 * 账号信息类
 */
public class AccountInfo
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
	 * 年龄
	 */
	private int m_nAge = 0;
	
	/**
	 * 身高, 米
	 */
	private double m_dHeight = 0;
	
	/**
	 * 体重, 公斤
	 */
	private double m_dWeight = 0;
	
	/**
	 * 个人周目标(每周跑多少千米)
	 */
	private double m_dGoal = 0;	

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
	 * 获取年龄
	 * @return
	 */
	public int getNAge() { return m_nAge; }
	
	/**
	 * 设置年龄
	 * @param nAge
	 */
	public void setNAge(int nAge) { m_nAge = nAge; }

	/**
	 * 获取身高
	 * @return
	 */
	public double getDHeight() { return m_dHeight; }
	
	/**
	 * 设置身高
	 * @param dHeight
	 */
	public void setDHeight(double dHeight) { m_dHeight = dHeight; }

	/**
	 * 获取体重
	 * @return
	 */
	public double getDWeight() { return m_dWeight; }
	
	/**
	 * 设置体重
	 * @param dWeight
	 */
	public void setDWeight(double dWeight) { m_dWeight = dWeight; }

	/**
	 * 获取个人周目标
	 * @return
	 */
	public double getDGoal() { return m_dGoal; }
	
	/**
	 * 设置个人周目标
	 * @param dGoal
	 */
	public void setDGoal(double dGoal) { m_dGoal = dGoal; }
}
