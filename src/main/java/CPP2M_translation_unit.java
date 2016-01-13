/* Generated By:JJTree: Do not edit this line. CPP2M_translation_unit.java */

public class CPP2M_translation_unit extends SimpleNode {
  public CPP2M_translation_unit(int id) {
    super(id);
  }

  public CPP2M_translation_unit(CPPParser p, int id) {
    super(p, id);
  }

  public String process(String prefix) throws Exception {
  	int n = this.jjtGetNumChildren();
  	//prefix = scope.toString();
    
  	println("% Script file automatically generated from C\n");
    println("new_system(\'" + prefix + "\')");
    println("load_system(\'" + prefix + "\')\n");
    
	for (int i = 0; i < n; ++i) {
		SimpleNode c = (SimpleNode)children[i];
		if (c != null)
			c.process(prefix);
	}
	
    println("\nsave_system(\'" + prefix + "\')");
    println("close_system(\'" + prefix + "\')");

  	return "";
  }

}