/**
 * 客户端主操作界面
 */

package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import com.sun.awt.AWTUtilities;

/**
 * 
 * @author
 */
public class ClientMainUI implements ActionListener {
	/**
	 * 窗体
	 */
	private JFrame winFrame = null;

	/**
	 * 添加、addBtn、setBtn、 noteBabo、jLabel_ui、jPanel_add
	 */
	private JPanel mainjCP = null;// 添加所有组件
	/**
	 * 系统提示喇叭
	 */
	private JLabel noteBabo = null;

	/**
	 * 窗体界面
	 */
	private JLabel jLabel_ui = null;

	/**
	 * 用户头像
	 */
	private JLabel headpic = null;

	/**
	 * 添加好友按钮
	 */
	private JButton addBtn = null;

	/**
	 * 个人设置按钮
	 */
	private JButton setBtn = null;

	/**
	 * 添加JTree
	 */
	private JPanel jPanel_add = null;

	/**
	 * 此方法初始化窗体
	 */
	public JFrame getwinFrame() {
		if (winFrame == null) {
			winFrame = new JFrame();
			winFrame.setSize(new Dimension(227, 480));
			winFrame.setResizable(false);
			winFrame.setTitle("MainWindow");			
			winFrame.setContentPane(this.getmainjCP());
			winFrame.setFont(new Font("宋体", Font.PLAIN, 12));
			winFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(
					getClass().getResource("/picture/client.gif")));
			Toolkit toolkit = winFrame.getToolkit();
			Dimension screen = toolkit.getScreenSize();
			winFrame.setBounds(screen.width - 300, 10, 282, 700);// //让窗体在屏幕右侧央显示
			winFrame.setVisible(true);
			winFrame.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent e) {
					Client.client.logout();// 向服务器发出自己离线的信令
				}
			});
			addBtn.addActionListener(this);
			setBtn.addActionListener(this);
			winFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		return winFrame;
	}

	/**
	 * 设置好友列表
	 */
	public void setFriend(JTree tree)// 传入一个节点
	{
		if (jPanel_add == null) {
			jPanel_add = new JPanel();
		}
		jPanel_add.setLayout(new GridLayout(1, 1, 0, 0));
		jPanel_add.setBounds(new Rectangle(1, 66, 226, 500));
		JScrollPane js = new JScrollPane();
		js.getViewport().add(tree);
		jPanel_add.add(js);
	}

	/**
	 * 此方法初始化添加组件后的mainjCP
	 */
	public JPanel getmainjCP() {
		if (mainjCP == null) {
			mainjCP = new JPanel();
			jLabel_ui = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit()
					.getImage(getClass().getResource("/picture/online.png"))));
			jLabel_ui.setBounds(new Rectangle(8, 8, 40, 40));
			mainjCP.add(getheadpic());
			noteBabo = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit()
					.getImage(getClass().getResource("/picture/msg.jpg"))));
			mainjCP.add(noteBabo);
			noteBabo.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					Client.client.acceptOrder();
				}
			});
			noteBabo.setBounds(new Rectangle(103, 578, 25, 30));
			noteBabo.setFont(new Font("黑体", Font.PLAIN, 15));
			mainjCP.setBackground(Color.white);

			mainjCP.setLayout(null);
			mainjCP.add(getaddBtn());
			mainjCP.add(getaddJPanel());
			mainjCP.add(noteBabo);
			mainjCP.add(jLabel_ui);
			mainjCP.add(getsetBtn());
		}
		return mainjCP;
	}

	/**
	 * 返回新增好友按钮
	 */
	private JButton getaddBtn() {
		if (addBtn == null) {
			addBtn = new JButton();
			addBtn.setFocusPainted(false);
			addBtn.setBounds(new Rectangle(10, 580, 80, 26));
			addBtn.setFont(new Font("黑体", Font.BOLD, 12));
			addBtn.setText("添加好友");
			addBtn.setBackground(null);
			addBtn.setBorder(null);
		}
		return addBtn;
	}

	/**
	 * 此方法初始化添加好友列表jPanel_add
	 */
	private JPanel getaddJPanel() {
		if (jPanel_add == null) {
			jPanel_add = new JPanel();
			jPanel_add.setLayout(new BoxLayout(jPanel_add, BoxLayout.X_AXIS));
			jPanel_add.setBounds(new Rectangle(0, 78, 226, 255));
			jPanel_add.setBackground(null);
			jPanel_add.repaint();
		}
		return jPanel_add;
	}

	/**
	 * 此方法初始化个人设置按钮
	 */
	private JButton getsetBtn() {
		if (setBtn == null) {
			setBtn = new JButton();
			setBtn.setFocusPainted(false);
			setBtn.setBounds(new Rectangle(136, 580, 80, 26));
			setBtn.setFont(new Font("黑体", Font.BOLD, 12));
			setBtn.setText("个人设置");
			setBtn.setBackground(null);
		}
		return setBtn;
	}

	/**
	 * 返回喇叭 noteBabo
	 */
	public JLabel getnoteBabo() {
		return noteBabo;
	}

	/**
	 * 返回头像headpic
	 */
	public JLabel getheadpic() {
		if (headpic == null) {
			headpic = new JLabel();
			headpic.setBounds(new Rectangle(52, 10, 200, 20));
			headpic.setForeground(Color.BLUE);
			headpic.setFont(new Font("黑体", Font.PLAIN, 16));
			//headpic.setText("昵称");
		}
		return headpic;
	}

	/**
	 * 设置喇叭noteBabo
	 */
	public void setnoteBabo(JLabel label) {
		noteBabo = label;
	}

	/**
	 * 实现ActionListener接口所需实现的方法
	 */
	public void actionPerformed(ActionEvent e) {
		/**
		 * 添加好友
		 */
		if (e.getSource() == addBtn) {
			Client.client.addFriend();
		}
		/**
		 * 个人设置
		 */
		if (e.getSource() == setBtn) {
			Client.client.ModifyUserInfo();
		}
	}
}
