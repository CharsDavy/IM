/**
 * 好友列表显示
 */

package client;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * 
 * @author
 */
class JTreeCellRenderer extends DefaultTreeCellRenderer {

	public JTreeCellRenderer() {
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (node.getUserObject() instanceof JTreeBean) {
		} else {
			System.out.println(node.getUserObject() + "admin");
		}
		JTreeBean jtb = (JTreeBean) node.getUserObject();
		setIcon(jtb.getIcon());
		setText(jtb.getString());
		setTextNonSelectionColor(jtb.getColor());
		//setBackground(null);
		return this;
	}

	protected boolean isTutorialBook(Object value) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		return false;
	}
}
