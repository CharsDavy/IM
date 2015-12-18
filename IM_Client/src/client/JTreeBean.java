/**
 * 好友列表节点
 */
package client;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * 
 * @author
 */
public class JTreeBean {

	private String strNode;
	private ImageIcon iconNode;
	private Color strColor;
	private JComponent jcp = null;

	public JTreeBean(String str, ImageIcon icon, Color color) {
		strNode = str;
		iconNode = icon;
		strColor = color;
	}

	public JTreeBean(String str, ImageIcon icon) {
		strNode = str;
		iconNode = icon;
	}

	public JTreeBean(String str) {
		strNode = str;
	}

	public String getString() {
		return strNode;
	}

	public void setString(String strNode) {
		this.strNode = strNode;
	}

	public void setJComponent(JComponent jcp) {
		this.jcp = jcp;
	}

	public ImageIcon getIcon() {
		return iconNode;
	}

	public void setIcon(ImageIcon iconNode) {
		this.iconNode = iconNode;
	}

	public Color getColor() {
		return strColor;
	}

	public void setColor(Color strColor) {
		this.strColor = strColor;
	}
}
