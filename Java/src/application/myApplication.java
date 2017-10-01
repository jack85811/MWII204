package application;


import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("store")
public class myApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	
	public myApplication() {
		singletons.add(new Controller());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
