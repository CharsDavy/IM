/**
 * 主界面好友列表上的操作布局
 */

package client;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import setting.User;

/**
 * 
 * @author
 */
public class FriendTree {
	/**
	 * 定义DefaultTreeModel
	 */
	DefaultTreeModel treemode;

	/**
	 * 定义根节点"我的好友"
	 */
	JTreeBean root_bean = new JTreeBean("我的好友", new ImageIcon(Toolkit
			.getDefaultToolkit().getImage(
					getClass().getResource("/picture/client.gif"))),
			Color.BLACK);
	JTreeBean online_bean = new JTreeBean("在线好友", new ImageIcon(Toolkit
			.getDefaultToolkit().getImage(
					getClass().getResource("/picture/client.gif"))),
			Color.BLACK);
	JTreeBean quitline_bean = new JTreeBean("离线好友", new ImageIcon(Toolkit
			.getDefaultToolkit().getImage(
					getClass().getResource("/picture/client.gif"))),
			Color.BLACK);
	DefaultMutableTreeNode root = new DefaultMutableTreeNode(root_bean);
	/**
	 * 以节点"我的好友"构造一棵树
	 */
	JTree tree = new JTree(root);

	/**
	 * 定义节点"在线的好友"
	 */
	DefaultMutableTreeNode online;

	/**
	 * 定义节点"不在线的好友"
	 */
	DefaultMutableTreeNode quitline;

	/**
	 * 添加"在线的好友"和"不在线的好友"节点
	 */
	JMenuItem chatMenuItem; // 弹出式菜单“聊天”
	JMenuItem delMenuItem; // 弹出式菜单“删除好友”
	JMenuItem broInfoMenuItem; // 弹出式菜单“查看信息”
	JMenuItem logChatMenuItem; // 弹出式菜单“聊天记录”
	JMenuItem broIPMenuItem; // 弹出式菜单“查看好友IP”

