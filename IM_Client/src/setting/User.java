/**
 * 客户端功能实现
 */

package setting;

import java.io.Serializable;
import java.util.Vector;

/**
 * 
 * @author
 */
public class User implements Serializable {
	/**
	 * 定义serialVersionUID
	 */
	private static final long serialVersionUID = 1198641954990687835L;

	/**
	 * 定义用户姓名
	 */
	private String name;

	/**
	 * 定义用户账号
	 */
	private int ID;

	/**
	 * 定义用户IP
	 */
	private String IP;
	/**
	 * 定义用户密码
	 */
	private String password;

	/**
	 * 定义用户好友信息
	 */
	// @SuppressWarnings("unchecked")
	private Vector friends = new Vector();

	/**
	 * 定义用户在线状态 1为不在线 0为在线
	 */
	private int status = 1;

	/**
	 * 定义用户昵称
	 */
	private String nickName;

	/**
	 * 定义用户年龄
	 */
	private String age;

	/**
	 * 定义用户性别
	 */
	private String sex;
	/**
	 * 定义用户星座
	 */
	private String constellation;

	/**
	 * 定义用户个人说明
	 */
	private String explain;

	/**
	 * 定义信号量：用户是否有留言
	 */
	private boolean hasNote = true;

	/**
	 * 定义给用户的留言信息
	 */
	private String note;

	/**
	 * 定义给该用户留言的用户号码
	 */
	private int noteTxId;

	/**
	 * 定义信号量：用户是否有消息
	 */
	private boolean hasMessage = true;

	/**
	 * 定义给用户的消息
	 */
	private String messageTx;

	/**
	 * 定义给该用户消息的用户号码
	 */
	private int messageTxId;

	/**
	 * 返回用户号码
	 */
	public int getID() {
		return ID;
	}

	/**
	 * 设置用户帐号
	 */
	public void setID(int id) {
		ID = id;
	}

	/**
	 * 返回客户端地址
	 */
	public String getIP() {
		return IP;
	}

	/**
	 * 设置客户端地址
	 */
	public void setIP(String IP) {
		this.IP = IP;
	}

	/**
	 * 返回用户姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置用户姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 返回用户的好友
	 */
	// @SuppressWarnings("unchecked")
	public Vector getFriend() {
		return friends;
	}

	/**
	 * 添加好友
	 */
	@SuppressWarnings("unchecked")
	public void addFriend(User friend) {
		friends.add(friend);
	}

	/**
	 * 返回用户状态
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置用户状态为在线状态
	 */
	public void setOnline() {
		this.status = 0;
	}

	/**
	 * 设置用户状态为不在线状态
	 */
	public void setOffline() {
		this.status = 1;
	}

	/**
	 * 用户在线
	 */
	public boolean isOnline() {
		boolean online = false;
		if (this.status == 0) {
			online = true;
		} else {
			online = false;
		}
		return online;
	}

	/**
	 * 返回年龄
	 */
	public String getAge() {
		return age;
	}

	/**
	 * 设置年龄
	 */
	public void setAge(String age) {
		this.age = age;
	}

	/**
	 * 返回个人说明
	 */
	public String getExplain() {
		return explain;
	}

	/**
	 * 设置个人说明
	 */
	public void setExplain(String explain) {
		this.explain = explain;
	}

	/**
	 * 返回用户昵称
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * 设置用户昵称
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * 返回性别
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 设置性别
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * 设置用户状态
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 返回星座
	 */
	public String getConstellation() {
		return constellation;
	}

	/**
	 * 设置星座
	 */
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	/**
	 * 返回是否有留言boolean值
	 */
	public boolean hasNote() {
		return hasNote;
	}

	/**
	 * 设置用户是否有留言
	 */
	public void setHasNote(boolean hasNote) {
		this.hasNote = hasNote;
	}

	/**
	 * 返回用户的留言信息
	 */
	public String getNote() {
		return note;
	}

	/**
	 * 设置用户的留言信息
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * 返回给用户留言者的ID
	 */
	public int getNoteTxId() {
		return noteTxId;
	}

	/**
	 * 设置给用户留言者的ID
	 */
	public void setNoteTxId(int noteTxId) {
		this.noteTxId = noteTxId;
	}

	/**
	 * 返回是否有消息boolean值
	 */
	public boolean hasMessage() {
		return hasMessage;
	}

	/**
	 * 设置用户是否有消息
	 */
	public void setHasMessage(boolean hasMessage) {
		this.hasMessage = hasMessage;
	}

	/**
	 * 返回用户的消息
	 */
	public String getMessageTx() {
		return messageTx;
	}

	/**
	 * 设置用户的所留消息
	 */
	public void setMessageTx(String messageTx) {
		this.messageTx = messageTx;
	}

	/**
	 * 返回给用户留料消息人的ID
	 */
	public int getMessageTxId() {
		return messageTxId;
	}

	/**
	 * 设置给用户留消息人的ID
	 */
	public void setMessageTxId(int messageTxId) {
		this.messageTxId = messageTxId;
	}
}
