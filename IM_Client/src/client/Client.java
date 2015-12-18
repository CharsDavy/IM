/**
 * 客户端
 */

package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

import setting.AudioPlay;
import setting.Embody;
import setting.User;

/**
 * 
 * @author
 */
public class Client {
	/**
	 * 定义全局变量
	 */
	public String message;
	public int id;
	public String friendIP = null;
	/**
	 * 定义Socket 通过"套接字"向网络发出请求或者应答网络请求
	 */
	private static Socket socket;

	/**
	 * 客户端主界面
	 */
	private static ClientMainUI mainUI;

	/**
	 * 树形好友列表
	 */
	static FriendTree friendTree;

	/**
	 * 修改个人信息基面
	 */
	static ModifyUserUI modify;

	/**
	 * 登录界面
	 */
	static LoginUI loginui;

	/**
	 * 注册界面
	 */
	static UserRegUI userRegUI;

	/**
	 * 聊天窗口
	 */
	private static ChatUI chatui;

	/**
	 * 聊天记录窗口
	 */
	private static Chatrecord chatrecord;
	/**
	 * 对象输入流
	 */
	private ObjectInputStream netIn;

	/**
	 * 对象输出流
	 */
	private ObjectOutputStream netOut;

	/**
	 * 保存用户聊天窗口souceID和destID更据它判断是否需要NEW新的窗口
	 */
	// @SuppressWarnings("unchecked")
	private Vector chatUIvector = new Vector();

	/**
	 * 保存用户聊天记录窗口souceID和destID更据它判断是否需要NEW新的窗口
	 */
	// @SuppressWarnings("unchecked")
	private Vector chatrecordvector = new Vector();
	/**
	 * 客户端运行标志
	 */
	private boolean runFlag = true;

	/**
	 * 用来保存自己的信息,其值常改变
	 */
	private User userInfo;

	/**
	 * 用户登录成功后 信息保存在此对象 其值不再改变 用于获取ID,设置窗体标题
	 */
	private User constuser = new User();

	/**
	 * 延时接收信令信号量
	 */
	private static boolean acceptorder = false;

	/**
	 * 控制ClientMainUI中 jLabel显示
	 */
	private static boolean hidden = true;

	/**
	 * 暂时储存信令
	 */
	private String ordersave;

	/**
	 * 储存添加好友和下线时的信令
	 */
	// @SuppressWarnings("unchecked")
	private Vector orderSys = new Vector();

	/**
	 * 用于客户端其他类调用
	 */
	public static Client client;

	/**
	 * 播放声音
	 */
	private AudioPlay aplay = new AudioPlay();
	javax.swing.Timer time;

