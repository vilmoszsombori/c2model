/*
 * Created on Nov 12, 2004
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
public class Block {
	private String componentName;
	private String blockName;
	private String options;
	
	public Block(String componentName, String blockName, String options) {
		this.componentName = componentName;
		this.blockName = blockName;
		this.options = options;
	}
	
	public static Block create(String componentName, String blockName, String options) {
		return new Block(componentName, blockName, options);
	}
	
	public String getComponentName() { return this.componentName; }
	public String getBlockName() { return this.blockName; }
	public String getOptions() { return this.options; }
}
