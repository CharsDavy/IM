/**
 * 服务器界面
 */
package server;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import setting.User;

/**
 * 
 * @author
 */
public class ServerUI implements ActionListener {
	/**
	 * 文本域用于Server、Work类写入事件
	 */
	private JTextArea eventArea;

	/**
	 * 文本域用于管理员查询用户信息
	 */
	private JTextArea searchInfoArea;

	/**
	 * "服务日志"按钮
	 */
	private JButton serve_log_jBtn;

	/**
	 * "查询"按钮
	 */
	private JButton search_jBtn;

	/**
	 * "清空日志"按钮
	 */
	private JButton clear_log_jBtn;

	/**
	 * "关闭服务器"按钮
	 */
	private JButton close_server_jBtn;

	/**
	 * "删除用户"按钮
	 */
	private JButton del_jBtn;

	/**
	 * "用户下线"按钮
	 */
	private JButton offLine_jBtn;

	/**
	 * 填写号码
	 */
	private JTextField no_editField;

	/**
	 * "用户号码"
	 */
	private JLabel userID;

	/**
	 * 滚动条
	 */
	JScrollPane displ_jSP;

	/**
	 * 窗体
	 */
	private JFrame mainWin;

	/**
	 * 面板，CardLayout，放置eventArea，searchInfoArea
	 */
	JPanel jPaneShowTextArea;

	/**
	 * 卡式布局
	 */
	CardLayout layout;

	/**
	 * 放置按钮及文本框
	 */
	private JPanel log;

	/**
	 * 显示eventArea
	 */
	private boolean showeventArea = true;

	/**
	 * 显示searchInfoArea
	 */
	private boolean showsearchInfoArea = true;

