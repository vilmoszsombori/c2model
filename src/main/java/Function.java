/*
 * Created on Nov 22, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author student02
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Function {
	protected String name;
	protected Scope sc = null;
	
	public Function() {}
	
	public Function(String name, Scope sc) { this.name = name; this.sc = sc;}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the inputs.
	 */

	public void setScope(Scope sc) {
		this.sc = sc;
	}
	
	public Scope getScope() {
		return this.sc;
	}

	public String toString() {
		return "Function[" + name + ", in = " + sc.paramTable.size() + "]";
	}	
}
