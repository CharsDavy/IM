/**
 * 响应请求事件。线程类
 */

package server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import setting.Embody;
import setting.User;

/**
 * 
 * @author
 */
public class Work extends Thread {
	/**
	 * 定义变量
	 */
	public int id;
	/**
	 * 定义对象输入流
	 */
	private ObjectInputStream netIn = null;
	/**
	 * 定义对象输出流
	 */
	private ObjectOutputStream netOut = null;
	/**
	 * 定义Socket
	 */
	private Socket socket = null;
	/**
	 * 接收网络信息标志
	 */
	private boolean runFlag = true;
	/**
	 * 定义Server对象
	 */
	private Serve serve = new Serve();

	/**
	 * 构造函数
	 */
	Work(Serve serve, Socket socket) throws IOException {

		this.serve = serve;
		this.socket = socket;
		netIn = new ObjectInputStream(socket.getInputStream());
		netOut = new ObjectOutputStream(socket.getOutputStream());
	}

	/**
	 * 线程主体
	 */
	public void run() {

		while (runFlag) {
			receive(); // 始终接受来自客户的信令，除非用户退出
		}

	}

	/**
	 * 向网络发送消息
	 */
	public void send(Embody objMsg) {

		try {
			netOut.writeObject(objMsg);
			netOut.flush();
		} catch (IOException e) {
			System.out.println("发送异常" + e.getMessage());
		}
	}

	/**
	 * 接收信令，调用chatProtocol
	 */
	@SuppressWarnings("deprecation")
	public void receive() {
		Embody objMsg = new Embody();
		try {
			if ((objMsg = (Embody) netIn.readObject()) != null) {

				serve.serverUI.setShoweventArea(true);
				serve.serverUI.setShowsearchInfoArea(false);
				serve.serverUI.layout.first(serve.serverUI.jPaneShowTextArea);
				serve.serverUI.geteventArea().append(
						Serve.getTime() + " 处理用户请求：" + objMsg.getOrder() + "\n");
				serve.serverUI.geteventArea().selectAll();
				chatProtocol(objMsg);
			}
		} catch (IOException e) {
			System.out.println("IO异常");
			stop();// 终止正在运行的线程
		} catch (ClassNotFoundException e) {
			System.out.println("找不到这个类");
		}
	}

	/**
	 * 接受信令,调用Work接收信令的 注册新用户 聊天 用户登录 添加好友  消息签收 删除好友 修改个人信息 检查是否有留言
	 * 检查用户是否在线方法 关闭连接发送离线消息
	 */
	public void chatProtocol(Embody objMsg) {
		registerNewUser(objMsg);
		login(objMsg);
		addFriend(objMsg);
		modifyUserInfo(objMsg);
		closeConn(objMsg);//
		personchat(objMsg);// 聊天
		checkliuyan(objMsg);
		delfriend(objMsg);
		checkfriendstatus(objMsg);
		checkFreindInfo(objMsg);
		checkFreindIP(objMsg);
		getFriendIP(objMsg);
	}

	/**
	 * 接收用户登陆相关信令
	 */

	public void login(Embody objMsg) {
		String order = objMsg.getOrder();// 得到信令
		if (order.equals(setting.Command.login)) {
			User user = new User();// 保存从客户端传来的号码和密码
			User userInfo = new User();// 保存从数据库读出的USER对象
			user = (User) objMsg.getObjMessage();// 传来的只有号码和密码
			System.out.println("用户登录");
			if (!new File(".\\UserDB.db").exists())
			// 若用户数据库不存在则返回给客户密码错误消息
			{
				objMsg.setOrder(setting.Command.loginfail);// 设置反馈给客户登录失败的信令
				send(objMsg);
				return;
			}
			userInfo = serve.readUserDBbyID(user.getID());// 根据号码读取
			if (userInfo.getID() == user.getID()
					&& userInfo.getPassword().equals(user.getPassword())) {// 判断所传来的ID和密码是否相等
				serve.saveWorkAndUser(userInfo);
				// 把此用户存在USERINFO中用与和其他客户交互时 查询相关线程
				// 主要是为了获取ID
				if (serve.isOnline(userInfo))// 判断此用户是否登录 在线则返回真
				{
					objMsg.setSourceID(userInfo.getID());
					objMsg.setDestID(userInfo.getID());
					objMsg.setOrder(setting.Command.loginreapt);
					send(objMsg);
					return;// 若登录了则返回
				}
				userInfo.setIP(user.getIP());
				userInfo.setOnline();// 将用户设置为在线状态
				serve.modifyUserInfo(userInfo);
				// 保存以后再看用户的状态是否修改
				objMsg.setOrder(setting.Command.loginsuccess);// 发给客服注册成功的信令
				objMsg.setObjMessage(userInfo);// 把此用户的信息反馈给客服端
				send(objMsg);
				serve.serverUI.geteventArea().append(
						Serve.getTime() + " 用户验证成功 "
								+ String.valueOf(userInfo.getID()) + " 连入服务器"
								+ "\n");

			} else {
				// 密码不正确
				objMsg.setOrder(setting.Command.loginfail);
				send(objMsg);
			}
		}
	}

