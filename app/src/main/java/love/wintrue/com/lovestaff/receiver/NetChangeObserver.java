package love.wintrue.com.lovestaff.receiver;

public class NetChangeObserver {
	
	public static enum NetType {
		wifi, CMNET, CMWAP, noneNet,G2,G3,G4
	}

	/**
	 * 网络连接连接时调用
	 */
	public void onConnect(NetType type) {

	}

	/**
	 * 当前没有网络连接
	 */
	public void onDisConnect() {

	}
}
