/**
 * 客户端聊天界面
 */

package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JWindow;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import fileTrans.TransfileRecive;
import fileTrans.TransfileSend;

import setting.User;

/**
 * 
 * @author
 */
public class ChatUI extends JFrame {
	private static final long serialVersionUID = 1453456465L;
	JDesktopPane desktop = new JDesktopPane();
	/**
	 * 文件
	 */
	File file;
	/**
	 * 文本域：存放聊天记录
	 */
	public static JTextPane dsiplChatArea = null;

	/**
	 * 文本域：聊天输入
	 */
	public static JTextPane inputChatArea = null;

	/**
	 * 发送截图标记false为非截图发送
	 */
	public static boolean isCapScr = false;

	/**
	 * 关闭按钮
	 */
	JButton closeBtn = null;

	/**
	 * 发送按钮
	 */
	JButton sendBtn = null;
	/**
	 * 窗体
	 */
	JFrame win = null;// 窗体

	/**
	 * 添加按钮等
	 */
	JPanel winMainJP = null;

	/**
	 * 聊天发送者号码
	 */
	public static int souceid = 0;// 聊天发送者

	/**
	 * 聊天接收者号码
	 */
	public static int destid = 0;// 聊天目标

	/**
	 * 显示消息文本区
	 */
	JScrollPane displayMessjSP = null;

	/**
	 * 发送消息文本区
	 */
	JScrollPane sendMessjSP = null;

	User userInfo = new User();

	// 菜单栏
	JMenuBar mb = new JMenuBar();
	JMenu fileMenu = new JMenu("文件");
	JMenuItem sendItem = new JMenuItem("发送");
	JMenuItem reciveItem = new JMenuItem("接收");
	JMenuItem refuseItem = new JMenuItem("拒绝");
	JMenu pictureMenu = new JMenu("截图");
	JMenuItem capScreenItem = new JMenuItem("截取图片");
	JMenuItem reccapScrItem = new JMenuItem("接收截图");
	JButton qqButton = null;
	// 窗体高度、宽度
	int width = 425;
	int height = 425;

	/**
	 * 构造函数
	 */
	public ChatUI(int souceid, int destid) {
		this.souceid = souceid;
		this.destid = destid;
	}

	/**
	 * 此方法初始化
	 */
	public JFrame getwinFrame() {
		if (win == null) {
			win = new JFrame();
			win.setSize(width, height);
			win.setContentPane(getwinMainJP());
			fileMenu.add(sendItem);
			fileMenu.add(reciveItem);
			fileMenu.add(refuseItem);
			pictureMenu.add(capScreenItem);
			pictureMenu.add(reccapScrItem);
			mb.add(fileMenu);
			mb.add(pictureMenu);
			win.setJMenuBar(mb);
			Toolkit toolkit = win.getToolkit();
			win.setIconImage(Toolkit.getDefaultToolkit().getImage(
					getClass().getResource("/picture/client.gif")));
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			// 计算屏幕中心坐标
			int centerX = screenSize.width / 2;
			int centerY = screenSize.height / 2;
			// 设置窗体居中
			win.setBounds(centerX - width / 2, centerY - height / 2 - 30,
					width, height);
			win.setVisible(true);
			win.setResizable(false);
			win.getRootPane().setDefaultButton(sendBtn);
		}
		return win;
	}

