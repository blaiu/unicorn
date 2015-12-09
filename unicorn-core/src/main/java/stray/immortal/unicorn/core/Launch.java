/**
 * 
 */
package stray.immortal.unicorn.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author blaiu
 *
 */
public class Launch {
	
	private static Logger LOG = LoggerFactory.getLogger(Launch.class);
	
	private String address;

    private int port;

    private int shutdownTimeout;
    
    public Launch() {
    	this.address = Env.get(Env.Keys.AWAIT_ADDRESS, String.class);
        this.port = Env.get(Env.Keys.AWAIT_PORT, Integer.class);
        this.shutdownTimeout = Env.get(Env.Keys.AWAIT_SHUTDOWN_TIMEOUT, Integer.class);
    }
    
    public void start() {
    	
    }
    
    public void stop() {
    	
    }
    
    private void load() throws Exception {
    	
    }
}
