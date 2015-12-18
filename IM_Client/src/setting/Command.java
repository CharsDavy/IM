/**
 * 信令定义
 */

package setting;

/**
 * 
 * @author 
 */
public class Command {
	/**
	 * 服务器端口
	 */
	public final static int server_port = 8888;

	/**
	 * 服务器IP
	 */
	public final static String server_ipaddress = "";

	/**
	 * 服务器数据库文件
	 */
	public final static String userdatebase = "UserDB.db";

	/**
	 * 服务端用户登录信令
	 */
	public final static String login = "登录";

	/**
	 * 用户登录成功信令
	 */
	public final static String loginsuccess = "登录成功";

	/**
	 * 用户登录重复信令
	 */
	public final static String loginreapt = "重复登录";

	/**
	 * 用户登录失败信令
	 */
	public final static String loginfail = "登录失败";

	/**
	 * 服务端注册信令
	 */
	public final static String register = "注册";

	/**
	 * 客户端用户注册成功信令
	 */
	public final static String registersuccess = "注册成功";

	/**
	 * 注册失败信令
	 */
	public final static String registerfail = "注册失败";

	/**
	 * 好友在线
	 */
	public final static String friendisonline = "在线";

	/**
	 * 好友不在线
	 */
	public final static String friendisoffline = "离线";

	/**
	 * 将自己更细到好友的好友列表信令
	 */
	public final static String updatemetofriend = "好友更新";

	/**
	 * 收到更新好友列表信令
	 */
	public final static String updatemetofriendaccept = "更新好友";

	/**
	 * 检查是否有好友给我留言信令
	 */
	public final static String checkfriendnote = "检查留言";

	/**
	 * 有好友给我留言信令
	 */
	public final static String hasnote = "有留言";

	/**
	 * 没有好友给我留言信令
	 */
	public final static String nonote = "没有留言";

	/**
	 * 删除好友信令
	 */
	public final static String delfriend = "删除好友";

	/**
	 * 删除好友成功信令
	 */
	public final static String delfriendsuccess = "删除成功";

	/**
	 * 删除好友失败信令
	 */
	public final static String delfriendfail = "删除失败";

	/**
	 * 修改用户信息信令
	 */
	public final static String modifyusrinfo = "更改信息";

	/**
	 * 修改成功信令
	 */
	public final static String modifyusrinfosuccess = "更改成功";

	/**
	 * 聊天信令
	 */
	public final static String personchat = "聊天";

	/**
	 * 失败信令
	 */
	public final static String personchatfail = "聊天失败";

	/**
	 * 成功信令
	 */
	public final static String personchatsuccess = "聊天成功";

	/**
	 * 验证成功
	 */
	public final static String verifysuccess = "验证成功";

	/**
	 * 密码验证失败
	 */
	public final static String passwdwrong = "密码错误";
	/**
	 * 密送失败信令
	 */
	public final static String Inchatfail = "密送失败";

	/**
	 * 发送完成
	 */
	public final static String Inchatsuc = "已到达目的";
	/**
	 * 好友不在线
	 */
	public final static String friendoffline = "好友离线";

	/**
	 * 添加好友
	 */
	public final static String addfriend = "添加好友";

	/**
	 * 用户不存在
	 */
	public final static String addfriendisnotexist = "用户不存在";

	/**
	 * 用户不在线
	 */
	public final static String addfriendisoffline = "用户离线";

	/**
	 * 好友添加重复
	 */
	public final static String addfriendreapt = "重复添加";

	/**
	 * 是否同意对方加你为好友
	 */
	public final static String addfriendwait = "等待对方同意";

	/**
	 * 对方同意
	 */
	public final static String addfriendagree = "对方同意";

	/**
	 * 对方不同意
	 */
	public final static String addfriendrefuse = "对方拒绝";

	/**
	 * 对方同意后 发出信令是否加我
	 */
	public final static String addfriendagin = "同意加我";

	/**
	 * 对方按确认
	 */
	public final static String addfriendaginconfirm = "对方确认";

	/**
	 * 对方按取消
	 */
	public final static String addfriendagincancel = "对方取消";

	/**
	 * 按确认后把我加入他的好友列表
	 */
	public final static String addfriendaginflush = "更新列表";
	/**
	 * 查询好友信息
	 */
	public final static String queryfriendinfo = "查询好友信息";

	/**
	 * 查询好友IP
	 */
	public final static String queryfriendIP = "查询好友IP";
	/**
	 * 文件传输
	 */
    public final static String filetransmit = "文件传输";

}
