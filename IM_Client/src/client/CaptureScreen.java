package client;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.StyledDocument;

import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

/**
 * 截图
 * 
 * @author DENGWEI
 * 
 */
public class CaptureScreen extends JFrame implements ActionListener {
	private BufferedImage get;// BufferedImage 子类描述具有可访问图像数据缓冲区的 Image

	/** Creates a new instance of CaptureScreen */
	public CaptureScreen() {
		doStart();

	}

	private void doStart() {
		try {
			this.setVisible(false);// 使得当前界面不可见
			Robot ro = new Robot();// 此类用于为测试自动化、自运行演示程序和其他需要控制鼠标和键盘的应用程序生成本机系统输入事件
			Toolkit tk = Toolkit.getDefaultToolkit();// 获取默认的工具包
			Dimension di = tk.getScreenSize();// 得到屏幕大小
			Rectangle rec = new Rectangle(0, 0, di.width, di.height);
			BufferedImage bi = ro.createScreenCapture(rec);// 得到屏幕图像
			JFrame jf = new JFrame();
			Temp temp = new Temp(jf, bi, di.width, di.height);// 打开拷贝屏幕界面
			// 开始执行TEMP类中的监听
			jf.getContentPane().add(temp, BorderLayout.CENTER);
			jf.setUndecorated(true);// 禁用或启用此 frame 的装饰.只有在 frame
			// 不可显示时才调用此方法.必须设置
			jf.setSize(di);
			jf.setVisible(true);
			jf.setAlwaysOnTop(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存截图
	 */
	/**
	 * 公用的处理保存图片的方法 这个方法不再私有了
	 */
	public void doPicSave(BufferedImage get, String saveImagePath) {

		try {
			if (get == null) {
				JOptionPane.showMessageDialog(this, "图片不能为空!!", "错误",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			String about = "jpg";
			File file = new File(saveImagePath);
			String suffix = saveImagePath.substring(saveImagePath
					.lastIndexOf('.') + 1);
			ImageIO.write(get, about, file);
			System.out.println("保存图片到" + saveImagePath);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 一个内部类,它表示一个面板,一个可以被放进tabpane的面板
	// 也有自己的一套处理保存和复制的方法
	private class PicPanel extends JPanel implements ActionListener {
		JButton save, close;// 表示保存,复制,关闭的按钮
		BufferedImage get;// 得到的图片

		public PicPanel(BufferedImage get) {
			super(new BorderLayout());
			this.get = get;
		}

		public void actionPerformed(ActionEvent e) {

		}
	}

	// 一个暂时类，用于显示当前的屏幕图像,内部类
	private class Temp extends JPanel implements MouseListener,
			MouseMotionListener {
		private BufferedImage bi;
		private int width, height;
		private int startX, startY, endX, endY, tempX, tempY;
		private JFrame jf;
		private Rectangle select = new Rectangle(0, 0, 0, 0);// 表示选中的区域
		private Cursor cs = new Cursor(Cursor.CROSSHAIR_CURSOR);// 表示一般情况下的鼠标状态
		private States current = States.DEFAULT;// 表示当前的编辑状态
		private Rectangle[] rec;// 表示八个编辑点的区域
		// 下面四个常量,分别表示谁是被选中的那条线上的端点
		public static final int START_X = 1;
		public static final int START_Y = 2;
		public static final int END_X = 3;
		public static final int END_Y = 4;
		private int currentX, currentY;// 当前被选中的X和Y,只有这两个需要改变
		private Point p = new Point();// 当前鼠标移的地点
		private boolean showTip = true;// 是否显示提示.如果鼠标左键一按,则提示不再显了

		public Temp(JFrame jf, BufferedImage bi, int width, int height) {
			this.jf = jf;
			this.bi = bi;
			this.width = width;
			this.height = height;
			this.addMouseListener(this);// 添加鼠标单击和其他鼠标事件
			this.addMouseMotionListener(this);
			// 用于接收组件上的鼠标移动事件的侦听器接口(鼠标按键在组件上按下并拖动时调用。鼠标光标移动到组件上但无按键按下时调用。)
			initRecs();
		}

		private void initRecs() {// 初始化选中界面的八个点
			rec = new Rectangle[8];
			for (int i = 0; i < rec.length; i++) {
				rec[i] = new Rectangle();
			}
		}

		public void paintComponent(Graphics g) {// 如果在子类中重写此方法，则不应该对传入到 Graphics
												// 中的内容进行永久更改
			// 画出蓝色色的线
			g.drawImage(bi, 0, 0, width, height, this);
			g.setColor(Color.blue);
			g.drawLine(startX, startY, endX, startY);
			g.drawLine(startX, endY, endX, endY);
			g.drawLine(startX, startY, startX, endY);
			g.drawLine(endX, startY, endX, endY);
			int x = startX < endX ? startX : endX;
			int y = startY < endY ? startY : endY;
			select = new Rectangle(x, y, Math.abs(endX - startX), Math.abs(endY
					- startY));
			// 构造一个新的 Rectangle，其左上角被指定为（x，y），而其宽度和高度由同名称的参数指定。
			int x1 = (startX + endX) / 2;
			int y1 = (startY + endY) / 2;
			g.fillRect(x1 - 2, startY - 2, 5, 5);
			g.fillRect(x1 - 2, endY - 2, 5, 5);
			g.fillRect(startX - 2, y1 - 2, 5, 5);
			g.fillRect(endX - 2, y1 - 2, 5, 5);
			g.fillRect(startX - 2, startY - 2, 5, 5);
			g.fillRect(startX - 2, endY - 2, 5, 5);
			g.fillRect(endX - 2, startY - 2, 5, 5);
			g.fillRect(endX - 2, endY - 2, 5, 5);
			rec[0] = new Rectangle(x - 5, y - 5, 10, 10);
			rec[1] = new Rectangle(x1 - 5, y - 5, 10, 10);
			rec[2] = new Rectangle((startX > endX ? startX : endX) - 5, y - 5,
					10, 10);
			rec[3] = new Rectangle((startX > endX ? startX : endX) - 5, y1 - 5,
					10, 10);
			rec[4] = new Rectangle((startX > endX ? startX : endX) - 5,
					(startY > endY ? startY : endY) - 5, 10, 10);
			rec[5] = new Rectangle(x1 - 5, (startY > endY ? startY : endY) - 5,
					10, 10);
			rec[6] = new Rectangle(x - 5, (startY > endY ? startY : endY) - 5,
					10, 10);
			rec[7] = new Rectangle(x - 5, y1 - 5, 10, 10);
			if (showTip) {// 画出文字提示
				// 背景色（白色）
				g.setColor(Color.white);
				g.fillRect(p.x, p.y, 170, 20);
				// 边框颜色（灰色）
				g.setColor(Color.lightGray);
				g.drawRect(p.x, p.y, 170, 20);
				// 文字颜色（蓝色）
				g.setColor(Color.blue);
				g.drawString("请按住鼠标左键不放选择截图区", p.x, p.y + 15);
			}
		}

		// 根据东南西北等八个方向决定选中的要修改的X和Y的座标
		private void initSelect(States state) {
			switch (state) {
			case DEFAULT:
				currentX = 0;
				currentY = 0;
				break;
			case EAST:
				currentX = (endX > startX ? END_X : START_X);
				currentY = 0;
				break;
			case WEST:
				currentX = (endX > startX ? START_X : END_X);
				currentY = 0;
				break;
			case NORTH:
				currentX = 0;
				currentY = (startY > endY ? END_Y : START_Y);
				break;
			case SOUTH:
				currentX = 0;
				currentY = (startY > endY ? START_Y : END_Y);
				break;
			case NORTH_EAST:
				currentY = (startY > endY ? END_Y : START_Y);
				currentX = (endX > startX ? END_X : START_X);
				break;
			case NORTH_WEST:
				currentY = (startY > endY ? END_Y : START_Y);
				currentX = (endX > startX ? START_X : END_X);
				break;
			case SOUTH_EAST:
				currentY = (startY > endY ? START_Y : END_Y);
				currentX = (endX > startX ? END_X : START_X);
				break;
			case SOUTH_WEST:
				currentY = (startY > endY ? START_Y : END_Y);
				currentX = (endX > startX ? START_X : END_X);
				break;
			default:
				currentX = 0;
				currentY = 0;
				break;
			}
		}

		public void mouseMoved(MouseEvent me) {// 设置跟踪鼠标的提示文字
			doMouseMoved(me);
			initSelect(current);
			if (showTip) {
				p = me.getPoint();
				repaint();
			}
		}

		// 特意定义一个方法处理鼠标移动,是为了每次都能初始化一下所要选择的地区
		private void doMouseMoved(MouseEvent me) {
			if (select.contains(me.getPoint())) {
				this.setCursor(new Cursor(Cursor.MOVE_CURSOR));// 设置十字光标
				current = States.MOVE;// 设置移动状态
			} else {// 设置状态
				States[] st = States.values();
				for (int i = 0; i < rec.length; i++) {
					if (rec[i].contains(me.getPoint())) {
						current = st[i];
						this.setCursor(st[i].getCursor());
						return;
					}
				}
				this.setCursor(cs);
				current = States.DEFAULT;
			}
		}

		public void mouseDragged(MouseEvent me) {
			int x = me.getX();
			int y = me.getY();
			if (current == States.MOVE) {
				startX += (x - tempX);
				startY += (y - tempY);
				endX += (x - tempX);
				endY += (y - tempY);
				tempX = x;
				tempY = y;
			} else if (current == States.EAST || current == States.WEST) {
				if (currentX == START_X) {
					startX += (x - tempX);
					tempX = x;
				} else {
					endX += (x - tempX);
					tempX = x;
				}
			} else if (current == States.NORTH || current == States.SOUTH) {
				if (currentY == START_Y) {
					startY += (y - tempY);
					tempY = y;
				} else {
					endY += (y - tempY);
					tempY = y;
				}
			} else if (current == States.NORTH_EAST
					|| current == States.NORTH_EAST
					|| current == States.SOUTH_EAST
					|| current == States.SOUTH_WEST) {
				if (currentY == START_Y) {
					startY += (y - tempY);
					tempY = y;
				} else {
					endY += (y - tempY);
					tempY = y;
				}
				if (currentX == START_X) {
					startX += (x - tempX);
					tempX = x;
				} else {
					endX += (x - tempX);
					tempX = x;
				}

			} else {
				startX = tempX;
				startY = tempY;
				endX = me.getX();
				endY = me.getY();
			}
			this.repaint();
		}

		public void mousePressed(MouseEvent me) {
			showTip = false;// 鼠标按下获得一个临时点以后不再显示提示
			tempX = me.getX();
			tempY = me.getY();
		}

		public void mouseReleased(MouseEvent me) {
			if (me.isPopupTrigger()) {// isPopupTrigger返回此鼠标事件是否为该平台的弹出菜单触发事件。
				if (current == States.MOVE) {
					showTip = true;// 如果鼠标右键移动选区
					p = me.getPoint();
					startX = 0;
					startY = 0;
					endX = 0;
					endY = 0;
					repaint();
				} else { // 鼠标右键点击退出
					jf.dispose();
					// updates();
				}

			}
		}

		public void mouseClicked(MouseEvent me) {
			if (me.getClickCount() == 2) {// 如果双击得到选中的图片内容
				Point p = me.getPoint();
				if (select.contains(p)) {
					if (select.x + select.width < this.getWidth()
							&& select.y + select.height < this.getHeight()) {
						get = bi.getSubimage(select.x, select.y, select.width,
								select.height);
						// 获得图片
						jf.dispose();
					} else {
						int wid = select.width, het = select.height;
						if (select.x + select.width >= this.getWidth()) {
							wid = this.getWidth() - select.x;
						}
						if (select.y + select.height >= this.getHeight()) {
							het = this.getHeight() - select.y;
						}
						get = bi.getSubimage(select.x, select.y, wid, het);// 返回由指定矩形区域定义的子图像。
						jf.dispose();// 关闭屏幕拷贝界面
					}
				}
				doPicSave(get, ".\\picScreen\\capScr.jpg");

				ImageIcon imageIcon = new ImageIcon(".\\picScreen\\capScr.jpg");
				StyledDocument doc = ChatUI.inputChatArea.getStyledDocument();
				ChatUI.inputChatArea.setCaretPosition(doc.getLength());
				ChatUI.inputChatArea.insertIcon(imageIcon);
				ChatUI.isCapScr=true;
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}

// 一些表示状态的枚举
enum States {
	NORTH_WEST(new Cursor(Cursor.NW_RESIZE_CURSOR)), // 表示西北角
	NORTH(new Cursor(Cursor.N_RESIZE_CURSOR)), NORTH_EAST(new Cursor(
			Cursor.NE_RESIZE_CURSOR)), EAST(new Cursor(Cursor.E_RESIZE_CURSOR)), SOUTH_EAST(
			new Cursor(Cursor.SE_RESIZE_CURSOR)), SOUTH(new Cursor(
			Cursor.S_RESIZE_CURSOR)), SOUTH_WEST(new Cursor(
			Cursor.SW_RESIZE_CURSOR)), WEST(new Cursor(Cursor.W_RESIZE_CURSOR)), MOVE(
			new Cursor(Cursor.MOVE_CURSOR)), DEFAULT(new Cursor(
			Cursor.DEFAULT_CURSOR));
	private Cursor cs;

	States(Cursor cs) {
		this.cs = cs;
	}

	public Cursor getCursor() {
		return cs;
	}
}
