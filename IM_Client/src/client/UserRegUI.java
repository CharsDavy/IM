/**
 * 用户注册界面
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

/**
 * 
 * @author
 */
public class UserRegUI implements ActionListener {
	/**
	 * 窗体
	 */
	private JFrame winFrame = null;

	/**
	 * 添加生成后的所有组件
	 */
	private JPanel mainjCP = null;

	/**
	 * 放置"用户昵称"
	 */
	private JLabel nick = null;

	/**
	 * 放置"用户密码"
	 */
	private JLabel passwd = null;

	/**
	 * 放置"姓名"
	 */
	private JLabel name = null;

	/**
	 * 放置"性别"
	 */
	private JLabel sex = null;

	/**
	 * 放置"年龄"
	 */
	private JLabel age = null;
	/**
	 * 放置"星座"
	 */
	private JLabel star = null;

	/**
	 * 用户昵称输入框
	 */
	private JTextField nickField = null;

	/**
	 * 用户密码输入框
	 */
	private JPasswordField passwdField = null;

	/**
	 * 用户性别选择
	 */
	private JComboBox sexBox = null;
	/**
	 * 用户星座选择
	 */
	private JComboBox starBox = null;

	/**
	 * 用户姓名输入框
	 */
	private JTextField nameField = null;

	/**
	 * 用户年龄输入框
	 */
	private JTextField ageField = null;

	/**
	 * 个人说明输入文本域
	 */
	private JTextArea expArea = null;

	/**
	 * 注册按钮
	 */
	private JButton registBtn = null;

	/**
	 * 取消按钮
	 */
	private JButton cancelBtn = null;

	/**
	 * 注册IP
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
			winFrame = new JFrame();
			winFrame.setSize(new Dimension(353, 335));
			winFrame.setTitle("用户注册");
			Toolkit toolkit = winFrame.getToolkit();
			Dimension screen = toolkit.getScreenSize();
			winFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(
					getClass().getResource("/picture/client.gif")));
			winFrame.setBounds(screen.width / 2 - 371 / 2,
					screen.height / 2 - 353 / 2, 415, 385);// 让窗体在屏幕正中央显示
			winFrame.setContentPane(getmainjCP());
			winFrame.setResizable(false);// 固定窗口大小
			//winFrame.setBackground(Color.gray);
			winFrame.setVisible(true);
			registBtn.addActionListener(this);
			cancelBtn.addActionListener(this);
			winFrame.setDefaultCloseOperation(1);
			winFrame.addWindowListener(new WindowAdapter() {// 添加窗口时间监听
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		}
		return winFrame;
	}

	/**
	 * 此方法初始化mainjCP
	 */
	private JPanel getmainjCP() {
		if (mainjCP == null) {
			star = new JLabel();
			//star.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			star.setBounds(new Rectangle(193, 119, 56, 26));
			star.setText(" 星   座");
			age = new JLabel();
			//age.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			age.setBounds(new Rectangle(193, 80, 56, 26));
			age.setText(" 性   别");
			sex = new JLabel();
			//sex.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			sex.setBounds(new Rectangle(23, 119, 56, 24));
			sex.setText(" 年   龄");
			name = new JLabel();
			//name.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			name.setBounds(new Rectangle(23, 80, 56, 24));
			name.setText(" 姓   名");
			passwd = new JLabel();
			//passwd.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			passwd.setBounds(new Rectangle(23, 46, 56, 24));
			passwd.setText(" 密  码");
			nick = new JLabel();
			//nick.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			nick.setBounds(new Rectangle(23, 13, 56, 24));
			nick.setText(" 昵  称");
			mainjCP = new JPanel();
			mainjCP.setLayout(null);
			mainjCP.setSize(new Dimension(344, 303));
			mainjCP.add(nick);
			mainjCP.add(passwd);
			mainjCP.add(name);
			mainjCP.add(sex);
			mainjCP.add(getnickField());
			mainjCP.add(getpasswdField());
			mainjCP.add(age);
			mainjCP.add(star);
			mainjCP.add(getsexBox());
			mainjCP.add(getstarBox());
			mainjCP.add(getnameField());
			mainjCP.add(getageField());
			mainjCP.add(getexpArea());
			mainjCP.add(getregistBtn());
			mainjCP.add(getcancelBtn());
			mainjCP.add(getIP());
		}
		return mainjCP;
	}

