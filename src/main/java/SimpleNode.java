/* Generated By:JJTree: Do not edit this line. SimpleNode.java */
public class SimpleNode implements Node {  
  protected Node parent;
  protected Node[] children;
  protected int id; 
  protected String _token;
  
  /**
   * Helpers for keeping track of scope and block-numbering
   */
  protected Scope scope;

  public SimpleNode(int i) {
    id = i;
    this.scope = SymtabManager.GetCurScope();  
  }

  public SimpleNode(CPPParser p, int i) {
    this(i);
    this.scope = SymtabManager.GetCurScope();  
  }

  public void jjtOpen() {
  }

  public void jjtClose() {
  }
  
  public void jjtSetParent(Node n) { parent = n; }
  public Node jjtGetParent() { return parent; }

  public void jjtAddChild(Node n, int i) {
    if (children == null) {
      children = new Node[i + 1];
    } else if (i >= children.length) {
      Node c[] = new Node[i + 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = n;
  }

  public Node jjtGetChild(int i) {
    return children[i];
  }

  public int jjtGetNumChildren() {
    return (children == null) ? 0 : children.length;
  }

  /* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

  public String toString() { 
  	return CPPParserTreeConstants.jjtNodeName[id] + 
  		   ((this.jjtGetNumChildren() != 0) ? " [" + this.jjtGetNumChildren() + "]" : "") +
		   ((_token != null && _token != "") ? " == " + _token : ""); 
  }
  public String toString(String prefix) { return prefix + toString(); }

  /* Override this method if you want to customize how the node dumps
     out its children. */

  public void dump(String prefix) {
    C2Model.parsetree.println(toString(prefix));
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
		SimpleNode n = (SimpleNode)children[i];
		if (n != null) {
		  n.dump(prefix + " ");
		}
      }
    }
  }   
 
  public String process(String prefix) throws Exception {
  	int n = this.jjtGetNumChildren();
  	prefix = scope.toString();  	
  	if (n==1)
  		return ((SimpleNode) this.jjtGetChild(0)).process(prefix);
  	else {
  		for (int i = 0; i < n; ++i) {
  			SimpleNode c = (SimpleNode)children[i];
  			if (c != null)
  				c.process(prefix);
  		}
  	}
  	return "";
  }
  
  protected String add_block(String prefix, String componentName, String blockName, String options, boolean showName) {
  	int count = this.scope.blockProperties.count++;
  	println("b = add_block(" + componentName + ", '" + prefix + "/" + blockName + count + "'" 
			+ ((options == null || options == "") ? "" : ", " + options)
			+ (showName ? "" : ", 'ShowName', 'off'" )
			+ ");" );
  	return blockName + count;
  }
  
  protected void add_line(String prefix, String from, String to, String name) {
  	println("l = add_line('" + prefix + "', '" + from + "', '" + to + "', 'autorouting', 'on');");
  	if(name != null && name != "")
  		println("set_param(l, 'name', '" + name + "');");
  }
  
  protected void println(String s) {
  	C2Model.println(s);
  }
  
  protected void print(String s) {
  	C2Model.print(s);
  }  
}

