/**
 * 聊天记录
 */

package client;

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
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * 
 * @author
 */
public class Chatrecord extends JFrame {
	private static final long serialVersionUID = 146565654168L;
	/**
	 * 文本域：存放聊天记录
	 */
	JTextPane chatlogArea = null;

	/**
	 * 关闭按钮
	 */
	JButton closeBtn = null;

	/**
	 * 打开按钮
	 */
	JButton openBtn = null;
	/**
	 * 窗体
	 */
	JFrame winFrame = null;// 窗体

	/**
	 * 添加按钮等
	 */
	JPanel winMainJP = null;

	/**
	 * 聊天发送者号码
	 */
	int souceid = 0;// 聊天发送者

	/**
	 * 聊天接收者号码
	 */
	int destid = 0;// 聊天目标

	/**
	 * 添加文本区
	 */
	JScrollPane textjSP = null;

	static Chatrecord chatrecord;

	/**
	 * 构造函数
	 */
	public Chatrecord(int souceid, int destid) {
		this.souceid = souceid;
		this.destid = destid;
	}

	/**
	 * 此方法初始化winFrame
	 */
	public JFrame getwinFrame() {
		if (winFrame == null) {
			winFrame = new JFrame();
			//winFrame.setBackground());
			winFrame.setSize(460, 190);
			winFrame.setContentPane(getwinMainJP());
			Toolkit toolkit = winFrame.getToolkit();
			Dimension screen = toolkit.getScreenSize();
			winFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(
					getClass().getResource("/picture/client.gif")));
			winFrame.setBounds(screen.width / 2 - 460 / 2,
					screen.height / 2 - 350 / 2, 428, 390);// //让窗体在屏幕正中央显示
			winFrame.setVisible(true);
			winFrame.setResizable(false);
			winFrame.getRootPane().setDefaultButton(openBtn);
			winFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					winFrame.setVisible(false);// 隐藏窗体
				}
			});
		}
		return winFrame;
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
		if (chatlogArea == null) {
			chatlogArea = new JTextPane();
			//chatlogArea.setTabSize(8);
		}
		if (closeBtn == null) {
			closeBtn = new JButton();
			closeBtn.setFont(new Font("黑体", Font.PLAIN, 12));
		}
		if (openBtn == null) {
			openBtn = new JButton();
			openBtn.setFont(new Font("黑体", Font.PLAIN, 12));
		}
		if (textjSP == null) {
			textjSP = new JScrollPane();
		}
		chatlogArea.setBackground(Color.white);
		chatlogArea.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
		chatlogArea.setForeground(Color.black);
		//chatlogArea.setBorder(BorderFactory.createLineBorder(Color.green));
		//chatlogArea.setLineWrap(true);
		chatlogArea.setBounds(6, 9, 100, 200);
		chatlogArea.setEditable(false);
		textjSP.setBounds(new Rectangle(6, 9, 360, 260));
		textjSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		textjSP.getViewport().add(chatlogArea);// 聊天记录
		closeBtn.setBounds(new Rectangle(59, 280, 62, 24));
		closeBtn.setForeground(SystemColor.menuText);
		closeBtn.setText("关闭");
		openBtn.setBounds(new Rectangle(230, 280, 62, 24));
		openBtn.setForeground(SystemColor.menuText);
		openBtn.setText("查看");
		/**
		 * 打开按钮事件监听
		 */
		openBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == openBtn)// 查看聊天记录
					displayChatLog();

			}
		});
		/**
		 * 关闭按钮行为事件监听
		 */
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == closeBtn)
					winFrame.dispose();// 隐藏窗体
			}
		});
		winMainJP.setEnabled(true);
		winMainJP.add(closeBtn);
		winMainJP.add(openBtn);
		winMainJP.add(textjSP);
		return winMainJP;
	}

	/**
	 * 查看聊天记录
	 */
	public void displayChatLog() {
		chatlogArea.setText("");
		try {
			String name = ".//" + String.valueOf(souceid) + ".txt";
			BufferedReader fileread = new BufferedReader(new FileReader(name));
			String temp = "";
			do {
				temp = fileread.readLine();
				if (temp != null){
///////////////////////////解析图片表情///////////////////////////////////////////
					temp+="\n";
					int length=temp.length();//获取String 长度
					  char[] every=new char[length];
					  int count=0;//初始字符的位置，变化
					  String path="phiz/";
					  Document doc = chatlogArea.getStyledDocument();
					  SimpleAttributeSet attr=new SimpleAttributeSet();            
					  Boolean hadjin=false;   
					   for(int i=0;i<temp.length();i++)
					  {
					   every[i]=temp.charAt(i);
					     if(every[i]=='#')  //识别信息中是否有#号
					    hadjin=true;
					  }
					  //开始解析	  
					  for(int i=0;i<temp.length();i++)
					  {
					   if(hadjin=false)
					    break;
					   if(every[i]=='#')
					   {
					    String str=null;
					       str=temp.substring(count,i);      //得到表情前的文字
					    
					    try{
					     if(str!=null)
					       doc.insertString(doc.getLength(), str, attr);//添加表情前的文字
					    }catch(Exception e2){
					    	e2.printStackTrace();
					    }    				    
					    String icName;
					    icName=temp.substring(i, i+3);//得到表情的名字 					    
					    Icon ic=new ImageIcon(path+icName+".gif");//将表情转化为icon
					    chatlogArea.setCaretPosition(doc.getLength());//获取当前的位置
					    chatlogArea.insertIcon(ic); //加入表情               
					    count=i+3;//将字符起始位置跳到表情后第一位置
					    }
					  }	 
					  if(count<temp.length())
					  {
					   String theLast=null;
					   theLast=temp.substring(count, length);
					   try{					   
					    doc.insertString(doc.getLength(), theLast, attr);
					    }catch(Exception e3){
					     e3.printStackTrace();
					    }
					  }
//////////////////////////////////////////////////////////////////////////////////////
				}
			} while (temp != null);
			fileread.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