	/**
	 * 设置注册IP
	 */
	public JLabel getIP() {
		if (IP == null)
			IP = new JLabel();
		IP.setBounds(136, 275, 78, 18);
		IP.setVisible(false);
		try {
			IP.setText(getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return IP;
	}

	/**
	 * 此方法初始化 nickField
	 */
	public JTextField getnickField() {
		if (nickField == null) {
			nickField = new JTextField();
			//nickField.setBorder(new LineBorder(Color.black, 1, false));
			nickField.setBounds(new Rectangle(87, 14, 149, 24));
		}
		return nickField;
	}

	/**
	 * 此方法初始化passwdField
	 */
	public JPasswordField getpasswdField() {
		if (passwdField == null) {
			passwdField = new JPasswordField();
			//passwdField.setBorder(new LineBorder(Color.black, 1, false));
			passwdField.setBounds(new Rectangle(85, 47, 151, 24));
		}
		return passwdField;
	}

	/**
	 * 此方法初始化sexBox
	 */
	public JComboBox getsexBox() {
		if (sexBox == null) {
			String[] sex = { "男", "女" };
			sexBox = new JComboBox(sex);
			sexBox.setSelectedIndex(0);
			sexBox.setBounds(new Rectangle(255, 81, 78, 24));
		}
		return sexBox;
	}

	/**
	 * 此方法初始化starBox
	 */
	public JComboBox getstarBox() {
		if (starBox == null) {
			String[] star = { "双鱼座", "水瓶座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座",
					"处女座", "天称座", "天蝎座", "射手座", "魔羯座" };
			starBox = new JComboBox(star);
			starBox.setSelectedIndex(0);
			starBox.setBounds(new Rectangle(255, 120, 78, 24));
		}
		return starBox;
	}

	/**
	 * 此方法初始化nameField
	 */
	public JTextField getnameField() {
		if (nameField == null) {
			nameField = new JTextField();
			//nameField.setBorder(new LineBorder(Color.black, 1, false));
			nameField.setBounds(new Rectangle(86, 81, 78, 24));
		}
		return nameField;
	}

	/**
	 * 此方法初始化ageField
	 */
	public JTextField getageField() {
		if (ageField == null) {
			ageField = new JTextField();
			//ageField.setBorder(new LineBorder(Color.black, 1, false));
			ageField.setBounds(new Rectangle(85, 120, 79, 24));
		}
		return ageField;
	}

	/**
	 * 此方法初始化expArea
	 */
	public JTextArea getexpArea() {
		if (expArea == null) {
			expArea = new JTextArea();
			expArea.setLineWrap(true);
			expArea.setRows(3);
		//	expArea.setBorder(new TitledBorder(null, "个人说明",
		//			TitledBorder.DEFAULT_JUSTIFICATION,
		//			TitledBorder.DEFAULT_POSITION, null, null));
			expArea.setText("个性签名");
			expArea.setBounds(new Rectangle(23, 166, 310, 100));
		}
		return expArea;
	}

	/**
	 * 此方法初始化registBtn
	 */
	public JButton getregistBtn() {
		if (registBtn == null) {
			registBtn = new JButton();
			registBtn.setBounds(new Rectangle(220, 272, 60, 25));
			registBtn.setText("注册");
			registBtn.setUI(new  BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		}
		return registBtn;
	}

	/**
	 * 此方法初始化cancelBtn
	 */
	public JButton getcancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new JButton();
			cancelBtn.setBounds(new Rectangle(80, 272, 60, 25));
			cancelBtn.setText("返回");
			cancelBtn.setUI(new  BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		}
		return cancelBtn;
	}

	/**
	 * 实现ActionListener所需实现的方法
	 */
	public void actionPerformed(ActionEvent e) {
		/**
		 * 按返回
		 */
		if (e.getSource() == cancelBtn) {
			this.winFrame.setVisible(false);
			Client.loginui.getwinFrame().setVisible(true);
		}
		/**
		 * 按注册按钮
		 */
		if (e.getSource() == registBtn) {
			Client.client.regUser();
		}
	}
}