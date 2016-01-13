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
public class Variable {
	private String name;
	private String type = null;
	private Vector arity = new Vector();
	private boolean pointer = false;
	private Scope sc = null;
	
	public Variable(String name, Scope sc, String type, boolean pointer) {
		this.name = name;
		this.type = type;
		this.pointer = pointer;
		this.sc = sc;
	}

	public int getArity() {
		return arity.size();
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getArity(int i) {
		return ((Integer)arity.get(i)).intValue();
	}
	
	public void addArity(Integer i) {
		arity.add(i);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isPointer() {
		return this.pointer;
	}
	
	public Scope getScope() {
		return this.sc;
	}
	
	public void setScope(Scope sc) {
		this.sc = sc;
	}
	
	public String toString() {
		return "Variable[" + name + arity + ", " + type + (pointer ? ", PTR]" : "]");
	}
	
	public String getAsCustomizedInputFcn() {
		String s = "";
		/*
		Object _type = sc.GetType(this.type);
		if(_type == null || _type instanceof String) {
			System.out.println("getAsCustomizedInputFcn :: " + this.name + " is simple type.");
			return "";
		}
		*/		
		s += "\nvoid " 
		  + this.name + "_inputs(SimStruct *S, int_T port_num)\n{"
		  + "\n\t/* Inputs pointer */"
		  + "\n\tInputRealPtrsType input  = ssGetInputPortRealSignalPtrs(S,port_num);";
		Vector v = this.toStringVector(true);
		for(int i=0; i<v.size(); i++)
			s += "\n\t" + (String)v.get(i) + " *input[" + i + "];";
		s += "\n}"; 
		return s;
	}

	public String getAsCustomizedOutputFcn() {
		String s = "";
		/*
		Object _type = sc.GetType(this.type);
		if(_type == null || _type instanceof String) {
			System.out.println("getAsCustomizedOutputFcn :: " + this.name + " is simple type.");
			//return "";
		}	
		*/	
		s += "\nvoid " + this.name + "_outputs(real_T *output)\n{";
		Vector v = this.toStringVector(false);
		for(int i=0; i<v.size()-1; i++)
			s += "\n\t*output++	= " + (String)v.get(i) + ";";
		s += "\n\t*output		= " + (String)v.get(v.size()-1) + ";";
		s += "\n}"; 
		return s;
	}
	
	protected int getWidth() {
		Vector v = toStringVector(false);
		if(v!=null)
			return v.size();
		return 0;
	}
	
	protected Vector toStringVector(boolean eqCast) {
		Vector v = new Vector();
		Object _type = sc.GetType(this.type);
		if(_type == null || _type instanceof String) {
			if(getArity() != 0)
				for(int i=0; i<getArity(0); i++)
					v.add(this.name + "[" + i + "]" + (eqCast ? "		= (" + this.type + ") " : ""));				
			else				
				v.add(this.name + (eqCast ? "		= (" + this.type + ") " : ""));
		}
		else {
			Variable var = null;
			for(int e=0; e<((Scope)_type).GetVariableCount(); e++) {
				var = ((Scope)_type).GetVariable(e);
				if(var!=null) {
					Vector temp = var.toStringVector(eqCast);
					for(int i=0; i<temp.size(); i++)
						v.add(this.name + "." + (String)temp.get(i));
				}				
			}
		}
		return v;
	}
		
	protected void dumpAsInput(Scope sc) {
		String prefix = sc.toString();
		int startY = sc.blockProperties.expY;
		String busSelector = getBusSelector(sc, "");   				
		int endY = sc.blockProperties.expY;
		double location = Math.floor((startY + endY)/2);
		C2Model.println("add_block('built-in/InPort', '" + prefix + "/" + getName() + " 0', " +  
							        "'Position', [" + sc.blockProperties.inX + " " 
								                    + (location-7) + " " 
												    + (sc.blockProperties.inX+30) + " " 
													+ (location+8) + "]," +
									" 'ShowName', 'off');"); 
		//scope.blockProperties.inY += 50;
		sc.blockProperties.resetPositions(false);
		C2Model.println("l = add_line('" + prefix + "', '" + getName() + " 0/1', 'i_" + busSelector + "/1', 'autorouting', 'on');");
  		C2Model.println("set_param(l, 'name', '" + getName() + "');");
	}

	protected void dumpAsOutput(Scope sc) {
		String prefix = sc.toString();
		int startY = sc.blockProperties.expY;
		String busCreator = getBusCreator(sc, "");   				
		int endY = sc.blockProperties.expY;
		double location = Math.floor((startY + endY)/2);
		C2Model.println("add_block('built-in/OutPort', '" + prefix + "/" + getName() + " 1', " +  
							        "'Position', [" + sc.blockProperties.outX + " " 
								                    + (location-7) + " " 
												    + (sc.blockProperties.outX+30) + " " 
													+ (location+8) + "]," +
									" 'ShowName', 'off');"); 
		//scope.blockProperties.inY += 50;
		sc.blockProperties.resetPositions(false);
		C2Model.println("l = add_line('" + prefix + "', '" + busCreator + "/1', '" + getName() + " 1/1', 'autorouting', 'on');");
  		C2Model.println("set_param(l, 'name', '" + busCreator + "');");
	}
	
	private String getBusSelector(Scope scope, String prefix) {
		Object _type = this.type != null ? this.sc.GetType(this.type) : null;
		String fullyScopedName = prefix + this.name;
		//System.out.println(fullyScopedName);
		if(_type == null || _type instanceof String) {			
			String id = (new String(fullyScopedName)).replace(".", "_");
			id = "i_" + id;
			//correct it in the near future
			id = id.substring(0, Math.min(31, id.length()));
			C2Model.println("add_block('built-in/Goto', '" + scope.toString()+ "/i_" + fullyScopedName + "', " + 
			        			"'Position', [" + scope.blockProperties.outX + " " 
										+ (scope.blockProperties.outY) + " " 
										+ (scope.blockProperties.outX+15+(id.length()*7)) + " " 
										+ (scope.blockProperties.outY+20) + "]" 
					            + ", 'GotoTag', '" + id + "', 'ShowName', 'off');");
			scope.blockProperties.outY += 30;
		}
		else {
			Vector results = new Vector();			
			int startY = scope.blockProperties.outY;
			String signals = "";
			scope.blockProperties.expX += 150;
			for(int e=0; e<((Scope)_type).GetVariableCount(); e++) {
				Variable var = ((Scope)_type).GetVariable(e);
				String temp = var.getBusSelector(scope, fullyScopedName + ".");
				if(temp!="" && temp!=null)
					results.add(temp.replace(".", ":"));
			}
			scope.blockProperties.expX -= 150;
			int endY = startY + results.size()*30 - 10;
			C2Model.println("add_block('built-in/BusSelector', '" + scope.toString()+ "/i_" + fullyScopedName + "', " + 
	        					"'Position', [" + scope.blockProperties.expX + " " 
										+ (startY) + " " 
										+ (scope.blockProperties.expX+5) + " " 
										+ (endY) + "], " 
							    + "'OutputSignals', '" + results.toString() + "', "
								+ "'ShowName', 'off');");
			scope.blockProperties.expY = endY;
			for(int e=0; e<results.size(); e++) {
				String name = (String)results.get(e);
				String temp = (new String(name)).replace(":", ".");
				C2Model.println("l = add_line('" + scope.toString()+ "', 'i_" + fullyScopedName + "/" + (e+1) + "', 'i_" + temp + "/1', 'autorouting', 'on');"); 
		  		C2Model.println("set_param(l, 'name', '" + name + "');");
			}
		}		
		return fullyScopedName;
	}
	
	protected String getBusCreator(Scope scope, String prefix) {
		Object _type = this.type != null ? this.sc.GetType(this.type) : null;
		String fullyScopedName = prefix + this.name;
		//System.out.println(fullyScopedName);
		if(_type == null || _type instanceof String) {			
			String id = (new String(fullyScopedName)).replace(".", "_");
			id = "o_" + id;
			//correct it in the near future
			id = id.substring(0, Math.min(31, id.length()));
			C2Model.println("add_block('built-in/From', '" + scope.toString()+ "/" + fullyScopedName + "', " + 
			        			"'Position', [" + scope.blockProperties.inX + " " 
										+ (scope.blockProperties.inY) + " " 
										+ (scope.blockProperties.inX+15+(id.length()*7)) + " " 
										+ (scope.blockProperties.inY+20) + "]" 
					            + ", 'GotoTag', '" + id + "', 'ShowName', 'off');");
			scope.blockProperties.inY += 30;
		}
		else {
			Vector results = new Vector();			
			int startY = scope.blockProperties.inY;
			String signals = "";
			scope.blockProperties.expX -= 150;
			for(int e=0; e<((Scope)_type).GetVariableCount(); e++) {
				Variable var = ((Scope)_type).GetVariable(e);
				String temp = var.getBusCreator(scope, fullyScopedName + ".");
				if(temp!="" && temp!=null)
					results.add(temp.replace(".", ":"));
			}
			scope.blockProperties.expX += 150;
			int endY = startY + results.size()*30 - 10;
			C2Model.println("add_block('built-in/BusCreator', '" + scope.toString()+ "/" + fullyScopedName + "', " + 
	        					"'Position', [" + scope.blockProperties.expX + " " 
										+ (startY) + " " 
										+ (scope.blockProperties.expX+5) + " " 
										+ (endY) + "], " 
							    + "'Inputs', '" + results.toString() + "', "
								+ "'ShowName', 'off');");
			scope.blockProperties.expY = endY;
			for(int e=0; e<results.size(); e++) {
				String name = (String)results.get(e);
				String temp = (new String(name)).replace(":", ".");
				C2Model.println("l = add_line('" + scope.toString()+ "', '" + temp + "/1', '" + fullyScopedName + "/" + (e+1) + "', 'autorouting', 'on');");
		  		C2Model.println("set_param(l, 'name', '" + name + "');");
			}
		}		
		return fullyScopedName;
	}	
}
