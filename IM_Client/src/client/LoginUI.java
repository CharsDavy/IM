/**
 * 登录界面
 */

package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

/**
 * 
 * @author
 */
public class LoginUI implements ActionListener {
	/**
	 * 窗体
	 */
	private JFrame winFrame = null;

	/**
	 * 放置其他所有组件
	 */
	private JPanel mainjCP = null;

	/**
	 * 放置字符串号码
	 */
	private JLabel toID = null;

	/**
	 * 号码输入框
	 */
	private JTextField ID = null;

	/**
	 * 放置字符串密码
	 */
	private JLabel topass = null;

	/**
	 * 密码输入框
	 */
	public JPasswordField PasswordField = null;

	/**
	 * 登录按钮
	 */
	private JButton login = null;

	/**
	 * 取消按钮
	 */
	private JButton quit = null;

	/**
	 * 注册按钮
	 */
	private JButton regedit = null;

	/**
	 * 登录IP
	 */
	private JLabel IP = null;

	/**
	 * 获取本地IP
	 */
	public static InetAddress getLocalHost() throws UnknownHostException {
		InetAddress LocalIP = InetAddress.getLocalHost();
		return LocalIP;
	}

	/**
	 * 此方法初始化winFrame
	 */
	public JFrame getwinFrame() {
		if (winFrame == null) {
			winFrame = new JFrame();// 实例化jFrame
			winFrame.setSize(265, 140);// 封装jFrame对象的宽度和高度
			winFrame.setTitle("登录");// 设置窗体标题
			Toolkit toolkit = winFrame.getToolkit();// Tookit是AWT所有实际实现的抽象超类
			Dimension screen = toolkit.getScreenSize();// 得到屏幕的大小
			winFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(
					getClass().getResource("/picture/client.gif")));// 设置窗体图标
			winFrame.setBounds(screen.width / 2 - 265 / 2,
					screen.height / 2 - 174 / 2, 325, 225);// //让窗体在屏幕正中央显示
			winFrame.setContentPane(getmainjCP());// 把窗体的内容面板设置为mainjCP
			winFrame.setResizable(false);// //固定窗口大小
			winFrame.setVisible(true);// 设置窗体为可见
			regedit.addActionListener(this);// 为添加行为事件监听
			login.addActionListener(this);// 为添加行为事件监听
			winFrame.addWindowListener(new WindowAdapter() {// 添加窗口时间监听
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			winFrame.setDefaultCloseOperation(1);
			winFrame.getRootPane().setDefaultButton(login);
		}
		return winFrame;// 返回jFrame,此时上面的以添加好
	}

	/**
	 * 此方法初始化mainjCP
	 */
	private JPanel getmainjCP() {
		if (mainjCP == null) {
			topass = new JLabel();
			topass.setBounds(new Rectangle(36, 67, 80, 25));// Rectangle指定了坐标空间中的一个区域
			topass.setText("用户密码：");
			toID = new JLabel();
			toID.setBounds(new Rectangle(36, 28, 80, 25));
			toID.setText("用户ID号：");
			mainjCP = new JPanel();
			mainjCP.setLayout(null);
			mainjCP.add(toID);
			mainjCP.add(getIDTextField());
			mainjCP.add(topass);
			mainjCP.add(getPasswordField());
			mainjCP.add(getloginBtn());
			mainjCP.add(getregeditBtn());
			mainjCP.add(getIP());
		}
		return mainjCP;
	}

	/**
	 * 设置登录IP
	 */
	public JLabel getIP() {
		if (IP == null) {
			IP = new JLabel();
			IP.setBounds(134, 150, 73, 18);
			try {
				IP.setText(getLocalHost().getHostAddress());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			IP.setVisible(false);
		}
		return IP;
	}

	/**
	 * 此方法初始化 ID
	 */
	public JTextField getIDTextField() {
		if (ID == null) {
			ID = new JTextField();
			//ID.setBorder(new LineBorder(Color.black, 1, false));
			ID.setBounds(new Rectangle(112, 30, 130, 21));
		}
		return ID;
	}

	/**
	 * 此方法初始化PasswordField
	 */
	public JPasswordField getPasswordField() {
		if (PasswordField == null) {
			PasswordField = new JPasswordField();
			//PasswordField.setBorder(new LineBorder(Color.black, 1, false));
			PasswordField.setBounds(new Rectangle(112, 69, 130, 21));
		}
		return PasswordField;
	}

	/**
	 * 此方法初始化loginButton
	 */
	private JButton getloginBtn() {
		if (login == null) {
			login = new JButton();
			login.setFocusPainted(false);
			login.setBounds(new Rectangle(40, 110, 60, 24));
			login.setText("登录");
			login.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
		}
		return login;
	}

	/**
	 * 此方法初始化regeditButton
	 */
	public JButton getregeditBtn() {
		if (regedit == null) {
			regedit = new JButton();
			regedit.setFocusPainted(false);
			regedit.setBounds(new Rectangle(160, 110, 60, 24));
			regedit.setText("注册");
			regedit.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue));
		}
		return regedit;
	}

	/**
	 * 实现actionListener接口所需实现的方法
	 */
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {// regeditButton,loginButton,quitButton事件处理
		/**
		 * 按注册按钮后弹出用户注册界面
		 */
		if (e.getSource() == regedit) {
			Client.userRegUI = new UserRegUI();
			Client.userRegUI.getwinFrame();
		}
		/**
		 * 按登录按钮后,执行Client的login()方法
		 */
		if (e.getSource() == login) {
			Client.client.login(this.getIDTextField().getText(), this
					.getPasswordField().getText(), this.IP.getText());
		}
		/**
		 * 按取消按钮后，隐藏用户登录界面
		 */
		if (e.getSource() == quit) {
			winFrame.setVisible(false);
		}
	}
}
