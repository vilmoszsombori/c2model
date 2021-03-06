/* Generated By:JJTree: Do not edit this line. CPP2M_constant.java */

public class CPP2M_constant extends SimpleNode {
  public CPP2M_constant(int id) {
    super(id);
  }

  public CPP2M_constant(CPPParser p, int id) {
    super(p, id);
  }

  public String process(String prefix) throws Exception {
  	prefix = scope.toString(); 	
  	if (_token != null) {
  		String port = (_token.startsWith("0x") || _token.startsWith("0X") ? "h" + _token.substring(2) : _token);
		String out = add_block(prefix, "'built-in/Constant'", "Constant ",
						"'Value', '" + port + "', " + 
				        "'Position', [" + (scope.blockProperties.inX+50) + " " 
					                + (scope.blockProperties.inY-5) + " " 
									+ (scope.blockProperties.inX+100) + " " 
									+ (scope.blockProperties.inY+25) 
								    + "], 'BackgroundColor', 'lightBlue'", 
					false);
		scope.blockProperties.inY += 50;
		return out;
  	}
  	return "";
  }
}
