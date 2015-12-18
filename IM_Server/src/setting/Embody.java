/**
 * 信令实现
 */

package setting;

import java.io.Serializable;

/**
 * 
 * @author
 */
public class Embody implements Serializable {
	/**
	 * 定义serialVersionUID
	 */
	private static final long serialVersionUID = 3674727123335529803L;

	/**
	 * 信令变量
	 */
	private String order;

	/**
	 * 消息体成员属性
	 */
	private Object objMessage;

	/**
	 * 发送者用户编号
	 */
	private int sourceID;

	/**
	 * 接收者编号
	 */
	private int destID;

	/**
	 * 暂时储存好友ID
	 */
	private int friendID;

	/**
	 * 暂存好友IP
	 */
	private String friendIP;

	/**
	 * 返回接收者号码
	 */
	public int getDestID() {
		return destID;
	}

	/**
	 * 设置接收者号码
	 */
	public void setDestID(int destID) {
		this.destID = destID;
	}

	/**
	 * 返回消息对象
	 */
	public Object getObjMessage() {
		return objMessage;
	}

	/**
	 * 设置消息对象
	 */
	public void setObjMessage(Object objMessage) {
		this.objMessage = objMessage;
	}

	/**
	 * 返回信令变量
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 设置信令变量
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * 返回信息发送者ID
	 */
	public int getSourceID() {
		return sourceID;
	}

	/**
	 * 设置信息发送者ID
	 */
	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
	}

	/**
	 * 返回被加用户的ID
	 */
	public int getFriendID() {
		return friendID;
	}

	/**
	 * 设置用户好友ID
	 */
	public void setFriendID(int friendID) {
		this.friendID = friendID;
	}

	/**
	 * 设置好友IP
	 */
	public void setfriendIP(String friendIP) {
		this.friendIP = friendIP;
	}

	/**
	 * 返回好友IP
	 */
	public String getfriendIP() {
		return friendIP;
	}
}