	/**
	 * 启动客服端与服务器建立连接
	 */
	public void init() {
		try {
			String ip = "";
			String port = "8888";
			ip = JOptionPane.showInputDialog("服务器IP", "127.0.0.1");
			int portInt = Integer.parseInt(port);
			socket = new Socket(ip, portInt);
			loginui = new LoginUI();
			loginui.getwinFrame();
			netOut = new ObjectOutputStream(socket.getOutputStream());
			netIn = new ObjectInputStream(socket.getInputStream());
			receive();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "网络异常，连接服务端失败", "提示", 1);
			System.exit(0);
		}
	}

	/**
	 * 把对象输出网络
	 */
	public void send(Embody objMessage) {
		try {
			netOut.writeObject(objMessage);
			netOut.flush();
		} catch (Exception e) {
			System.out.println("网络输出异常" + e.getMessage());
		}
	}

	/**
	 * 从网络接收对象
	 */
	public void receive() {
		Embody objMsg = new Embody();
		while (runFlag) {
			try {
				if ((objMsg = (Embody) netIn.readObject()) != null) {
					chatProtocol(objMsg);
				}
			} catch (IOException e) {
				System.out.println("网络接收异常" + e.getMessage());
			} catch (ClassNotFoundException e) {
				System.out.println("找不到这个类" + e.getMessage());
			} catch (Exception e) {
				System.out.println("未知异常" + e.getMessage());
			}
		}
	}

	/**
	 * 响应ClientMainUI的喇叭事件 接收到系统消息后
	 */
	public void acceptOrder() {
		Client.hidden = false;
		this.msgShandong(hidden);
	}

	/**
	 * 信令接收的全部方法
	 */
	@SuppressWarnings("unchecked")
	public void chatProtocol(Embody objMsg) throws IOException,
			ClassNotFoundException {
		this.loginOrderRecieve(objMsg);
		this.regOrderRecieve(objMsg);
		this.reicievModifyUserInfo(objMsg);
		this.chatMsgRecieve(objMsg);
		this.delFriendOrderRecieve(objMsg);
		this.addfriendfailInfoOder(objMsg);
		this.checkFriendInfoOrderRecieve(objMsg);
		this.checkfriendIPReceive(objMsg);
		this.getIPofFriend(objMsg);
		if (objMsg.getOrder().equals(setting.Command.addfriendwait)
				|| objMsg.getOrder()
						.equals(setting.Command.addfriendaginconfirm)
				|| objMsg.getOrder().equals(setting.Command.addfriendagree)
				|| objMsg.getOrder().equals(setting.Command.addfriendrefuse)
				|| objMsg.getOrder().equals(setting.Command.friendoffline)) {
			if (objMsg.getOrder().equals(setting.Command.addfriendwait)
					|| objMsg.getOrder().equals(
							setting.Command.addfriendaginconfirm)
					|| objMsg.getOrder()
							.equals(setting.Command.addfriendagree)
					|| objMsg.getOrder().equals(
							setting.Command.addfriendrefuse)
					|| objMsg.getOrder().equals(setting.Command.friendoffline)) {
				aplay.soundPlay("/music/kesou.wav");
			}
			orderSys.add(objMsg.getOrder());// 将收来的信令保存
			hidden = true;// 收到信令就开始闪动
			this.msgShandong(hidden);
			if (!this.msgShandong(hidden)) {
				acceptorder = true;
			}
		}
		while (Client.acceptorder) {
			for (int i = 0; i < orderSys.size(); i++) {
				ordersave = (String) orderSys.elementAt(i);
				objMsg.setOrder(ordersave);
				orderSys.removeElementAt(i);// 删除已发信令
				this.addFriendOrderRecieve(objMsg);
				this.friendlogoutOrderRecieve(objMsg);
				Client.acceptorder = false;
			}
		}
	}

	/**
	 * ClientMainUI中头像是否闪动
	 */
	public void run() {
		Client.mainUI.getnoteBabo().setVisible(false);
	}

	public boolean msgShandong(boolean hidden) {
		while (Client.hidden) {
			for (int i = 0;; i = i + 5) {
				if (i % 2 == 0) {
					Client.mainUI.getnoteBabo().setVisible(false);
				}
				if (i % 2 == 1) {
					Client.mainUI.getnoteBabo().setVisible(true);
				}
				if (!Client.hidden) {
					Client.mainUI.getnoteBabo().setVisible(true);
					break;
				}
			}
		}
		return hidden;
	}

	/**
	 * 接收注册信令
	 */
	public void regOrderRecieve(Embody objMsg)// 注册信令
	{
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.registersuccess)) {
			JOptionPane.showMessageDialog(
					null,
					"注册成功,您的号码为"
							+ String.valueOf(((User) objMsg.getObjMessage())
									.getID()), "提示", 1);
			loginui.getIDTextField().setText(
					String.valueOf(((User) objMsg.getObjMessage()).getID()));
			loginui.getPasswordField().setText("");
			Client.userRegUI.getwinFrame().setVisible(false);
			return;
		}
	}

	/**
	 * 接收登录信令和留言信令
	 */
	public void loginOrderRecieve(Embody objMsg)// 登录信令接受
	{
		String order = objMsg.getOrder();
		/**
		 * 收到登录成功信令
		 */
		if (order.equals(setting.Command.loginsuccess))// 得到登录成功的信令后还需更新好友列表
		{
			aplay.soundPlay("/music/shangxian.wav");
			Client.loginui.getwinFrame().setVisible(false);// 登录成功后隐藏登录界面
			userInfo = new User();
			userInfo = (User) objMsg.getObjMessage();// 更新从服务器传来的数据
			constuser = userInfo;// 保存
			mainUI = new ClientMainUI();// 实例化主界面
			friendTree = new FriendTree();// 实例化保存好友列表的JTree
			if (userInfo.getFriend().size() == 0)// 没有好友
			{
				mainUI.setFriend(friendTree.chushi());
				mainUI.getwinFrame();// 得到JFrame意味着生成了所有的组件
				mainUI.getwinFrame().setVisible(true);
				mainUI.getwinFrame().setTitle(String.valueOf(userInfo.getID()));// 设置标题
				mainUI.getheadpic().setText(String.valueOf(userInfo.getNickName()));/*设置昵称*/

			}
			if (userInfo.getFriend().size() > 0)// 有好友
			{
				// 发出查询请求好友在线状态请求
				objMsg = new Embody();
				objMsg.setSourceID(userInfo.getID());
				objMsg.setOrder(setting.Command.registerfail);
				objMsg.setObjMessage(userInfo);
				send(objMsg);
			}
		}

		/**
		 * 收到有好友留言信令 打开留言窗口
		 */
		if (order.equals(setting.Command.hasnote)) {
			aplay.soundPlay("/music/msg.wav");
			User user = new User();
			user = (User) objMsg.getObjMessage();
			if (user.getNote() != null) {
				ChatUI c2 = new ChatUI(constuser.getID(), constuser.getID());
				c2.getwinFrame();
				c2.getwinFrame().setVisible(true);
				c2.win.setTitle("好友" + String.valueOf(user.getNoteTxId())
						+ "给您留言");
//////////////////////////解析图片表情///////////////////////////////////////////////
				int length = user.getNote().length();// 获取String 长度
				char[] every = new char[length];
				int count = 0;// 初始字符的位置，变化
				String path = "phiz/";
				
				// 实现insertString()的必要前提
				Document doc = c2.dsiplChatArea.getStyledDocument();
				SimpleAttributeSet attr = new SimpleAttributeSet();
				Boolean hadjin = false;
				for (int i1 = 0; i1 < user.getNote().length(); i1++) {
					every[i1] = user.getNote().charAt(i1);
					if (every[i1] == '#') // 识别信息中是否有#号
						hadjin = true;
				}
				// 开始解析
				for (int i1 = 0; i1 < user.getNote().length(); i1++) {
					if (hadjin = false)
						break;
					if (every[i1] == '#') {
						String str = null;
						str = user.getNote().substring(count, i1); // 得到表情前的文字

						try {
							if (str != null)
								doc.insertString(doc.getLength(), str, attr);//添加表情前的文字
						} catch (Exception e2) {
							e2.printStackTrace();
						}

						String icName;
						icName =user.getNote().substring(i1, i1 + 3); // 得到表情的名字 

						Icon ic = new ImageIcon(path + icName + ".gif");//将表情转化为icon
						c2.dsiplChatArea.setCaretPosition(doc.getLength());// 获取当前的位置
						c2.dsiplChatArea.insertIcon(ic); // 加入表情
						count = i1 + 3;// 将字符起始位置跳到表情后第一位置
					}
				}
				if (count <= user.getNote().length()) {
					String theLast = null;
					theLast = user.getNote().substring(count, length);
					try {
						doc.insertString(doc.getLength(), theLast, attr);
					} catch (Exception e3) {
						e3.printStackTrace();
					}
				}
	///////////////////////////////////////////////////////////
				//c2.dsiplChatArea.setText(user.getLiuyanmsg() + "\n");
				c2.win.setDefaultCloseOperation(1);
			}
			objMsg.setOrder(setting.Command.updatemetofriend);
			send(objMsg);
		}

		/**
		 * 收到没有留言信令 发送我上线的命令给其他好友 令其更新好友列表
		 */
		if (order.equals(setting.Command.nonote))// 每收到留言 也需更新把我更新到对方的好友列表中
		{
			objMsg.setOrder(setting.Command.updatemetofriend);
			send(objMsg);
		}

		/**
		 * 收到好友在线信令
		 */
		if (order.equals(setting.Command.friendisonline))// 收到好友在线的信令
		{
			if (friendTree.online == null)// 若树还没有实例化则先实例化
			{
				mainUI.setFriend(friendTree.chushi());
			}
			friendTree.addonlinefriend(objMsg.getDestID());// 将此ID加在在线的好友列表中
			mainUI.getwinFrame();
			mainUI.getwinFrame().setVisible(true);
			aplay.soundPlay("/music/shangxian.wav");
			mainUI.getwinFrame().setTitle(String.valueOf(constuser.getID()));
			objMsg.setSourceID(userInfo.getID());
			// 再看看有人给我留言没有
			objMsg.setOrder(setting.Command.checkfriendnote);
			objMsg.setObjMessage(userInfo);
			send(objMsg);
			// 若好友在线我还需则把我上线的消息发给她
			// 再向服务器发出信息 以便把我加入对方的好友列表中
		}
		/**
		 * 收到好友不在线信令
		 */
		// 收到好友不在线的信令，不需要再更新好友的好友列表
		if (order.equals(setting.Command.friendisoffline)) {
			if (friendTree.quitline == null) {
				mainUI.setFriend(friendTree.chushi());
			}
			friendTree.addnotonlinefriend(objMsg.getDestID());
			mainUI.getwinFrame();
			mainUI.getwinFrame().setVisible(true);
			mainUI.getwinFrame().setTitle(
					constuser.getNickName() + "("
							+ String.valueOf(constuser.getID()) + ")");
			objMsg.setSourceID(userInfo.getID());
			objMsg.setOrder(setting.Command.checkfriendnote);
			objMsg.setObjMessage(userInfo);
			send(objMsg);
		}
		/**
		 * 收到登录重复信令
		 */
		if (order.equals(setting.Command.loginreapt)) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(null, " 用户" + objMsg.getDestID()
					+ "已经登录", "警告", 1);
		}
		/**
		 * 收到更新好友列表信令
		 */
		if (order.equals(setting.Command.updatemetofriendaccept)) {
			// 收到来自别的客户的请求 把客户更新到我的好友列表
			if (userInfo.getID() == objMsg.getDestID()) {
				aplay.soundPlay("/music/shangxian.wav");
				JOptionPane.showMessageDialog(mainUI.getwinFrame(), " 用户"
						+ objMsg.getSourceID() + "上线", " 提示", 1);
				friendTree.shangxian((User) objMsg.getObjMessage(),
						objMsg.getSourceID());
				mainUI.getwinFrame();
				mainUI.getwinFrame().setTitle(
						constuser.getNickName() + "("
								+ String.valueOf(constuser.getID()) + ")");
				// mainUI.getjFrame().setTitle(String.valueOf(constuser.getID()));
			}
		}

		/**
		 * 收到登录失败信令
		 */
		if (order.equals(setting.Command.loginfail)) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(loginui.getwinFrame(),
					"用户名或密码错误,请重新输入", "警告", 1);
			loginui.PasswordField.setText("");
		}
	}

	/**
	 * 添加好友更新好友列表相关信令接收
	 */
	public void addFriendOrderRecieve(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.addfriendwait)) {
			int i = JOptionPane.showConfirmDialog(mainUI.getwinFrame(),
					"是否同意用户" + objMsg.getSourceID() + "加您为好友", "提示",
					JOptionPane.YES_NO_OPTION);
			if (i == 0) {
				objMsg.setOrder(setting.Command.addfriendagree);
				send(objMsg);
			} else {
				objMsg.setOrder(setting.Command.addfriendrefuse);
				send(objMsg);
			}
		}
		if (order.equals(setting.Command.addfriendagree))//
		{
			userInfo = (User) (objMsg.getObjMessage());
			friendTree.addonlinefriend(objMsg.getFriendID());
			mainUI.getwinFrame();
			mainUI.getwinFrame().setVisible(true);// 更新好友信息后 应该提示对方是否要加我
			JOptionPane.showMessageDialog(mainUI.getwinFrame(), " 添加好友成功",
					"友情提示", 1);
			objMsg.setObjMessage(userInfo);// 把自己对象传过去
			objMsg.setOrder(setting.Command.addfriendagin);
			send(objMsg);
		}
		if (order.equals(setting.Command.addfriendrefuse)) {
			JOptionPane.showMessageDialog(mainUI.getwinFrame(), "对方不同意", "警告",
					1);
		}
		if (order.equals(setting.Command.addfriendaginconfirm)) {

			int i = JOptionPane.showConfirmDialog(
					mainUI.getwinFrame(),
					"用户" + objMsg.getSourceID() + "已经加您为好友，你是否要加"
							+ objMsg.getSourceID() + "为好友", "提示",
					JOptionPane.YES_NO_OPTION);
			if (i == 0) {
				objMsg.setOrder(setting.Command.addfriendagincancel);
				send(objMsg);
			}
		}
	}

	/**
	 * 添加好友失败的错误信令及更新好友列表信令
	 */
	public void addfriendfailInfoOder(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.addfriendisnotexist)) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(mainUI.getwinFrame(), " 没有这个号码",
					"警告", 1);
		}
		if (order.equals(setting.Command.addfriendisoffline)) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(mainUI.getwinFrame(), " 该用户不在线",
					"警告", 1);
		}
		if (order.equals(setting.Command.addfriendreapt)) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(mainUI.getwinFrame(), " 此用户已经是您的好友",
					"警告", 1);
		}
		if (order.equals(setting.Command.addfriendaginflush))// 加好友的最后一次通信
		{
			userInfo = (User) (objMsg.getObjMessage());
			mainUI.getwinFrame();
			friendTree.addonlinefriend(objMsg.getFriendID());
			mainUI.getwinFrame();
			mainUI.getwinFrame().setVisible(true);
			JOptionPane.showMessageDialog(mainUI.getwinFrame(), "好友列表更新成功",
					"提示", 1);
		}
	}

	/**
	 * 接收好友退出信令
	 */
	public void friendlogoutOrderRecieve(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.friendoffline))// 下线通知时更新自己的好友列表
		{
			aplay.soundPlay("/music/bell.wav");
			JOptionPane.showMessageDialog(mainUI.getwinFrame(), " 您的好友"
					+ objMsg.getSourceID() + "下线", "提示", 1);
			userInfo = (User) (objMsg.getObjMessage());
			friendTree.liveline((User) objMsg.getObjMessage(),
					objMsg.getSourceID());
			mainUI.getwinFrame();
			mainUI.getwinFrame().setVisible(true);
		}
	}

	/**
	 * 接收与好友聊天信令
	 */
	@SuppressWarnings("unchecked")
	public void chatMsgRecieve(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.personchatsuccess)) {
			aplay.soundPlay("/music/msg.wav");
			if (Client.chatui == null)// 与第一个客户会话时实例化一个聊天窗口
			{
				chatui = new ChatUI(objMsg.getDestID(), objMsg.getSourceID());
				chatUIvector.add(chatui);
			} else {
				boolean newJFrame = true;
				for (int i = 0; i < chatUIvector.size(); i++) {
					ChatUI c = (ChatUI) chatUIvector.elementAt(i);// 遍历所有ID对
																	// 若找到相等的
					if (c.destid == objMsg.getSourceID()) {
						newJFrame = false;// 不开新窗口

					}
				}
				if (newJFrame)// 若没找到 则开一个新窗口
				{
					aplay.soundPlay("/music/msg.wav");
					chatui = new ChatUI(objMsg.getDestID(),
							objMsg.getSourceID());// 是否有新的好友和你聊天 有则再开启一个窗口
					chatUIvector.add(chatui);
				}
			}
			for (int i = 0; i < chatUIvector.size(); i++) {
				ChatUI c = (ChatUI) chatUIvector.elementAt(i);
				// 构造的时候以自己的ID为源ID以好友的ID为目标ID
				if (objMsg.getDestID() == c.souceid
						&& objMsg.getSourceID() == c.destid)// 若目标ID是 我
				{
					c.getwinFrame();
					c.win.setVisible(true);
// //////////////////////////解析图片表情///////////////////////////////////////////////
					String info=(String) objMsg.getObjMessage();
					int length = info.length();// 获取String 长度
					char[] every = new char[length];
					int count = 0;// 初始字符的位置，变化
					String path = "phiz/";
					
					// 实现insertString()的必要前提
					Document doc = c.dsiplChatArea.getStyledDocument();
					SimpleAttributeSet attr = new SimpleAttributeSet();
					Boolean hadjin = false;
					for (int i1 = 0; i1 < info.length(); i1++) {
						every[i1] = info.charAt(i1);
						if (every[i1] == '#') // 识别信息中是否有#号
							hadjin = true;
					}
					// 开始解析
					for (int i1 = 0; i1 < info.length(); i1++) {
						if (hadjin = false)
							break;
						if (every[i1] == '#') {
							String str = null;
							str = info.substring(count, i1); // 得到表情前的文字

							try {
								if (str != null)
									doc.insertString(doc.getLength(), str, attr);//添加表情前的文字
							} catch (Exception e2) {
								e2.printStackTrace();
							}

							String icName;
							icName = info.substring(i1, i1 + 3); // 得到表情的名字 

							Icon ic = new ImageIcon(path + icName + ".gif");//将表情转化为icon
							c.dsiplChatArea.setCaretPosition(doc.getLength());// 获取当前的位置
							c.dsiplChatArea.insertIcon(ic); // 加入表情
							count = i1 + 3;// 将字符起始位置跳到表情后第一位置
						}
					}
					if (count < info.length()) {
						String theLast = null;
						theLast = info.substring(count, length);
						try {
							doc.insertString(doc.getLength(), theLast, attr);
						} catch (Exception e3) {
							e3.printStackTrace();
						}
					}
// /////////////////////////////////////////////////////////////					
					c.dsiplChatArea.selectAll();
					c.win.setTitle("与好友" + String.valueOf(objMsg.getSourceID())
							+ "对话中");
				}
			}
		}
		if (order.equals(setting.Command.personchatfail)) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(mainUI.getwinFrame(), "信息对方不在线,已留言",
					"提示", 2);
		}
	}

	/**
	 * 接收更新个人设置信令
	 */
	public void reicievModifyUserInfo(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.modifyusrinfosuccess)) {

			userInfo = (User) objMsg.getObjMessage();
			Client.modify.getnickField().setText(userInfo.getNickName());
			Client.modify.NoLabel.setText(String.valueOf(userInfo.getID()));
			Client.modify.getpasswdField().setText(userInfo.getPassword());
			Client.modify.getnameField().setText(userInfo.getName());
			Client.modify.getageField().setText(userInfo.getAge());
			Client.modify.getsexBox().setSelectedItem(userInfo.getSex());
			Client.modify.getstarBox().setSelectedItem(
					userInfo.getConstellation());
			Client.modify.getexpArea().setText(userInfo.getExplain());
			JOptionPane.showMessageDialog(null, " 你的信息更新成功", "提示", 1);

		}

	}

	/**
	 * 响应ClientMainui退出事件
	 */
	public void logout() {
		Embody objMsg = new Embody();
		objMsg.setOrder("退出");
		objMsg.setSourceID(constuser.getID());// 不应拿标题设置
		send(objMsg);
		try {
			client.runFlag = false;// 不再接收信令
			netIn.close();// 关闭网络对象输入流
			netOut.close();// 关闭网络对象输出流
			socket.close();//
			System.exit(0);
		} catch (Exception eclose) {
			System.out.println("客服端退出异常:" + eclose.getMessage());
		}
	}

	/**
	 * 响应LoginUI登录事件
	 */
	public void login(String userID, String userPWD, String IP) {
		User userInfo = new User();// 此userInfo登录时传送过去的只有ID,密码和IP
		try {
			userInfo.setID(Integer.parseInt(userID));
			userInfo.setPassword(userPWD);
		} catch (Exception e) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(null, "用户ID和密码输入错误", "警告", 1);
			return;
		}
		userInfo.setIP(IP);
		Embody objMsg = new Embody(); // 首次通信
		objMsg.setOrder(setting.Command.login);
		objMsg.setObjMessage(userInfo);// 登录时传送过去的只有ID,密码和IP
		send(objMsg);
	}

	/**
	 * 响应ClientMainUI添加好友事件
	 */
	public void addFriend() {// 添加好友
		String s = JOptionPane.showInputDialog(mainUI.getwinFrame(),
				"请输入要加的用户号码", "添加好友", 3);
		if (s == null) {
			return;
		}
		int id = 0;
		try {
			id = Integer.parseInt(s);
		} catch (Exception e) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(mainUI.getwinFrame(), "输入不正确", "警告",
					2);
			return;
		}
		if (id == (Integer.parseInt(mainUI.getwinFrame().getTitle()))) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(mainUI.getwinFrame(), "不能添加自己", "警告",
					2);
			return;
		}
		Embody objMsg = new Embody();
		objMsg.setOrder(setting.Command.addfriend);// 发出向服务器查找的请求信令只包括SourceID和DestID(
		objMsg.setSourceID(constuser.getID());
		objMsg.setDestID(id);
		send(objMsg);
	}

	/**
	 * 响应ClientMainUI删除好友事件
	 */
	public void delFriend(int friendid) {
		Embody objMsg = new Embody();
		objMsg.setOrder(setting.Command.delfriend);// 发出向服务器查找的请求信令只包括SourceID和DestID(
		objMsg.setSourceID(constuser.getID());
		objMsg.setDestID(friendid);
		send(objMsg);
	}

	/**
	 * 接收删除好友返回信令
	 */
	public void delFriendOrderRecieve(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.delfriendsuccess)) {
			friendTree.delfriend(objMsg.getDestID());
			mainUI.getwinFrame();
			mainUI.getwinFrame().setVisible(true);
			aplay.soundPlay("/music/system.wav");
			JOptionPane
					.showMessageDialog(mainUI.getwinFrame(), "删除成功", "提示", 2);
		}
		if (order.equals(setting.Command.delfriendfail)) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(mainUI.getwinFrame(), "没有这个好友", "警告",
					2);
		}
	}

	/**
	 * 响应ChatUI发送按钮事件
	 */
	public void sendChatMsg(String s, int souceid, int destid) {

		Embody objMsg = new Embody();
		objMsg.setOrder(setting.Command.personchat);
		objMsg.setObjMessage(s);
		objMsg.setSourceID(souceid);
		objMsg.setDestID(destid);
		send(objMsg);
	}

	/**
	 * 响应ClientMainUI个人设置按钮事件
	 */
	public void ModifyUserInfo() {
		modify = new ModifyUserUI();// 实例化修改信息窗体
		modify.getwinFrame();// 得到窗体
		modify.getnickField().setText(userInfo.getNickName());
		modify.NoLabel.setText(String.valueOf(userInfo.getID()));//
		modify.getpasswdField().setText(userInfo.getPassword());
		modify.getnameField().setText(userInfo.getName());
		modify.getageField().setText(userInfo.getAge());
		modify.getsexBox().setSelectedItem(userInfo.getSex());
		modify.getstarBox().setSelectedItem(userInfo.getConstellation());
		modify.getexpArea().setText(userInfo.getExplain());
	}

	/**
	 * 相应查询好友信息事件
	 */
	public void checkfriendInfo(int friendid) {
		Embody objMsg = new Embody();
		objMsg.setSourceID(constuser.getID());
		objMsg.setDestID(friendid);
		objMsg.setOrder(setting.Command.queryfriendinfo);
		send(objMsg);
	}

	/**
	 * 相应查询IP事件
	 */
	public void checkfriendIP(int friendid) {
		Embody objMsg = new Embody();
		objMsg.setSourceID(constuser.getID());
		objMsg.setDestID(friendid);
		objMsg.setOrder(setting.Command.queryfriendIP);
		send(objMsg);
	}

	/**
	 * 接受好友IP
	 */
	public void checkfriendIPReceive(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.queryfriendIP)) {
			User friendInfo = new User();
			friendInfo = (User) objMsg.getObjMessage();
			JOptionPane
					.showMessageDialog(null, friendInfo.getIP(), "好友IP地址", 1);
		}
	}

	// ///////////////////////////////////////////////////////
	/**
	 * 得到好友iP
	 */
	public void getfriendIP(int friendid) {
		Embody objMsg = new Embody();
		objMsg.setSourceID(constuser.getID());
		objMsg.setDestID(friendid);
		objMsg.setOrder(setting.Command.filetransmit);
		send(objMsg);
	}

	public void getIPofFriend(Embody objMsg) {
		String order = objMsg.getOrder();

		if (order.equals(setting.Command.filetransmit)) {
			User friendInfo = new User();
			friendInfo = (User) objMsg.getObjMessage();
			this.friendIP = friendInfo.getIP();
			//System.out.println("friendip:" + friendIP);
		}
	}

	// ///////////////////////////////////////////////////////
	/**
	 * 相应打开聊天记录
	 */
	@SuppressWarnings("unchecked")
	public void openChatRecord(String nodename) {
		if (nodename.equals("我的好友") || nodename.equals("在线的好友")
				|| nodename.equals("不在线的好友")) {
			// 选择上叙节点不能弹出节点
		} else {
			if (Client.chatrecord == null) {
				Client.chatrecord = new Chatrecord(constuser.getID(),
						Integer.parseInt(nodename));
				chatrecordvector.add(Client.chatrecord);
			}
			boolean newJFrame = true;
			for (int i = 0; i < chatrecordvector.size(); i++) {

				Chatrecord c = (Chatrecord) chatrecordvector.elementAt(i);// 遍历所有ID对
																			// 若找到相等的
				if (c.destid == Integer.parseInt(nodename)) {
					newJFrame = false;// 不NEW 新窗口
					Client.chatrecord.getwinFrame();
					Client.chatrecord.winFrame.setVisible(true);
					Client.chatrecord.winFrame.setTitle("与好友" + nodename
							+ "的聊天记录");
				}

			}
			if (newJFrame)// 若没找到 则new一个新窗口
			{
				chatrecord = new Chatrecord(constuser.getID(),
						Integer.parseInt(nodename));// 是否有新的好友和你聊天 有则再开启一个窗口
				chatrecordvector.add(chatrecord);
				chatrecord.getwinFrame();
				chatrecord.winFrame.setVisible(true);
				chatrecord.winFrame.setTitle("与好友" + nodename + "的聊天记录");
			}
		}
	}

	/**
	 * 接收好友个人信息
	 */
	public void checkFriendInfoOrderRecieve(Embody objMsg) {
		String order = objMsg.getOrder();
		if (order.equals(setting.Command.queryfriendinfo)) {
			User friendInfo = new User();
			friendInfo = (User) objMsg.getObjMessage();
			modify = new ModifyUserUI();// 实例化修改信息窗体
			modify.getwinFrame();// 得到窗体
			modify.getwinFrame().setTitle("好友的个人资料");
			modify.getnickField().setText(friendInfo.getNickName());
			modify.getnickField().setEditable(false);
			modify.NoLabel.setText(String.valueOf(friendInfo.getID()));
			modify.getpasswdField().setVisible(false);
			modify.getnameField().setText(friendInfo.getName());
			modify.getnameField().setEditable(false);
			modify.getageField().setText(friendInfo.getAge());
			modify.getageField().setEditable(false);
			modify.getsexBox().setSelectedItem(friendInfo.getSex());
			modify.getsexBox().setEnabled(false);
			modify.getstarBox().setEnabled(false);
			modify.getstarBox().setSelectedItem(friendInfo.getConstellation());
			modify.getexpArea().setEditable(false);
			modify.getexpArea().setText(friendInfo.getExplain());
			modify.getalterBtn().setVisible(false);
			modify.getcancelBtn().setVisible(false);
		}
	}

	/**
	 * 响应ModifyUserUI修改按钮事件
	 */
	@SuppressWarnings("deprecation")
	public void ModifyUserInfoOrder() {

		userInfo.setNickName(Client.modify.getnickField().getText());// 设置用户昵称
		userInfo.setPassword(Client.modify.getpasswdField().getText());// 设置用户密码
		userInfo.setName(Client.modify.getnameField().getText());
		userInfo.setAge(Client.modify.getageField().getText());
		if (Client.modify.getsexBox().getSelectedItem() != null) {
			userInfo.setSex(Client.modify.getsexBox().getSelectedItem()
					.toString());// 设置用户性别
		}
		if (Client.modify.getstarBox().getSelectedItem() != null) {
			userInfo.setConstellation(Client.modify.getstarBox()
					.getSelectedItem().toString());// 设置用户星座
		}
		userInfo.setExplain(Client.modify.getexpArea().getText());

		Embody objMsg = new Embody();
		objMsg.setSourceID(constuser.getID());
		objMsg.setOrder(setting.Command.modifyusrinfo);
		objMsg.setObjMessage(userInfo);
		send(objMsg);
	}

	/**
	 * 响应FriendTree双击某好友节点事件
	 */
	@SuppressWarnings("unchecked")
	public void openChatUI(String nodename) {
		boolean newJFrame = true;
		if (nodename.equals("我的好友") || nodename.equals("在线好友")
				|| nodename.equals("离线好友")) {
			// 选择上叙节点不能弹出节点
		} else {
			if (Client.chatui == null) {
				Client.chatui = new ChatUI(constuser.getID(),
						Integer.parseInt(nodename));
				chatUIvector.add(Client.chatui);
			}
			for (int i = 0; i < chatUIvector.size(); i++) {
				ChatUI c = (ChatUI) chatUIvector.elementAt(i);// 遍历所有ID对 若找到相等的
				if (c.destid == Integer.parseInt(nodename)) {
					newJFrame = false;// 不NEW 新窗口
					Client.chatui.getwinFrame();
					Client.chatui.win.setVisible(true);
					Client.chatui.win.setTitle("与好友" + nodename + "对话中");
				}

			}
			if (newJFrame)// 若没找到 则new一个新窗口
			{
				chatui = new ChatUI(constuser.getID(),
						Integer.parseInt(nodename));// 是否有新的好友和你聊天 有则再开启一个窗口
				chatUIvector.add(chatui);
				chatui.getwinFrame();
				chatui.win.setVisible(true);
				chatui.win.setTitle("与好友" + nodename + "对话中");
			}
		}
	}

	/**
	 * 响应注册按钮事件
	 */
	@SuppressWarnings("deprecation")
	public void regUser() {
		userInfo = new User();
		userInfo.setNickName(Client.userRegUI.getnickField().getText());// 设置用户昵称
		userInfo.setPassword(Client.userRegUI.getpasswdField().getText().trim());// 设置用户密码
		userInfo.setIP(Client.userRegUI.getIP().getText());
		userInfo.setName(Client.userRegUI.getnameField().getText());
		userInfo.setAge(Client.userRegUI.getageField().getText());
		userInfo.setSex(Client.userRegUI.getsexBox().getSelectedItem()
				.toString());// 设置用户性别
		userInfo.setConstellation(Client.userRegUI.getstarBox()
				.getSelectedItem().toString());// 设置用户星座
		userInfo.setExplain(Client.userRegUI.getexpArea().getText());// 设置个人说明
		if (userInfo.getNickName().equals("")) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(null, "注册失败,昵称没有填写", "警告", 1);
			return;
		}
		if (userInfo.getNickName().length() > 10) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(null, "昵称不能超过10个字符", "警告", 1);
			return;
		}
		if (userInfo.getPassword().length() < 1) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(null, "注册失败,密码不能少于1位数", "警告", 1);
			return;
		}
		if (userInfo.getPassword().length() > 16) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(null, "密码不能超过16个字符", "警告", 1);
			return;
		}
		if (userInfo.getName().equals("")) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(null, "注册失败,姓名没有填写", "警告", 1);
			return;
		}
		if (userInfo.getName().length() > 8) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(null, "姓名不能超过8个字符", "警告", 1);
			return;
		}
		if (userInfo.getAge().equals("")) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(null, "注册失败,年龄没有填写", "警告", 1);
			return;
		}
		try {
			Integer.parseInt(userInfo.getAge());
		} catch (Exception e) {
			aplay.soundPlay("/music/system.wav");
			JOptionPane.showMessageDialog(null, "注册失败,年龄必须是数字", "警告", 1);
			return;
		}
		Embody objMsg = new Embody();
		objMsg.setOrder(setting.Command.register);
		objMsg.setObjMessage(userInfo);
		send(objMsg);
	}

	/**
	 * 得到当前时间
	 */
	public static String getTime() {
		Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
		Date date = calendar.getTime();// 得到当前时间
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	/**
	 * 程序入口main方法
	 */
	public static void main(String[] args) {
		try
	    {
	       org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
	      // UIManager.put("TabbedPane.tabAreaInsets" ,
	    	//	   new javax.swing.plaf.InsetsUIResource(3,0,2,20));
	    }
	    catch(Exception e)
	    {
	        //TODO exception
	    }
		UIManager.put("RootPane.setupButtonVisible", false);
		client = new Client();
		client.init();
	}
}