	/**
	 * 树初始化
	 */
	public JTree chushi()// 未加任何好友的情况
	{
		tree.setBackground(Color.white);
		online = new DefaultMutableTreeNode(online_bean);
		quitline = new DefaultMutableTreeNode(quitline_bean);
		treemode = (DefaultTreeModel) tree.getModel();// 得到DefaultTreeModel
		treemode.insertNodeInto(online, root, 0);
		treemode.insertNodeInto(quitline, root, 1);
		tree.setCellRenderer(new JTreeCellRenderer());
		tree.scrollPathToVisible(new TreePath(quitline.getPath()));
		tree.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			public void mousePressed(MouseEvent e) {
				@SuppressWarnings("unused")
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				if (e.getClickCount() == 2) {
					JTree tree = (JTree) e.getSource();
					int rowLocation = tree.getRowForLocation(e.getX(), e.getY());
					TreePath treepath = tree.getPathForRow(rowLocation);
					DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) treepath
							.getLastPathComponent();
					JTreeBean jtb = (JTreeBean) treenode.getUserObject();
					String id = jtb.getString();
					Client.client.openChatUI(id);
					//new ServerTcpListener();
				}
				if (SwingUtilities.isRightMouseButton(e)) {

					JPopupMenu popupMenu = new JPopupMenu();

					ItemListenerimp Itemimp = new ItemListenerimp(e);
					chatMenuItem = new JMenuItem();
					delMenuItem = new JMenuItem();
					broInfoMenuItem = new JMenuItem();
					logChatMenuItem = new JMenuItem();
					broIPMenuItem = new JMenuItem();
					chatMenuItem.addActionListener(Itemimp);
					delMenuItem.addActionListener(Itemimp);
					broInfoMenuItem.addActionListener(Itemimp);
					logChatMenuItem.addActionListener(Itemimp);
					broIPMenuItem.addActionListener(Itemimp);
					chatMenuItem.setText("聊天");
					delMenuItem.setText("删除好友");
					broInfoMenuItem.setText("查看信息");
					logChatMenuItem.setText("聊天记录");
					broIPMenuItem.setText("查看好友IP");
					popupMenu.add(chatMenuItem);
					popupMenu.add(delMenuItem);
					popupMenu.add(broInfoMenuItem);
					popupMenu.add(logChatMenuItem);
					popupMenu.add(broIPMenuItem);

					DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) selPath
							.getLastPathComponent();
					JTreeBean jtb = (JTreeBean) treenode.getUserObject();
					try {
						Integer.parseInt(jtb.getString());
						popupMenu.show(e.getComponent(), e.getX(), e.getY());
					} catch (Exception ecast) {
						return;
					}
				}
			}
		});
		return tree;
	}

	/**
	 * 根据id在“在线的好友"的子节点中添加该以id为节点的子节点
	 */
	public void addonlinefriend(int id) {
		treemode = (DefaultTreeModel) tree.getModel();// 得到DefaultTreeModel
		JTreeBean online_ico = new JTreeBean(String.valueOf(id), new ImageIcon(
				Toolkit.getDefaultToolkit().getImage(
						getClass().getResource("/picture/online.png"))),
				Color.BLACK);
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(online_ico);
		treemode.insertNodeInto(newNode, online, online.getChildCount());
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);// 只能选取一个节点
		tree.scrollPathToVisible(new TreePath(newNode.getPath()));
	}

	/**
	 * 根据id在“不在线的好友"的子节点中添加该以id为节点的子节点
	 */
	public void addnotonlinefriend(int id) {
		JTreeBean quitline_ico = new JTreeBean(String.valueOf(id),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						getClass().getResource("/picture/quitline.png"))),
				Color.BLACK);
		treemode = (DefaultTreeModel) tree.getModel();// 得到DefaultTreeModel
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
				quitline_ico);
		treemode.insertNodeInto(newNode, quitline, quitline.getChildCount());
		tree.scrollPathToVisible(new TreePath(newNode.getPath()));
	}

	/**
	 * 首先在在线的好友节点中 删除离线的那位好友 再在不在线的好友节点中 添加离线的那位好友
	 */
	public void liveline(User user, int id) {
		delNode(user, id);
		addNode(id);
	}

	/**
	 * 首先在不在线的好友节点中 删除离线的那位好友 再在在线的好友节点中 添加离线的那位好友
	 */
	public void shangxian(User user, int id) {
		delNode2(user, id);
		addNode2(id);
	}

	/**
	 * 根据id在“不在线的好友"的子节点将此id删除
	 */
	@SuppressWarnings("unchecked")
	public void delNode2(User user, int id)// 删除节点
	{
		DefaultMutableTreeNode n = new DefaultMutableTreeNode();
		if (quitline.getChildCount() > 0) {// 若在线好友大于零
			for (Enumeration e = quitline.children(); e.hasMoreElements();) {
				n = (DefaultMutableTreeNode) e.nextElement();
				JTreeBean n1 = (JTreeBean) n.getUserObject();
				if (n1.getString().equals(String.valueOf(id))) {
					quitline.remove(n);// 查找在线的列表中有没有此ID有则删除
					treemode.reload();
					tree.scrollPathToVisible(new TreePath(n.getPath()));
				}
			}
		}
	}

	/**
	 * 根据id在“在线的好友"的子节点将此id删除
	 */
	@SuppressWarnings("unchecked")
	public void delNode(User user, int id)// 删除节点
	{
		DefaultMutableTreeNode n = new DefaultMutableTreeNode();
		if (online.getChildCount() > 0) {// 若在线好友大于零
			for (Enumeration e = online.children(); e.hasMoreElements();) {
				n = (DefaultMutableTreeNode) e.nextElement();
				JTreeBean n1 = (JTreeBean) n.getUserObject();
				if (n1.getString().equals(String.valueOf(id))) {
					online.remove(n);// 查找在线的列表中有没有此ID有则删除
					treemode.reload();
					tree.scrollPathToVisible(new TreePath(n.getPath()));
				}
			}
		}
	}

	/**
	 * 根据id在“不在线的好友"节点添加该以id为节点的子节点
	 */
	public void addNode(int id) {
		treemode = (DefaultTreeModel) tree.getModel();// 得到DefaultTreeModel
		JTreeBean quitline_ico = new JTreeBean(String.valueOf(id),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						getClass().getResource("/picture/quitline.png"))),
				Color.BLACK);
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
				quitline_ico);
		treemode.insertNodeInto(newNode, quitline, quitline.getChildCount());
		tree.scrollPathToVisible(new TreePath(newNode.getPath()));
	}

	/**
	 * 根据id在“在线的好友"节点添加该以id为节点的子节点
	 */
	public void addNode2(int id) {
		treemode = (DefaultTreeModel) tree.getModel();// 得到DefaultTreeModel
		JTreeBean online_ico = new JTreeBean(String.valueOf(id), new ImageIcon(
				Toolkit.getDefaultToolkit().getImage(
						getClass().getResource("/picture/online.png"))),
				Color.BLACK);
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(online_ico);
		treemode.insertNodeInto(newNode, online, online.getChildCount());
		tree.scrollPathToVisible(new TreePath(newNode.getPath()));
	}

	/**
	 * 根据id在“在线的好友"节点或"不在线的好友"节点中 删除该以id为节点的子节点
	 */
	@SuppressWarnings({ "unchecked", "unchecked" })
	public void delfriend(int id) {
		DefaultMutableTreeNode n = new DefaultMutableTreeNode();
		for (Enumeration e = online.children(); e.hasMoreElements();) {
			n = (DefaultMutableTreeNode) e.nextElement();
			JTreeBean JTB_r = (JTreeBean) n.getUserObject();
			if (JTB_r.getString().equals(String.valueOf(id))) {
				online.remove(n);// 查找在线的列表中有没有此ID有则删除
				treemode.reload();
				tree.scrollPathToVisible(new TreePath(n.getPath()));
			}
		}
		for (Enumeration e = quitline.children(); e.hasMoreElements();) {
			n = (DefaultMutableTreeNode) e.nextElement();
			JTreeBean n1 = (JTreeBean) n.getUserObject();

			if (n1.getString().equals(String.valueOf(id))) {
				quitline.remove(n);// 查找在线的列表中有没有此ID有则删除
				treemode.reload();
				tree.scrollPathToVisible(new TreePath(n.getPath()));
			}
		}
	}
}

