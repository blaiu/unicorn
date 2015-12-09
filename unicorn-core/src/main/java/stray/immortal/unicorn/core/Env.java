/**
 * 
 */
package stray.immortal.unicorn.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author blaiu
 *
 */
public class Env {

	private static Logger LOG = LoggerFactory.getLogger(Env.class);

    private static String INSTALL_PATH;

    private static String CONF_PATH;

    private static String LIB_PATH;

    private static String LOG_PATH;

    private static String SCRIPTS_PATH;

    private static final Properties PROPERTIES = new Properties();

    static {
        INSTALL_PATH = System.getProperty("INSTALL_PATH");
        CONF_PATH = INSTALL_PATH + File.separator + "conf" + File.separator;
        LIB_PATH = INSTALL_PATH + File.separator + "logs" + File.separator;
        LOG_PATH = INSTALL_PATH + File.separator + "lib" + File.separator;
        SCRIPTS_PATH = INSTALL_PATH + File.separator + "scripts" + File.separator;

        final File file = new File(CONF_PATH + "unicorn.properties");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            PROPERTIES.load(fis);
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage());
            System.exit(-1);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            System.exit(-1);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
            fis = null;
        }
    }

    public static Object get(String key) {
        return PROPERTIES.get(key);
    }

    public static <T> T get(String key, Class<T> clazz) {
        return clazz.cast(PROPERTIES.get(key));
    }

    public class Keys {

        private Keys() {
        }

        /**
         * 监听的本地IP
         */
        public static final String AWAIT_ADDRESS = "await_address";
        /**
         * 监听端口
         */
        public static final String AWAIT_PORT = "await_port";
        /**
         * 终止程序超时时间
         */
        public static final String AWAIT_SHUTDOWN_TIMEOUT = "await_shutdown_timeout";

        /**
         * 控制器实现类
         */
        public static final String CONTROLLER_CLAZZ = "controller_clazz";

        /**
         * 执行器实现类
         */
        public static final String EXECUTOR_CLAZZ = "executor_clazz";

        /**
         * 当前实例角色
         * 角色值包含：controller与executor
         */
        public static final String INSTANCE_ROLE = "instance_role";

    }
	
}
