/**
 * 用户消息和线程
 */

package server;

import setting.User;
/**
 *
 * @author 
 */
public class UserInfoAndThread
{
	/**
	 * 定义线程类
	 */
	private Work working;

	/**
	 * 定义用户信息类
	 */
	private User user;

	/**
	 * 返回用户
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * 设置用户
	 */
	public void setUser(User user)
	{
		this.user = user;
	}

	/**
	 * 返回线程
	 */
	public Work getWorking()
	{
		return working;
	}

	/**
	 * 设置线程
	 */
	public void setWorking(Work working)
	{
		this.working = working;
	}
}