class ItemListenerimp implements ActionListener {

	MouseEvent me;
	String id;

	ItemListenerimp(MouseEvent e) {
		me = e;
		JTree tree = (JTree) me.getSource();
		int rowLocation = tree.getRowForLocation(me.getX(), me.getY());
		TreePath treepath = tree.getPathForRow(rowLocation);
		DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) treepath
				.getLastPathComponent();
		JTreeBean jtb = (JTreeBean) treenode.getUserObject();
		id = jtb.getString();
	}

	public void actionPerformed(ActionEvent e) {
		/**
		 * 开启聊天窗口
		 */
		if (e.getSource() == Client.friendTree.chatMenuItem) {
			Client.client.openChatUI(id);
//			new ServerTcpListener();
		}

		/**
		 * 查看IP
		 */
		if (e.getSource() == Client.friendTree.broIPMenuItem) {
			try {
				Client.client.checkfriendIP(Integer.parseInt(id));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
		}

		/**
		 * 删除好友
		 */
		if (e.getSource() == Client.friendTree.delMenuItem) {
			Client.client.delFriend(Integer.parseInt(id));
		}

		/**
		 * 查看好友信息
		 */
		if (e.getSource() == Client.friendTree.broInfoMenuItem) {
			Client.client.checkfriendInfo(Integer.parseInt(id));
		}

		/**
		 * 查看聊天记录
		 */
		if (e.getSource() == Client.friendTree.logChatMenuItem) {
			Client.client.openChatRecord(id);
		}
	}
}