	/**
	 * 生成窗体
	 */
	public JFrame getJFrame() {
		mainWin = new JFrame("监控台");
		mainWin.setSize(new Dimension(345, 360));
		mainWin.setResizable(false);
		Toolkit toolkit = mainWin.getToolkit();
		Dimension screen = toolkit.getScreenSize();
		mainWin.setBounds(screen.width / 2 - 400 / 2,
				screen.height / 2 - 500 / 2, 435, 350);// //让窗体在屏幕正中央显示
		mainWin.getContentPane().setLayout(null);
		mainWin.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/picture/server.gif")));
		mainWin.getContentPane().add(this.cardLayout());
		mainWin.getContentPane().add(this.getJPanel());
		mainWin.setVisible(true);
		mainWin.setDefaultCloseOperation(1);
		mainWin.addWindowListener(new WindowAdapter() {// 添加窗口时间监听
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		return mainWin;
	}

	/**
	 * 添加按钮
	 */
	public JPanel getJPanel() {
		log = new JPanel();
		log.setLayout(null);
		log.setBounds(new Rectangle(0, 201, 384, 74));
		serve_log_jBtn = new JButton("日志");
		serve_log_jBtn.setBounds(new Rectangle(6, 6, 80, 26));
		clear_log_jBtn = new JButton("清空");
		clear_log_jBtn.setBounds(new Rectangle(6, 40, 80, 26));
		userID = new JLabel("用户号码:");
		userID.setFont(new Font("宋体", Font.BOLD, 12));
		userID.setBounds(new Rectangle(210, 9, 80, 18));
		no_editField = new JTextField();
		//no_editField.setBorder(new LineBorder(Color.black, 1, false));
		no_editField.setBounds(new Rectangle(291, 9, 80, 24));
		search_jBtn = new JButton("查询");
		search_jBtn.setBounds(new Rectangle(101, 6, 80, 26));
		close_server_jBtn = new JButton("关闭");
		close_server_jBtn.setBounds(new Rectangle(101, 40, 80, 26));
		del_jBtn = new JButton("删除");
		del_jBtn.setBounds(new Rectangle(196, 40, 80, 26));
		offLine_jBtn = new JButton("下线");
		offLine_jBtn.setBounds(new Rectangle(291, 40, 80, 26));
		log.add(serve_log_jBtn);
		log.add(search_jBtn);
		log.add(clear_log_jBtn);
		log.add(close_server_jBtn);
		log.add(del_jBtn);
		log.add(offLine_jBtn);
		log.add(userID);
		log.add(no_editField);
		serve_log_jBtn.addActionListener(this);
		search_jBtn.addActionListener(this);
		clear_log_jBtn.addActionListener(this);
		close_server_jBtn.addActionListener(this);
		del_jBtn.addActionListener(this);
		offLine_jBtn.addActionListener(this);
		return log;
	}

	/**
	 * 返回 displ_jSP
	 */
	public JScrollPane cardLayout() {
		jPaneShowTextArea = new JPanel();
		searchInfoArea = new JTextArea();
		searchInfoArea.setLineWrap(true);
		searchInfoArea.setFont(new java.awt.Font("Dialog", Font.PLAIN, 14));
		searchInfoArea.setForeground(Color.black);
		searchInfoArea.setEditable(false);
		eventArea = new JTextArea();
		eventArea.setFont(new java.awt.Font("Dialog", Font.PLAIN, 14));
		eventArea.setForeground(Color.black);
		eventArea.setLineWrap(true);
		eventArea.setEditable(false);
		displ_jSP = new JScrollPane();
		displ_jSP.setBounds(new Rectangle(1, 1, 383, 200));
		displ_jSP
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		displ_jSP.getViewport().add(jPaneShowTextArea);// 将卡片布局加入滚动面板
		layout = new CardLayout();
		jPaneShowTextArea.setLayout(layout);
		jPaneShowTextArea.add(eventArea, "eventArea");
		jPaneShowTextArea.add(searchInfoArea, "searchInfoArea");
		layout.show(jPaneShowTextArea, "eventArea");
		return displ_jSP;
	}

	/**
	 * 行为事件处理
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == serve_log_jBtn)// 服务日志按钮功能实现
		{
			displayLog();
		}
		if (e.getSource() == search_jBtn)// 查询
		{
			displaySearch();
		}
		if (e.getSource() == clear_log_jBtn)// 清空日志
		{
			clearLog();
		}
		if (e.getSource() == close_server_jBtn)// 退出服务器
		{
			System.exit(0);
		}
		if (e.getSource() == offLine_jBtn)// 用户下线
		{
			setUserOffline();
		}
		if (e.getSource() == del_jBtn)// 删除用户
		{
			deleteUser();
		}
	}

	/**
	 * 显示服务器日志
	 */
	public void displayLog() {
		if (showeventArea) {
			layout.last(jPaneShowTextArea);
			showeventArea = false;
			searchInfoArea.select(searchInfoArea.getSelectionStart(),
					searchInfoArea.getSelectionEnd() - 1);
			showsearchInfoArea = true;

		} else {
			layout.first(jPaneShowTextArea);
			showsearchInfoArea = false;
			eventArea.selectAll();
			showeventArea = true;
		}
	}

	/**
	 * 显示查询结果
	 */
	public void displaySearch() {
		if (no_editField.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "号码不能为空");
			return;
		}
		String useridStr = no_editField.getText();
		User userInfo = new User();
		try {
			userInfo = Serve.serve.readUserDBbyID(Integer.parseInt(useridStr));
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null, "只能输入数字");
			return;
		}
		if (userInfo.getID() == 0) {
			JOptionPane.showMessageDialog(null, "没有这个号码");
		} else {
			if (userInfo.getID() == Integer.parseInt(useridStr)) {
				searchInfoArea.append(userInfo.getNickName() + "("
						+ userInfo.getID() + ")" + "的个人信息：" + "\n");
				searchInfoArea.append("*********************************"
						+ "\n");
				searchInfoArea.append("帐号：" + userInfo.getID() + "\n");
				searchInfoArea.append("昵称：" + userInfo.getNickName() + "\n");
				searchInfoArea.append("姓名：" + userInfo.getName() + "\n");
				searchInfoArea.append("密码：" + userInfo.getPassword() + "\n");
				if (userInfo.isOnline()) {
					searchInfoArea.append("状态：" + "在线" + "\n");
				} else {
					searchInfoArea.append("状态：" + "离线" + "\n");
				}			
				searchInfoArea.append("年龄：" + userInfo.getAge() + "\n");
				searchInfoArea.append("性别：" + userInfo.getSex() + "\n");
				searchInfoArea.append("星座：" + userInfo.getConstellation()
						+ "\n");
				searchInfoArea.append("个人说明：" + userInfo.getExplain() + "\n");
				searchInfoArea.append("当前IP：" + userInfo.getIP() + "\n");
				searchInfoArea.append("好友号码：");
				if (userInfo.getFriend().size() > 0) {
					for (int i = 0; i < userInfo.getFriend().size(); i++) {
						searchInfoArea.append(((User) userInfo.getFriend()
								.elementAt(i)).getID() + " ");
					}
				} else {
					searchInfoArea.append("该用户目前没有好友");
				}
				searchInfoArea.append("\n"
						+ "*********************************" + "\n");
				searchInfoArea.selectAll();
				showsearchInfoArea = true;
				showeventArea = false;
				layout.last(jPaneShowTextArea);
			}
		}
	}

