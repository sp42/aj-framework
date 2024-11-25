package com.ajaxjs;


/**
 * 初始化，检测是否可以运行
 */
public class Version {
    /**
     * 获取操作系统名称
     */
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    /**
     * 是否调试模式（开发模式）
     */
    public static boolean isDebug;

    static {
        if (!"Asia/Shanghai".equals(System.getProperty("user.timezone")))
            System.err.println("当前 JVM 非中国大陆时区");
        /*
         * 有两种模式：本地模式和远程模式（自动判断） 返回 true 表示是非 linux 环境，为开发调试的环境，即 isDebug = true； 返回
         * false 表示在部署的 linux 环境下。 Linux 的为远程模式
         */
        isDebug = !(OS_NAME.contains("nix") || OS_NAME.contains("nux") || OS_NAME.indexOf("aix") > 0);

//		if (!TestHelper.isRunningTest()) {
//			LOGGER.infoYellow("\n     ___       _       ___  __    __      _   _____        _          __  _____   _____  \n"
//					+ "     /   |     | |     /   | \\ \\  / /     | | /  ___/      | |        / / | ____| |  _  \\ \n"
//					+ "    / /| |     | |    / /| |  \\ \\/ /      | | | |___       | |  __   / /  | |__   | |_| |  \n"
//					+ "   / / | |  _  | |   / / | |   }  {    _  | | \\___  \\      | | /  | / /   |  __|  |  _  {  \n"
//					+ "  / /  | | | |_| |  / /  | |  / /\\ \\  | |_| |  ___| |      | |/   |/ /    | |___  | |_| |  \n"
//					+ " /_/   |_| \\_____/ /_/   |_| /_/  \\_\\ \\_____/ /_____/      |___/|___/     |_____| |_____/ \n");

//			LOGGER.infoGreen("Util 加载完毕，当前是[" + (isDebug ? "调试" : "生产环境") + "]模式");
//		}
    }

    private static Boolean isRunningTest;

    /**
     * 检测是否在运行单元测试
     *
     * @return true=运行单元测试
     */
    public static Boolean isRunningTest() {
        if (isRunningTest == null) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            for (StackTraceElement e : stackTrace) {
                if (e.toString().lastIndexOf("junit.runners") > -1)
                    return true;
            }

            isRunningTest = false;
        }

        return isRunningTest;
    }
}