	/**
	 * 查询好友状态
	 */
	public void checkfriendstatus(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.registerfail))// 读取好友的状态
		{
			User userInfo = new User();
			User user = new User();
			user = (User) objMsg.getObjMessage();
			userInfo = serve.readUserDBbyID(user.getID());
			User userfriendInfo = new User();
			if (userInfo.getFriend().size() > 0) {
				for (int i = 0; i < userInfo.getFriend().size(); i++) {
					userfriendInfo = (User) userInfo.getFriend().elementAt(i);
					if (serve.isOnline(userfriendInfo))// 好友在线 我需把他加如我的好友列表
					{
						objMsg = new Embody();
						objMsg.setSourceID(userInfo.getID());
						objMsg.setDestID(userfriendInfo.getID());// 传此好友的ID号给我
																	// 以便添加在哪
						objMsg.setObjMessage(userfriendInfo);// 传去好友对象
						objMsg.setOrder(setting.Command.friendisonline);// 好友在线的信令
						send(objMsg);
					}
					if (!serve.isOnline(userfriendInfo))// 不在线 
					{
						objMsg = new Embody();
						objMsg.setSourceID(userInfo.getID());
						objMsg.setDestID(userfriendInfo.getID());
						objMsg.setObjMessage(userfriendInfo);
						objMsg.setOrder(setting.Command.friendisoffline);// 好友不在线的信令
						send(objMsg);
					}
				}
			}
		}
		if (order.equals(setting.Command.updatemetofriend)) {
			objMsg.setOrder(setting.Command.updatemetofriendaccept);
			serve.sendpersonal(objMsg.getDestID(), objMsg);// 目标ID是好友的
		}

	}

	/**
	 * 接收用户注册信令
	 */
	//@SuppressWarnings("unchecked")
	public void registerNewUser(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.register)) {
			User userInfo = new User();// 这是和服务器的首次通信 需实例化userInfo
			// 用户的初始状态为1（1为不在线 0为在线）
			userInfo = (User) objMsg.getObjMessage(); // 把传来的User对象传给userInfo
			if (!new File(".\\UserDB.db").exists())// 若用户数据库文件不存在 即注册的第一个客户
			{
				userInfo.setID(1000);// 注册号码的是第一个用户1000
				serve.addNewUser(userInfo);
				objMsg.setOrder(setting.Command.registersuccess);
				objMsg.setObjMessage(userInfo);
				send(objMsg);

			} else {// 若用户数据库文件存在
				serve.readUserDB(); // 把用户文件中的用户读入arraylist中
				Iterator iter = serve.getArrayList().iterator();
				int account = 0;//用户号
				//随机生成用户号
				account=(int) (Math.random()*1000)+1000;
				while (iter.hasNext()) {
					@SuppressWarnings("unused")					
					User user = (User) iter.next();
					while(user.getID()==account){//检测用户号是否已经存在，若存在则重新生成
						account=(int) (Math.random()*1000)+1000;
					}	
				}
				userInfo.setID(account);
				serve.addNewUser(userInfo);// 不用再实例化arraylist
				objMsg.setOrder(setting.Command.registersuccess);
				objMsg.setObjMessage(userInfo);
				send(objMsg);
			}
		}// 用户注册
	}

	/**
	 * 接收用户关闭网络连接信令
	 */
	public void closeConn(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals("退出")) {
			// 不再接收来自网络的消息 但还是要要向好友发送的
			User exituser = new User();
			exituser = serve.readUserDBbyID(objMsg.getSourceID());
			exituser.setOffline();
			serve.saveDB(exituser);
			serve.serverUI.geteventArea().append(
					Serve.getTime() + " 用户" + String.valueOf(exituser.getID())
							+ " 离线" + "\n");
			User userfriendInfo = new User();
			if (exituser.getFriend().size() > 0) {
				for (int i = 0; i < exituser.getFriend().size(); i++) {
					userfriendInfo = (User) exituser.getFriend().elementAt(i);
					if (serve.isOnline(userfriendInfo))// 若用户在线 才发出我离线的通知
					{
						objMsg.setOrder(setting.Command.friendoffline);
						objMsg.setDestID(userfriendInfo.getID());
						objMsg.setSourceID(exituser.getID());
						objMsg.setObjMessage(userfriendInfo);
						serve.sendpersonal(userfriendInfo.getID(), objMsg);
					}
				}
			}
			try {
				runFlag = false;
				netIn.close();
				netOut.close();
				socket.close();
			} catch (IOException e) {
				System.out.println("关闭连接异常");
			}
		}
	}

	/**
	 * 接收用户添加好友相关信令
	 */
	public void addFriend(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.addfriend))// 由Client的addFriend()发出
		{
			User userInfo = new User();// 实例化一个新的userInfo
			User user = new User();
			int userOtherid = objMsg.getDestID();// 得到被加的人的号码
			userInfo = serve.readUserDBbyID(objMsg.getSourceID());
			for (int i = 0; i < userInfo.getFriend().size(); i++)// 好友已经添加了不能再添加的判断
			{
				user = (User) userInfo.getFriend().elementAt(i);// 遍历得到朋友的ID
				if (userOtherid == user.getID()) {
					// 提示用户次用户已经是你的好友
					objMsg.setOrder(setting.Command.addfriendreapt);
					objMsg.setObjMessage(userInfo);
					objMsg.setDestID(userInfo.getID());
					send(objMsg);// 传给自己
					return;
				}
			}
			user = serve.readUserDBbyID(userOtherid);
			if (objMsg.getDestID() != user.getID()) {
				// 不存在 或用户不在线 则发送信息告诉客户
				objMsg.setOrder(setting.Command.addfriendisnotexist);
				send(objMsg);
				return;
			}
			// 被请求添加的用户必须在线而且好友ID存在
			if (objMsg.getDestID() == user.getID() && user.getStatus() == 0) {
				objMsg.setOrder(setting.Command.addfriendwait);
				objMsg.setObjMessage(user);// 把查找到的用户信息发过去
				serve.sendpersonal(user.getID(), objMsg);
				return;
			} else {
				// 不存在 或用户不在线 则发送信息告诉客户
				objMsg.setOrder(setting.Command.addfriendisoffline);
				serve.sendpersonal(userInfo.getID(), objMsg);
			}
		}
		// 向被加的人发送加的人的确认信息再把对方加为入好友列表
		if (order.equals(setting.Command.addfriendagree)) {
			User userOther = new User();
			User userInfo = new User();
			userOther = (User) objMsg.getObjMessage();// 被加人的信息
			userInfo = serve.readUserDBbyID(objMsg.getSourceID());
			userInfo.addFriend(userOther);// 添加自己的好友
			serve.saveDB(userInfo);
			objMsg.setFriendID(userOther.getID());// 发过去以便保存在自己在线好友列表中
			objMsg.setOrder(setting.Command.addfriendagree);
			objMsg.setObjMessage(userInfo);// 把自己的信息发过去以便获取好友信息
			serve.sendpersonal(userInfo.getID(), objMsg);
		}
		if (order.equals(setting.Command.addfriendagin)) {
			User userother = new User();// 为了读取被加的这这好友的的好友信息是否包括我
			User userInfo = new User();
			userother = serve.readUserDBbyID(objMsg.getDestID());
			if (userother.getFriend().size() > 0) {
				for (int i = 0; i < userother.getFriend().size(); i++) {
					userInfo = (User) userother.getFriend().elementAt(0);
					if (userInfo.getID() == objMsg.getSourceID()) {
						return;// 如果目标ID的好友中有我则不需要再发送
					}
				}
			}
			objMsg.setOrder(setting.Command.addfriendaginconfirm);
			serve.sendpersonal(objMsg.getDestID(), objMsg);// 接受到信息的是
		}
		if (order.equals(setting.Command.addfriendagincancel)) {
			User userOther = (User) objMsg.getObjMessage();// 被加人的信息
			User userInfo = new User();
			userInfo = serve.readUserDBbyID(objMsg.getDestID());
			userInfo.addFriend(userOther);// 添加自己的好友
			serve.saveDB(userInfo);
			objMsg.setOrder(setting.Command.addfriendaginflush);
			objMsg.setFriendID(objMsg.getSourceID());// 设置好友
			serve.sendpersonal(objMsg.getDestID(), objMsg);
		}
		if (order.equals(setting.Command.addfriendrefuse)) {
			objMsg.setOrder(setting.Command.addfriendrefuse);
			serve.sendpersonal(objMsg.getSourceID(), objMsg);
		}
	}

	/**
	 * 接收用户聊天信令
	 */
	public void personchat(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.personchat)) {
			User destuser = serve.readUserDBbyID(objMsg.getDestID());
			if (serve.isOnline(destuser))
				//;// 检查对方是否在线
			{
				objMsg.setOrder(setting.Command.personchatsuccess);
				serve.sendpersonal(objMsg.getDestID(), objMsg);
			}
			if (!serve.isOnline(destuser))// 不在线还要告诉对方
			{
				objMsg.setOrder(setting.Command.personchatfail);// 给自己说的
				serve.sendpersonal(objMsg.getSourceID(), objMsg);
				// 保存信息
				destuser.setHasNote(true);
				destuser.setNote((String) objMsg.getObjMessage());
				destuser.setNoteTxId(objMsg.getSourceID());
				serve.modifyUserInfo(destuser);
			}
		}
	}

	/**
	 * 接收用户删除好友信令
	 */
	public void delfriend(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals("删除好友")) {
			User userInfo = new User();
			userInfo = serve.readUserDBbyID(objMsg.getSourceID());// 根据源ID读出数据
			if (serve.delfriend(userInfo, objMsg.getDestID())) {
				objMsg.setOrder(setting.Command.delfriendsuccess);
				objMsg.setObjMessage(userInfo);
				send(objMsg);
			} else {
				objMsg.setOrder(setting.Command.delfriendfail);
				objMsg.setObjMessage(userInfo);
				send(objMsg);
			}
		}
	}

	/**
	 * 接收用户取留言的信令
	 */
	public void checkliuyan(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.checkfriendnote)) {
			User liuuser = new User();
			liuuser = serve.readUserDBbyID(objMsg.getSourceID());
			if (liuuser.hasNote()) {
				objMsg.setOrder(setting.Command.hasnote);
				objMsg.setObjMessage(liuuser);
				send(objMsg);
				liuuser.setHasNote(false);
			} else {
				objMsg.setOrder(setting.Command.nonote);
				objMsg.setObjMessage(liuuser);
				send(objMsg);
			}
			serve.modifyUserInfo(liuuser);
		}
	}

	/**
	 * 接收用户修改用户信息信令
	 */
	public void modifyUserInfo(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.modifyusrinfo)) {
			serve.modifyUserInfo((User) objMsg.getObjMessage());
			objMsg.setDestID(objMsg.getSourceID());
			objMsg.setObjMessage(serve.readUserDBbyID(objMsg.getSourceID()));
			objMsg.setOrder(setting.Command.modifyusrinfosuccess);
			send(objMsg);
		}
	}

	/**
	 * 查询好友信息信令
	 */
	public void checkFreindInfo(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.queryfriendinfo)) {
			objMsg.setObjMessage(serve.readUserDBbyID(objMsg.getDestID()));
			objMsg.setOrder(setting.Command.queryfriendinfo);
			send(objMsg);
		}
	}

	/**
	 * 查询好友IP
	 */
	public void checkFreindIP(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.queryfriendIP)) {
			objMsg.setObjMessage(serve.readUserDBbyID(objMsg.getDestID()));
			objMsg.setOrder(setting.Command.queryfriendIP);
			send(objMsg);
		}
	}
////////////////////////////////////////
	public void getFriendIP(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.filetransmit)) {
			objMsg.setObjMessage(serve.readUserDBbyID(objMsg.getDestID()));
			objMsg.setOrder(setting.Command.filetransmit);
			send(objMsg);
		}
	}
}