	/**
	 * 此方法初始化winMainJP
	 */
	public JPanel getwinMainJP() {
		if (winMainJP == null) {
			winMainJP = new JPanel();
			winMainJP.setBackground(Color.white);
			winMainJP.setLayout(null);
		}
		if (dsiplChatArea == null) {
			dsiplChatArea = new JTextPane();
		}
		if (inputChatArea == null) {
			inputChatArea = new JTextPane();
			inputChatArea.setFocusCycleRoot(true);
		}
		if (closeBtn == null) {
			closeBtn = new JButton();
			closeBtn.setFocusPainted(true);
			closeBtn.setFont(new Font("黑体", Font.PLAIN, 14));
		}
		if (sendBtn == null) {
			sendBtn = new JButton();
			sendBtn.setFocusPainted(true);
		}
		if (displayMessjSP == null) {
			displayMessjSP = new JScrollPane();
		}
		if (sendMessjSP == null) {
			sendMessjSP = new JScrollPane();
		}
		if (fileMenu == null) {
			fileMenu = new JMenu("文件");
		}
		if (sendItem == null) {
			sendItem = new JMenuItem("发送");
			sendItem.setFocusPainted(true);
		}
		if (reciveItem == null) {
			reciveItem = new JMenuItem("接收");
			reciveItem.setFocusPainted(true);
		}
		if (pictureMenu == null) {
			pictureMenu = new JMenu("截图");
		}
		if (capScreenItem == null) {
			capScreenItem = new JMenuItem("截取");
			capScreenItem.setFocusPainted(true);
		}
		if (reccapScrItem == null) {
			reccapScrItem = new JMenuItem("接收截图");
			reccapScrItem.setFocusPainted(true);
		}
		if (qqButton == null) {
			Icon icon = new ImageIcon("src/picture/online.png");
			qqButton = new JButton(icon);
		}
		dsiplChatArea.setBackground(Color.white);
		dsiplChatArea.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		dsiplChatArea.setForeground(Color.blue);
		dsiplChatArea.setBounds(6, 9, 100, 200);
		dsiplChatArea.setEditable(false);
		displayMessjSP.setBounds(new Rectangle(6, 9, 360, 180));
		displayMessjSP
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		displayMessjSP.getViewport().add(dsiplChatArea);// 聊天记录
		inputChatArea.setFont(new Font("黑体", Font.PLAIN, 14));
		inputChatArea.setBounds(new Rectangle(6, 207, 200, 60));
		sendMessjSP.setBounds(6, 207, 360, 60);
		sendMessjSP
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		sendMessjSP.getViewport().add(inputChatArea);
		closeBtn.setBounds(new Rectangle(45, 278, 80, 24));
		closeBtn.setForeground(SystemColor.menuText);
		closeBtn.setText("关闭");
		closeBtn.setUI(new BEButtonUI()
				.setNormalColor(BEButtonUI.NormalColor.red));
		closeBtn.setFont(new Font("黑体", Font.PLAIN, 12));
		sendBtn.setBounds(new Rectangle(250, 278, 80, 24));
		sendBtn.setFont(new Font("黑体", Font.PLAIN, 12));
		sendBtn.setForeground(SystemColor.menuText);
		sendBtn.setText("发送");
		sendBtn.setUI(new BEButtonUI()
				.setNormalColor(BEButtonUI.NormalColor.green));
		qqButton.setBounds(new Rectangle(157, 274, 56, 35));
		qqButton.setIcon(new ImageIcon("src/picture/fish.png"));
		qqButton.setBackground(null);
		/**
		 * 表情按钮行为事件监听
		 */
		qqButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final JWindow jWindow = new JWindow(win);
				jWindow.setLayout(new GridLayout(5, 5, 5, 5));
				jWindow.setAlwaysOnTop(true);
				jWindow.setBounds(300, 100, 350, 210);
				jWindow.setLocationRelativeTo(qqButton);
				int jwX = jWindow.getX()
						+ (jWindow.getWidth() - qqButton.getWidth()) / 2;
				int jwY = jWindow.getY()
						+ (jWindow.getHeight() - qqButton.getHeight()) / 2;
				jWindow.setLocation(jwX, jwY);
				jWindow.setFocusable(true);
				JLabel[] ico = new JLabel[55];
				String fileName = "";
				for (int i = 1; i < ico.length; i++) {
					if (i < 10)
						fileName = "phiz/#0" + i + ".gif";// 修改图片路径
					else if (i >= 10)
						fileName = "phiz/#" + i + ".gif";// 修改图片路径
					ico[i] = new JLabel(new ImageIcon(fileName));
					final Icon img1 = ico[i].getIcon();
					ico[i].addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							inputChatArea.insertIcon(img1);
							jWindow.dispose();
						}
					});
					jWindow.add(ico[i]);
				}
				jWindow.setBackground(SystemColor.text);
				jWindow.setVisible(true);

				jWindow.addFocusListener(new FocusListener() {

					@Override
					public void focusLost(FocusEvent arg0) {
						// TODO Auto-generated method stub
						jWindow.dispose();
					}

					@Override
					public void focusGained(FocusEvent arg0) {
						// TODO Auto-generated method stub

					}
				});
			}

		});
		/**
		 * 发送按钮行为事件监听
		 */
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ////////////////////////////////////////////////////////////////////////////////////
				if (isCapScr) {// 发送截图
					Client.client.getfriendIP(client.ChatUI.destid);
					String pathString = ".\\picScreen\\capScr.jpg";
					String fileName = "capScr.jpg";
					new TransfileSend().start(pathString, fileName);
					inputChatArea.setText("");
					ImageIcon imageIcon = new ImageIcon(
							".\\picScreen\\capScr.jpg");
					StyledDocument doc = dsiplChatArea.getStyledDocument();
					dsiplChatArea.setCaretPosition(doc.getLength());
					dsiplChatArea.insertIcon(imageIcon);
					isCapScr = false;
				} else {
					ArrayList<String> picList = new ArrayList<String>();
					int imageLength = 0;
					for (int i = 0; i < inputChatArea.getStyledDocument()
							.getRootElements()[0].getElement(0)
							.getElementCount(); i++) {
						Icon icon = StyleConstants.getIcon(inputChatArea
								.getStyledDocument().getRootElements()[0]
								.getElement(0).getElement(i).getAttributes());
						if (icon != null) {
							picList.add(icon.toString());
							imageLength = imageLength + 2;
						}
					}
					int k = 0;
					String inputChatAreasString = "\n用户"
							+ String.valueOf(souceid) + " " + Client.getTime()
							+ "\n";
					for (int i = 0; i < inputChatArea.getText().length(); i++) {
						if (inputChatArea.getStyledDocument()
								.getCharacterElement(i).getName()
								.equals("icon")) {
							for (int j = 1; j < 46; j++) {
								if (picList.get(k).toString()
										.equals("phiz/#0" + j + ".gif")) {
									inputChatAreasString += "#0" + j;
								}
								if (picList.get(k).toString()
										.equals("phiz/#" + j + ".gif")) {
									inputChatAreasString += "#" + j;
								}
							}
							k++;
						} else {
							try {
								inputChatAreasString += inputChatArea
										.getStyledDocument().getText(i, 1);
							} catch (BadLocationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					int length = inputChatAreasString.length();// 获取String 长度
					char[] every = new char[length];
					int count = 0;// 初始字符的位置，变化
					String path = "phiz/";
					Document doc = dsiplChatArea.getStyledDocument();
					SimpleAttributeSet attr = new SimpleAttributeSet();
					Boolean hadjin = false;
					for (int i = 0; i < inputChatAreasString.length(); i++) {
						every[i] = inputChatAreasString.charAt(i);
						if (every[i] == '#') // 识别信息中是否有#号
							hadjin = true;
					}
					// 开始解析
					for (int i = 0; i < inputChatAreasString.length(); i++) {
						if (hadjin = false)
							break;
						if (every[i] == '#') {
							String str = null;
							str = inputChatAreasString.substring(count, i); // 得到表情前的文字

							try {
								if (str != null)
									doc.insertString(doc.getLength(), str, attr);// 添加表情前的文字
							} catch (Exception e2) {
								e2.printStackTrace();
							}
							String icName;
							icName = inputChatAreasString.substring(i, i + 3);// 得到表情的名字
							Icon ic = new ImageIcon(path + icName + ".gif");// 将表情转化为icon
							dsiplChatArea.setCaretPosition(doc.getLength());// 获取当前的位置
							dsiplChatArea.insertIcon(ic); // 加入表情
							count = i + 3;// 将字符起始位置跳到表情后第一位置
						}
					}
					if (count < inputChatAreasString.length()) {
						String theLast = null;
						theLast = inputChatAreasString.substring(count, length);
						try {
							doc.insertString(doc.getLength(), theLast, attr);
						} catch (Exception e3) {
							e3.printStackTrace();
						}
					}
					// ////////////////////////////////////////////////////////////////////////////////////
					Client.client.sendChatMsg(inputChatAreasString, souceid,
							destid);// 执行Client的sendChatMsg()方法
					String name = ".//" + String.valueOf(souceid) + ".txt";
					try {
						FileWriter fw = new FileWriter(name, true);
						fw.write(inputChatAreasString);
						fw.flush();
						fw.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					inputChatArea.setText("");// 清空
				}
			}

		});
		/**
		 * 保存聊天记录按钮行为事件监听
		 */
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == closeBtn)
					win.dispose();// 隐藏窗体
			}
		});
		/**
		 * 发送文件按钮行为事件监听
		 */
		sendItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Client.client.getfriendIP(client.ChatUI.destid);
				JFileChooser chooser = new JFileChooser(".");
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.showOpenDialog(rootPane);
				File file = chooser.getSelectedFile();
				String pathString = file.getPath();
				String fileName = file.getName();
				new TransfileSend().start(pathString, fileName);
			}
		});
		new DropTarget(inputChatArea, DnDConstants.ACTION_COPY,
				new DropTargetListener());// 拖放
		/**
		 * 接收文件按钮行为事件监听
		 */
		reciveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Client.client.getfriendIP(client.ChatUI.destid);
				JFileChooser chooser = new JFileChooser(".");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.showSaveDialog(rootPane);
				String savePath = chooser.getSelectedFile().getPath();
				boolean flag = true;
				new TransfileRecive(savePath, flag);
			}
		});
		/**
		 * 拒绝接收文件按钮行为事件监听
		 */
		refuseItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Client.client.getfriendIP(client.ChatUI.destid);
				boolean flag = false;
				new TransfileRecive("", flag);
			}
		});
		/**
		 * 截图按钮行为事件监听
		 */
		capScreenItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				// System.out.println("截图");
				new CaptureScreen();
			}
		});
		/**
		 * 接收截图按钮行为事件监听
		 */
		reccapScrItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Client.client.getfriendIP(client.ChatUI.destid);
				String savePath = ".\\recScreen";
				boolean flag = true;
				new TransfileRecive(savePath, flag);
			}
		});
		winMainJP.setEnabled(true);
		winMainJP.add(closeBtn);
		winMainJP.add(sendBtn);
		winMainJP.add(qqButton);
		winMainJP.add(displayMessjSP);
		winMainJP.add(sendMessjSP);
		return winMainJP;
	}

	/**
	 * 
	 * 私有类
	 * 
	 * @功能 实现在文本输入框中拖放文件使文件发送功能
	 * 
	 */
	private class DropTargetListener extends DropTargetAdapter {

		@Override
		public void drop(DropTargetDropEvent e) {
			// TODO Auto-generated method stub
			Client.client.getfriendIP(client.ChatUI.destid);// 得到文件传送者的iP
			e.acceptDrop(DnDConstants.ACTION_COPY);// 使具有复制功能
			// 获取拖放文件
			Transferable transferable = e.getTransferable();
			DataFlavor[] flavors = transferable.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				DataFlavor d = flavors[i];
				try {
					if (d.equals(DataFlavor.javaFileListFlavor)) {
						List fileList = (List) transferable.getTransferData(d);
						for (Object f : fileList) {
							File file1 = (File) f;
							String pathString = file1.getPath();
							String fileName = file1.getName();
							new TransfileSend().start(pathString, fileName);// 传送文件
						}
					}
				} catch (Exception event) {
					event.printStackTrace();
				}

				e.dropComplete(true);
			}
		}
	}
}
