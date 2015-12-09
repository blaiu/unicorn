/**
 * 
 */
package stray.immortal.unicorn.boot;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

/**
 * @author blaiu
 *
 */
public class Boot {

	private static final String START = "START";

    private static final String STOP = "STOP";

    private static Boot boot;
    
    private ClassLoader classLoader;
    
    private Object unicornDaemon;
	
    public static void main(String[] args) {
    	boot = new Boot();
    	boot.init();
        processCommand(args, boot);
	}
    
    public void init() {
    	 String installPath = getSystemProperty("INSTALL_PATH");
    	 if (installPath == null || "".equals(installPath)) {
             installPath = getCurrentPath();
         }
    	 System.out.println("INFO:Current install path is [".concat(installPath).concat("]"));
    	 setSystemProperty("INSTALL_PATH", installPath);
         final String libPath = installPath + File.separator + "lib";
         final File libDir = new File(libPath);
         if (!libDir.exists()) {
             System.err.println("ERROR:Can not find the lib directory.");
             System.exit(-1);
         } else if (libDir.isFile()) {
             System.err.println("ERROR:The lib is not a directory.");
             System.exit(-1);
         }
         
         try {
             createClassLoaders(libDir);
             createUnicornDaemon();
         } catch (Exception e) {
             System.err.println("ClassLoader created error.");
             error(e);
         }
    }
    
    private static final String UNICORN_LAUNCH = "stray.immortal.unicorn.core.Launch";
    
    private void createUnicornDaemon() throws Exception {
        final Class<?> clazz = Class.forName(UNICORN_LAUNCH, false, classLoader);
        unicornDaemon = clazz.newInstance();
    }
    
    private void createClassLoaders(File libDir) throws Exception {
        File[] files = libDir.listFiles();
        List<URL> urls = new LinkedList<URL>();
        for (File file : files) {
            if (file.getName().endsWith(".jar")) {
                urls.add(file.toURI().toURL());
            }
        }
        final URL[] array = new URL[urls.size()];
        urls.toArray(array);
        classLoader = createClassLoader(array, ClassLoader.getSystemClassLoader());
    }
    
    private ClassLoader createClassLoader(URL[] urls, ClassLoader parent) {
        URLClassLoader newLoader = null;
        if (parent != null) {
            newLoader = new URLClassLoader(urls, parent);
        } else {
            newLoader = new URLClassLoader(urls);
        }
        return newLoader;
    }
    
    private String getSystemProperty(String key) {
        return System.getProperty(key);
    }
    
    private void setSystemProperty(String key, String value) {
        System.setProperty(key, value);
    }
    
    private String getCurrentPath() {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("getProtectionDomain"));
        }
        final URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        File codeSource = null;
        try {
            codeSource = new File(url.toURI());
        } catch (URISyntaxException e) {
            error(e);
        }
        return codeSource.getParentFile().getAbsolutePath();
    }
    
    private void error(Exception e) {
        e.printStackTrace(System.err);
        System.err.println("Runtime start error, will be Interrupted.");
        System.exit(-1);
    }
    
    private static void processCommand(String[] args, Boot boot) {
        String command = START;
        if (args != null) {
            command = args[args.length - 1];
        }
        if (START.equalsIgnoreCase(command)) {
            boot.start();
        } else if (STOP.equalsIgnoreCase(command)) {
            boot.stop();
        } else {
            System.out.println("Current system can not supported command [" + args[0] + "]");
            System.exit(-1);
        }
    }

    private void stop() {
        invoke(unicornDaemon.getClass(), "stop", (Class<?>[]) null, unicornDaemon, (Object[]) null);
    }

    private void start() {
        invoke(unicornDaemon.getClass(), "start", (Class<?>[]) null, unicornDaemon, (Object[]) null);
    }

    private void invoke(Class<? extends Object> clazz, String methodName, Class<? extends Object>[] paramTypes, Object obj, Object... params) {
        Method method;
        try {
            method = clazz.getDeclaredMethod(methodName, paramTypes);
            method.invoke(unicornDaemon, params);
        } catch (Exception e) {
            error(e);
        }
    }
}