	/**
	 * 清空服务器日志、查询结果
	 */
	public void clearLog() {
		if (this.showeventArea) {
			eventArea.setText("");
		}
		if (this.showsearchInfoArea) {
			searchInfoArea.setText("");
		}
	}

	/**
	 * 强制用户下线
	 */
	public void setUserOffline() {
		if (no_editField.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "号码不能为空");
			return;
		}
		String useridStr = no_editField.getText();
		User userInfo = new User();
		try {
			userInfo = Serve.serve.readUserDBbyID(Integer.parseInt(useridStr));
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null, "只能输入数字");
			return;
		}
		if (userInfo.getID() == 0) {
			JOptionPane.showMessageDialog(null, "没有这个号码");
		} else {
			if (userInfo.getID() == Integer.parseInt(useridStr)) {
				if (userInfo.isOnline()) {
					//userInfo.setOffline();
					Serve.serve.setOfflineUser(userInfo.getID());
					JOptionPane.showMessageDialog(null, userInfo.getNickName()
							+ "(" + userInfo.getID() + ")" + " 已下线");
				} else {
					JOptionPane.showMessageDialog(null, userInfo.getNickName()
							+ "(" + userInfo.getID() + ")" + " 不在线");
				}
			}
		}
	}

	/**
	 * 强制删除用户
	 */
	public void deleteUser() {
		if (no_editField.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "号码不能为空");
			return;
		}
		String useridStr = no_editField.getText();
		User userInfo = new User();
		try {
			userInfo = Serve.serve.readUserDBbyID(Integer.parseInt(useridStr));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "只能输入数字");
			return;
		}
		if (userInfo.getID() == 0) {
			JOptionPane.showMessageDialog(null, "没有这个号码");
		} else {
			if (userInfo.getID() == Integer.parseInt(useridStr)) {
				Serve.serve.delUser(userInfo.getID());
				JOptionPane.showMessageDialog(null, userInfo.getNickName()
						+ "(" + userInfo.getID() + ")" +" 成功删除");
			}
		}
	}

	/**
	 * 获取eventArea，用于设置服务日志
	 */
	public JTextArea geteventArea() {
		return eventArea;
	}

	/**
	 * 设置eventArea是否显示
	 */
	public void setShoweventArea(boolean showeventArea) {
		this.showeventArea = showeventArea;
	}

	/**
	 * 设置searchInfoArea是否显示
	 */
	public void setShowsearchInfoArea(boolean showsearchInfoArea) {
		this.showsearchInfoArea = showsearchInfoArea;
	}
}
