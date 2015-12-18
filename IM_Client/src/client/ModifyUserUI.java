/**
 * 个人设置界面
 */

package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class ModifyUserUI implements ActionListener {
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
	private JLabel nickLabel = null;

	/**
	 * 放置"用户密码"
	 */
	private JLabel passwdLabel = null;

	/**
	 * 放置"姓名"
	 */
	private JLabel nameLabel = null;

	/**
	 * 放置"性别"
	 */
	private JLabel sexLabel = null;

	/**
	 * 放置"年龄"
	 */
	private JLabel ageLabel = null;
	/**
	 * 放置"星座"
	 */
	private JLabel starLabel = null;

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
	 * 修改按钮
	 */
	private JButton alterBtn = null;

	/**
	 * 取消按钮
	 */
	private JButton cancelBtn = null;

	/**
	 * 显示号码
	 */
	public JLabel idLabel = null;

	/**
	 * 显示具体的号码
	 */
	public JLabel NoLabel = null;

	/**
	 * 此方法初始化winFrame
	 */
	public JFrame getwinFrame() {
		if (winFrame == null) {
			winFrame = new JFrame();
			winFrame.setSize(new Dimension(353, 335));
			winFrame.setTitle("修改信息");
			Toolkit toolkit = winFrame.getToolkit();
			Dimension screen = toolkit.getScreenSize();
			winFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(
					getClass().getResource("/picture/client.gif")));
			winFrame.setBounds(screen.width / 2 - 371 / 2,
					screen.height / 2 - 353 / 2, 415, 390);// 让窗体在屏幕正中央显示
			winFrame.setContentPane(getmainjCP());
			winFrame.setResizable(false);// 固定窗口大小
			winFrame.setVisible(true);
			alterBtn.addActionListener(this);
			cancelBtn.addActionListener(this);
			winFrame.setDefaultCloseOperation(1);
		}
		return winFrame;
	}

	/**
	 * 此方法初始化mainjCP
	 */
	private JPanel getmainjCP() {
		if (mainjCP == null) {
			idLabel = new JLabel(" 用户号");
			//idLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			idLabel.setBounds(new Rectangle(194, 46, 56, 24));
			NoLabel = new JLabel();
			NoLabel.setFont(new java.awt.Font("Dialog", Font.PLAIN, 26));
			NoLabel.setForeground(Color.black);
			//NoLabel.setBorder(new LineBorder(Color.black, 1, false));
			NoLabel.setBounds(new Rectangle(255, 46, 80, 24));
			starLabel = new JLabel();
			//starLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			starLabel.setBounds(new Rectangle(194, 127, 56, 24));
			starLabel.setText(" 星  座");
			ageLabel = new JLabel();
		//	ageLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			ageLabel.setBounds(new Rectangle(194, 85, 56, 24));
			ageLabel.setText(" 性  别");
			sexLabel = new JLabel();
			//sexLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			sexLabel.setBounds(new Rectangle(23, 127, 56, 24));
			sexLabel.setText(" 年  龄");
			nameLabel = new JLabel();
			//nameLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			nameLabel.setBounds(new Rectangle(23, 85, 56, 24));
			nameLabel.setText(" 姓  名");
			passwdLabel = new JLabel();
			//passwdLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			passwdLabel.setBounds(new Rectangle(23, 46, 56, 24));
			passwdLabel.setText(" 密  码");
			nickLabel = new JLabel();
			//nickLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			nickLabel.setBounds(new Rectangle(23, 13, 58, 24));
			nickLabel.setText(" 昵  称");
			mainjCP = new JPanel();
			mainjCP.setBackground(Color.white);
			mainjCP.setLayout(null);
			mainjCP.setSize(new Dimension(344, 303));
			mainjCP.add(nickLabel);
			mainjCP.add(passwdLabel);
			mainjCP.add(nameLabel);
			mainjCP.add(sexLabel);
			mainjCP.add(getnickField());
			mainjCP.add(getpasswdField());
			mainjCP.add(ageLabel);
			mainjCP.add(starLabel);
			mainjCP.add(idLabel);
			mainjCP.add(NoLabel);
			mainjCP.add(getsexBox());
			mainjCP.add(getstarBox());
			mainjCP.add(getnameField());
			mainjCP.add(getageField());
			mainjCP.add(getexpArea());
			mainjCP.add(getalterBtn(), null);
			mainjCP.add(getcancelBtn());
		}
		return mainjCP;
	}

	/**
	 * 此方法初始化nickField
	 */
	public JTextField getnickField() {
		if (nickField == null) {
			nickField = new JTextField();
			//nickField.setBorder(new LineBorder(Color.black, 1, false));
			nickField.setBounds(new Rectangle(85, 13, 105, 24));
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
			passwdField.setBounds(new Rectangle(85, 46, 105, 24));
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
			sexBox.setBounds(new Rectangle(258, 84, 80, 24));
		}
		return sexBox;
	}

	/**
	 * 此方法初始化starBox
	 */
	public JComboBox getstarBox() {
		if (starBox == null) {
			String[] star = { "射手座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
					"狮子座", "处女座", "天称座", "天蝎座", "魔羯座" };
			starBox = new JComboBox(star);
			starBox.setSelectedIndex(0);
			starBox.setBounds(new Rectangle(258, 127, 80, 24));
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
			nameField.setBounds(new Rectangle(86, 85, 105, 24));
		}
		return nameField;
	}

	/**
	 * 此方法初始化ageField
	 */
	public JTextField getageField() {
		if (ageField == null) {
			ageField = new JTextField();
		//	ageField.setBorder(new LineBorder(Color.black, 1, false));
			ageField.setBounds(new Rectangle(85, 127, 105, 24));
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
			//		TitledBorder.DEFAULT_JUSTIFICATION,
			//		TitledBorder.DEFAULT_POSITION, null, null));
			expArea.setText("");
			expArea.setBounds(new Rectangle(23, 180, 310, 80));
		}
		return expArea;
	}

	/**
	 * 此方法初始化alterBtn
	 */
	public JButton getalterBtn() {
		if (alterBtn == null) {
			alterBtn = new JButton();
			alterBtn.setFocusPainted(true);
			alterBtn.setBounds(new Rectangle(225, 272, 60, 25));
			alterBtn.setText("修改");
			alterBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		}
		return alterBtn;
	}

	/**
	 * 此方法初始化cancelBtn
	 */
	public JButton getcancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new JButton();
			cancelBtn.setFocusPainted(true);
			cancelBtn.setBounds(new Rectangle(81, 272, 60, 25));
			cancelBtn.setText("返回");
			cancelBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
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
			winFrame.dispose();
		}
		/**
		 * 按修改
		 */
		if (e.getSource() == alterBtn) {
			Client.client.ModifyUserInfoOrder();
		}
	}
}
