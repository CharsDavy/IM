/**
 * 服务器功能实现
 */

package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import setting.Embody;
import setting.User;

/**
 * 
 * @author
 */
class Serve {
	/**
	 * 定义ServerSocket
	 */
	private ServerSocket serversocket;

	/**
	 * 定义Socket 当有客户连接到服务器时创建一个新的Socket
	 */
	private Socket socket = new Socket();

	/**
	 * 是否有线程正在访问文件
	 */
	private boolean isVisitFile = true;

	/**
	 * 定义线程类
	 */
	private Work work;

	/**
	 * 聚集：用于保存客户所有信息
	 */
	// @SuppressWarnings("unchecked")
	private ArrayList arraylist = null;

	/**
	 * 服务器监控UI
	 */
	ServerUI serverUI;

	/**
	 * 定义Server类
	 */
	static Serve serve = null;

	/**
	 * 定义保存线程及用户信息对象类
	 */
	// @SuppressWarnings("unchecked")
	private Vector clientsfind = null;

	/**
	 * 获取服务器IP
	 */
	public static InetAddress getLocalHost() throws UnknownHostException {
		InetAddress IP = InetAddress.getLocalHost();
		return IP;
	}

	/**
	 * 启动服务器
	 */
	public void start() {
		try {
			serversocket = new ServerSocket(setting.Command.server_port);
			serverUI = new ServerUI();
			serverUI.getJFrame();
			serverUI.setShoweventArea(true);
			serverUI.setShowsearchInfoArea(false);
			serverUI.layout.first(serverUI.jPaneShowTextArea);
			serverUI.geteventArea().append(
					Serve.getTime() + " 服务器已开启" + "\n" + "服务器IP："
							+ (getLocalHost().getHostAddress()) + "\n");
			socket.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "此端口被占用,系统将退出"); // 出错，打印出错信息
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启动线程
	 */
	public void startThread() {
		while (true)// 死循环 一直监听有没有客户端和服务器建立连接
		{
			try {
				socket = serversocket.accept();
				serverUI.setShoweventArea(true);
				serverUI.setShowsearchInfoArea(false);
				serverUI.layout.first(serverUI.jPaneShowTextArea);
				serverUI.geteventArea().append(
						Serve.getTime() + " 客户端开始连接服务器：" + "\n");
				serverUI.geteventArea().selectAll();
				work = new Work(serve, socket);// 通过Server的对象 和 连接入的Socket
				// 对象构造Work
				work.start();// 启动线程；
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 注册新用户
	 */
	@SuppressWarnings("unchecked")
	public synchronized void addNewUser(User user) {
		try {
			if (!isVisitFile) {
				wait();
			}
			isVisitFile = false;
			if (!new File(setting.Command.userdatebase).exists()) {
				new File(setting.Command.userdatebase);// 若用户数据文件不在则创建用户数据文件
			}
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(setting.Command.userdatebase, false));
			// 创建对象输出流
			// 以后写进来的信息覆盖原来的信息
			if (user.getID() == 1000)// 仅在服务器接受第一个客户注册时实例化
			{
				arraylist = new ArrayList();
			}
			arraylist.add(user);// 将从客户传进来的user信息添加到 arraylist中
			out.writeObject(arraylist);// 将arrylist对象写入文件中
			out.flush();
			out.close();
			isVisitFile = true;
			notifyAll();
		} catch (InterruptedException Interr) {
			System.out.println("错误:" + Interr.getMessage());
		} catch (FileNotFoundException Filenot) {
			System.out.println("文件错误:" + Filenot.getMessage());
		} catch (IOException Ioe) {
			Ioe.printStackTrace();
		} catch (Exception e) {
			System.out.println("异常:" + e.getMessage());
		} // 捕获一切可能的异常
	}

	/**
	 * 读取用户数据文件到arraylist到内存中
	 */
	// @SuppressWarnings("unchecked")
	public synchronized void readUserDB() {
		try {
			if (!isVisitFile) {
				wait();
			}
			isVisitFile = false;
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					setting.Command.userdatebase));
			arraylist = new ArrayList();
			arraylist = (ArrayList) in.readObject();
			in.close();
			isVisitFile = true;
			notifyAll();
		} catch (InterruptedException Interr) {
			System.out.println("错误:" + Interr.getMessage());
		} catch (FileNotFoundException Filenot) {
			System.out.println("文件错误:" + Filenot.getMessage());
		} catch (IOException Ioe) {
			Ioe.printStackTrace();
		} catch (Exception e) {
			System.out.println("异常:" + e.getMessage());
		} // 捕获一切可能的异常
	}

	/**
	 * 根据用户ID在数据库中找到相应User对象
	 */
	// @SuppressWarnings("unchecked")
	public synchronized User readUserDBbyID(int userid) {
		User readuser = new User();
		boolean isExist = false;
		try {
			if (!isVisitFile) {
				wait();
			}
			isVisitFile = false;
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					setting.Command.userdatebase));
			arraylist = new ArrayList();
			arraylist = (ArrayList) in.readObject();
			Iterator iter = arraylist.iterator();
			while (iter.hasNext()) {
				readuser = (User) iter.next();
				if (readuser.getID() == userid) {
					isExist = true;
					break;// 从数据库中再次读取用户
				}
			}
			if (!isExist) {
				readuser = new User();
				readuser.setID(0);
			}
			in.close();
			isVisitFile = true;
			notifyAll();
		} catch (InterruptedException Interr) {
			System.out.println("错误:" + Interr.getMessage());
		} catch (FileNotFoundException Filenot) {
			System.out.println("文件错误:" + Filenot.getMessage());
		} catch (IOException Ioe) {
			System.out.println("读写错误:" + Ioe.getMessage());
		} catch (Exception e) {
			System.out.println("异常:" + e.getMessage());
		} // 捕获一切可能的异常
		return readuser;
	}

	/**
	 * 保存用户信息
	 */
	@SuppressWarnings("unchecked")
	public synchronized void saveDB(User user) {
		try {
			if (!isVisitFile) {
				wait();
			}
			isVisitFile = false;
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(setting.Command.userdatebase));
			User saveuser = new User();
			Iterator iter = serve.getArrayList().iterator();
			while (iter.hasNext()) {
				saveuser = (User) iter.next();
				if (user.getID() == saveuser.getID()) {
					iter.remove();
					break;
				}
			}
			arraylist.add(user);
			out.writeObject(arraylist);
			out.flush();
			out.close();
			isVisitFile = true;
			notifyAll();
		} catch (InterruptedException Interr) {
			System.out.println("错误:" + Interr.getMessage());
		} catch (FileNotFoundException Filenot) {
			System.out.println("文件错误:" + Filenot.getMessage());
		} catch (IOException Ioe) {
			System.out.println("读写异常:" + Ioe.getMessage());
		} catch (Exception e) {
			System.out.println("异常:" + e.getMessage());
		}
	}

	/**
	 * 删除用户信息 user为用户号码 friendid为将要被删除的好友的号
	 */
	// @SuppressWarnings("unchecked")
	public synchronized boolean delfriend(User user, int friendid) {
		boolean delsuccess = false;
		try {
			if (!isVisitFile) {
				wait();
			}
			isVisitFile = false;
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					setting.Command.userdatebase));
			arraylist = new ArrayList();
			arraylist = (ArrayList) in.readObject();
			Iterator iter = arraylist.iterator();
			User deluser = new User();
			while (iter.hasNext()) {
				deluser = (User) iter.next();
				if (user.getID() == deluser.getID()) {
					break;
				}
			}
			User userfriend = new User();
			for (int i = 0; i < deluser.getFriend().size(); i++) {
				userfriend = (User) deluser.getFriend().elementAt(i);// 遍历得到朋友的ID
				if (userfriend.getID() == friendid) {
					deluser.getFriend().removeElementAt(i);
					delsuccess = true;
					break;
				}
			}
			in.close();
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(setting.Command.userdatebase));
			out.writeObject(arraylist);
			out.close();
			isVisitFile = true;
			System.out.println("读取数据");
			notifyAll();
		} catch (InterruptedException Interr) {
			System.out.println("错误:" + Interr.getMessage());
		} catch (FileNotFoundException Filenot) {
			System.out.println("文件错误:" + Filenot.getMessage());
		} catch (IOException Ioe) {
			System.out.println("读写错误:" + Ioe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return delsuccess;
	}

	/**
	 * 删除用户
	 * 
	 * @param user
	 * @param id
	 * @return
	 */
	public synchronized boolean delUser(int id) {
		boolean delsuccess = false;
		try {
			if (!isVisitFile) {
				wait();
			}
			isVisitFile = false;
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					setting.Command.userdatebase));
			arraylist = new ArrayList();
			arraylist = (ArrayList) in.readObject();
			Iterator iter = arraylist.iterator();
			User deluser = new User();
			while (iter.hasNext()) {
				deluser = (User) iter.next();
				if (id == deluser.getID()) {
					arraylist.remove(deluser);
					break;
				}
			}
			in.close();
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(setting.Command.userdatebase));
			out.writeObject(arraylist);
			out.close();
			isVisitFile = true;
			notifyAll();
		} catch (InterruptedException Interr) {
			System.out.println("错误:" + Interr.getMessage());
		} catch (FileNotFoundException Filenot) {
			System.out.println("文件错误:" + Filenot.getMessage());
		} catch (IOException Ioe) {
			System.out.println("读写错误:" + Ioe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return delsuccess;
	}

	public synchronized boolean setOfflineUser(int id) {
		boolean delsuccess = false;
		try {
			if (!isVisitFile) {
				wait();
			}
			isVisitFile = false;
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					setting.Command.userdatebase));
			arraylist = new ArrayList();
			arraylist = (ArrayList) in.readObject();
			Iterator iter = arraylist.iterator();
			User deluser = new User();
			while (iter.hasNext()) {
				deluser = (User) iter.next();
				if (id == deluser.getID()) {
					int flag=arraylist.indexOf(deluser);
					deluser.setOffline();
					arraylist.set(flag, deluser);
					break;
				}
			}
			in.close();
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(setting.Command.userdatebase));
			out.writeObject(arraylist);
			out.close();
			isVisitFile = true;
			notifyAll();
		} catch (InterruptedException Interr) {
			System.out.println("错误:" + Interr.getMessage());
		} catch (FileNotFoundException Filenot) {
			System.out.println("文件错误:" + Filenot.getMessage());
		} catch (IOException Ioe) {
			System.out.println("读写错误:" + Ioe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return delsuccess;
	}
	
	/**
	 * 服务器入口
	 */
	public static void main(String[] args) {
		try
	    {
	        org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
	    }
	    catch(Exception e)
	    {
	        //TODO exception
	    }
		UIManager.put("RootPane.setupButtonVisible", false);
		serve = new Serve();
		serve.start();// 启动服务器
		serve.startThread();// 等待客户连接 创建线程
	}

	/**
	 * 返回以生成的ArrayList对象
	 */
	// @SuppressWarnings("unchecked")
	public ArrayList getArrayList() {
		return this.arraylist;
	}

	/**
	 * 返回以生成的ServerUI对象
	 */
	public ServerUI getServerUI() {
		return this.serverUI;
	}

	/**
	 * 从数据库读出数据，修改用户信息并保存数据库文件
	 */
	@SuppressWarnings("unchecked")
	public synchronized void modifyUserInfo(User user) {
		try {
			if (!isVisitFile) {
				wait();
			}
			isVisitFile = false;
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					setting.Command.userdatebase));
			arraylist = new ArrayList();
			arraylist = (ArrayList) in.readObject();
			Iterator iter = arraylist.iterator();
			while (iter.hasNext()) {
				User userFlag = (User) iter.next();
				if (user.getID() == userFlag.getID()) {
					//iter.remove();// 在迭代器上的修改会导致arraylist的修改
					int flag=arraylist.indexOf(userFlag);
					arraylist.set(flag, user);
				}
			}
			//arraylist.add(user);
			in.close();
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(setting.Command.userdatebase));
			out.writeObject(arraylist);
			out.flush();
			out.close();
			notifyAll();
			isVisitFile = true;
		} catch (InterruptedException Interr) {
			System.out.println("错误:" + Interr.getMessage());
		} catch (FileNotFoundException Filenot) {
			System.out.println("文件错误:" + Filenot.getMessage());
		} catch (IOException Ioe) {
			Ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户是否在线
	 */
	// @SuppressWarnings("unchecked")
	public boolean isOnline(User userInfo) {
		Iterator iter = serve.getArrayList().iterator();
		User user = new User();
		boolean isOnline = true;
		while (iter.hasNext()) {
			user = (User) iter.next();// user是在数据库中找到的对象
			if (userInfo.getID() == user.getID()) {
				if (user.getStatus() == 0) {
					isOnline = true;
				}
				if (user.getStatus() == 1) {
					isOnline = false;
				}
			}
		}
		isVisitFile = true;
		return isOnline;
	}

	/**
	 * 保存用户登录时的User和Work对象
	 */
	@SuppressWarnings("unchecked")
	public void saveWorkAndUser(User user) {
		UserInfoAndThread userself = new UserInfoAndThread();
		userself.setWorking(work);
		userself.setUser(user);
		if (clientsfind == null) {
			clientsfind = new Vector();
		}
		clientsfind.add(userself);
	}

	/**
	 * 根据指定ID寻找线程并向网络发送信息
	 */
	public synchronized void sendpersonal(int id, Embody objMsg)// 根据用户ID发送
	{
		try {
			if (!isVisitFile) {
				wait();
			}
			isVisitFile = false;
			for (int i = 0; i < clientsfind.size(); i++) {
				UserInfoAndThread client = new UserInfoAndThread();
				// 分别得到每个客户端的连接
				client = (UserInfoAndThread) clientsfind.elementAt(i);
				if (client.getUser().getID() == id) {
					client.getWorking().send(objMsg);
					// 由目标ID得到目标线程
				}
			}
		} catch (InterruptedException Interr) {
			System.out.println("错误:" + Interr.getMessage());
		} catch (Exception e) {
			System.out.println("异常:" + e.getMessage());
		}
		isVisitFile = true;
		notifyAll();
	}

	/**
	 * 得到当前时间
	 */
	public static String getTime() {
		Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
		Date date = calendar.getTime();// 得到当前时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}
}
