import java.util.Enumeration;
import java.util.Hashtable;

public class Scope {
    public static final int FUNC 		= 5;
    public static final int STRUCT 		= 7;
    public static final int GLOBAL 		= 11;
    public static final int COMP_STMT 	= 13;
    public static final int TYPE 		= 17;
    public static final int UNKNOWN 	= 19;

	/**
     * Name of the scope (set only for class/function scopes).
     */
    String scopeName = "";

    /**
     * Parent scope. (null if it is the global scope).
     */
    Scope parent;

    /**
     * Indicates whether this is a class scope or not.
     */    
    int type = UNKNOWN;     // Indicates if this is a type.

    /**
     * (partial) table of type symbols introduced in this scope.
     */
    Hashtable typeTable  = new Hashtable();
    Hashtable funcTable	 = new Hashtable();
    Hashtable portTable	 = new Hashtable();
    Vector varTable   = new Vector();
    Vector paramTable = new Vector();
    
    BlockProperties blockProperties = new BlockProperties();
    
    /**
     * Creates a scope object with a given name.
     */    
    public Scope(String name, int type, Scope p) {
        scopeName = name != null ? name : "";
        //type = isType;
        this.type = type;
        parent = p;
    }

    /**
     * Creates an unnamed scope (like for compound statements).
     */
    public Scope(Scope p) {
        //type = false;
        parent = p;
    }

    /**
     * Inserts a name into the table to say that it is the name of a type.
     */
    public void PutTypeName(String name) {
    	if(GetType(name)==null) {
	    	//System.out.println("Simple type registered: " + name);
	    	typeTable.put(name, name);
    	}
    }

    /**
     * A type with a scope (class/struct/union).
     */
    public void PutTypeName(String name, Scope sc) {
    	if(GetType(name)==null) {    	
	    	//System.out.println("Complex type registered: " + name + " (" + sc.scopeName + ")");
	        typeTable.put(name, sc);
    	}
    }

    public Object GetType(String name) {
    	Scope l = this;
    	while(l != null) {
        	//System.out.println("Looking up "+name+" in scope "+l+" among "+l.varTable.size());
    		Object t = l.typeTable.get(name);
    		if (t != null)
    			return t;
    		else l = l.parent;
    	}
    	//System.out.println("Type lookup failed for: " + name);
    	return null;
    }

    /**
     * A variable with type and arity (Variable).
     */
    public void PutVariable(Variable var) {
    	//System.out.println("Variable registered: " + var + " in scope: " + this);
        if(var != null) varTable.add(var);
    }
    
    public boolean IsLocalVariable(Variable var) {
		for(int i=0; i<GetVariableCount(); i++)
			if (GetVariable(i).getName().equals(var))
				return true;    			
		return false;
    }
    
    public boolean IsLocalVariable(String name) {
       	Variable v = null;
   		for(int i=0; i<GetVariableCount(); i++) {
   			v = GetVariable(i);
    		if (v.getName().equals(name))
    			return true;    			
   		}
		return false;    	
    }
    
    public boolean IsVariable(Variable var) {
    	if(var != null && GetVariable(var.getName()) != null)
    		return true;
    	else
    		return false;
    }
    
    public boolean IsVariable(String name) {
    	if(GetVariable(name) != null)
    		return true;
    	else
    		return false;
    }
    
    public Variable GetVariable(String name) {
    	Scope l = this;
    	Variable v = null;
    	while(l != null) {
        	//System.out.println("Looking up "+name+" in scope "+l+" among "+l.varTable.size());
    		for(int i=0; i<l.GetVariableCount(); i++) {
    			v = l.GetVariable(i);
    			if (v.getName().equals(name))
    				return v;    			
    		}
    		l = l.parent;
    	}
    	return null;
    }
    
    public int GetVariableCount() {
    	return this.varTable.size();
    }
    
    public Variable GetVariable(int i) {
    	if(i>=0 && i<varTable.size())
    		return (Variable)varTable.get(i);
    	else
    		return null;
    }

    /**
     * A parameter.
     */
    public void PutParameter(Variable var) {
    	//System.out.println("Parameter registered: " + var + " in scope: " + this);
        if(var != null) this.paramTable.add(var);
    }
    
    public Variable GetParameter(String name) {
    	Scope l = this;
    	Variable v = null;
    	while(l != null) {
        	//System.out.println("Looking up "+name+" in scope "+l+" among "+l.varTable.size());
    		for(int i=0; i<l.GetParameterCount(); i++) {
    			v = l.GetParameter(i);
    			if (v.getName().equals(name))
    				return v;    			
    		}
    		l = l.parent;
    	}
    	return null;
    }
       
    public Variable GetParameter(int i) {
    	if(i>=0 && i<paramTable.size())
    		return (Variable)paramTable.get(i);
    	else
    		return null;
    }
    
    public int GetParameterCount() {
    	return paramTable.size();
    }

    /**
     * A function with inputs and return value.
     */
    public void PutFunction(String name, Scope sc) {
    	//System.out.println("Function registered: " + name + " in scope: " + this);
        if(name != null && name != "") funcTable.put(name, sc);
    }
    
    public Scope GetFunction(String name) {
    	Scope l = this;
    	Scope f = null;
    	while(l != null) {
        	//System.out.println("Looking up "+name+" in scope "+l+" among "+l.varTable.size());
    		f = (Scope)l.funcTable.get(name);
    		if (f != null)
    			return f;
    		else l = l.parent;
    	}
    	return null;
    }
    
    public void PutPort(String name, Integer t) {
    	Variable v = GetVariable(name);
    	if(v != null) {
	    	Integer temp = (Integer)this.portTable.get(v); 
	    	if(temp == null)
	    		this.portTable.put(v, t);
	    	else if(temp.equals(TEMP))
	    		this.portTable.put(v, t);
	    	else if((temp.equals(IN) && t.equals(OUT)) ||
	    			(temp.equals(OUT) && t.equals(IN))	)
	    		this.portTable.put(v, IN_OUT);
    	}
    }
    
    public int GetPortCount(Integer i) {
    	int j = 0;
		for(Enumeration e = portTable.keys(); e.hasMoreElements();) {
			Variable p = (Variable)e.nextElement();
			//System.out.println("\t" + p + " --> " + portTable.get(p));
			Integer temp = (Integer)portTable.get(p);
			if(temp.equals(i))
				j++;
		}
    	return j;
    }
    
    /*
    public Vector GetPortVector(Integer i) {
    	Vector v = new Vector();
		for(Enumeration e = portTable.keys(); e.hasMoreElements();) {
			Variable p = (Variable)e.nextElement();
			//System.out.println("\t" + p + " --> " + portTable.get(p));
			Integer temp = (Integer)portTable.get(p);
			if(temp.equals(i))
				v.add(p);
		}
    	return v;
    }
    */
    
    public boolean IsPort(Variable v, Integer i) {
    	return i.equals((Integer)portTable.get(v));
    }

    public boolean IsPort(Variable v) {
    	return portTable.get(v) != null;
    }
    
    /**
     * Checks if a given name is the name of a type in this scope.
     */
    public boolean IsTypeName(String name) {
        return typeTable.get(name) != null;
    }

    public Scope GetScope(String name) {
        Object sc = typeTable.get(name);

        if (sc instanceof Scope || sc instanceof ClassScope)
            return (Scope) sc;

        return null;
    }
    
    public String toString() {
    	return (parent != null ? (parent.scopeName!="" ? parent.toString() + "/" : parent.toString()) : "") + scopeName;
    }
    
    protected void dumpIncludes() {
    	Scope gs = SymtabManager.GetGlobalScope();
    	int num_outputs = GetPortCount(Scope.OUT) + GetPortCount(Scope.IN_OUT);
    	int num_inputs = num_outputs + GetPortCount(Scope.IN);
    	C2Model.println("/*@@@**************************************************************************\n" +
    							   "*******                           INCLUDES                              *******\n" +
								   "******************************************************************************/\n" +
								   "#define S_FUNCTION_NAME  sfun_" + scopeName + "\n" +
								   "#define S_FUNCTION_LEVEL 2\n\n" +
								   
								   "#include \"simstruc.h\"\n" +
								   "#include <math.h>\n" +
								   "#include \"typ.h\"\n" +
								   "#include \"variable_sign.h\"\n" +
								   "#include \"variable_can.h\"\n" +
								   "#include \"variable.h\"\n" +
								   "#include \"flags.h\"\n" +
								   "#include \"can.h\"\n\n" +
								   
								   "#define NUM_INPUTS		" + num_inputs + "\n" +
								   "#define NUM_OUTPUTS		" + num_outputs + "\n" +
								   "#define SAMPLE_TIME		0.01\n\n" + 

								   "extern void " + scopeName + "(void);\n");

    	for(int i=0, j=0; i<gs.GetVariableCount(); i++)
    		if(IsPort(gs.GetVariable(i))) 
    			C2Model.println("void " + gs.GetVariable(i).getName() + "_inputs(SimStruct *S, int_T port_num);");

    	C2Model.println("");

    	for(int i=0, j=0; i<gs.GetVariableCount(); i++)
    		if(IsPort(gs.GetVariable(i), Scope.OUT) || IsPort(gs.GetVariable(i), Scope.IN_OUT)) 
    			C2Model.println("void " + gs.GetVariable(i).getName() + "_outputs(real_T *output);");    		
    }
    
    protected void dumpMdlInitialization() {
    	Scope gs = SymtabManager.GetGlobalScope();
    	C2Model.println("\n/* Function: mdlInitializeSizes ===============================================\n" +
    			                   "* Abstract:\n" +
								   "*   Setup sizes of the various vectors.\n" +
								   "*/\n" +
								   "/*@@@**************************************************************************\n" +
								   "*******                   Configure Input/Output Variables              *******\n" +
								   "******************************************************************************/\n\n" +
								   
								   "static void mdlInitializeSizes(SimStruct *S)\n{\n" +
								   "\tu8 i;	/* Local variable */\n\n" +
								   
								   "\tssSetNumSFcnParams(S, 0);\n" +
								   "\tif (ssGetNumSFcnParams(S) != ssGetSFcnParamsCount(S)) \n" +
								   
								   "\t{\n\t\treturn; /* Parameter mismatch will be reported by Simulink */\n\t}\n\n" +
								   "\tif (!ssSetNumInputPorts(S, NUM_INPUTS)) return;\n");
    	
    	for(int i=0, j=0; i<gs.GetVariableCount(); i++)
    		if(IsPort(gs.GetVariable(i))) 
    			C2Model.println("\tssSetInputPortWidth(S, " + (j++) + ", " + gs.GetVariable(i).getWidth() + ");");


    	C2Model.println("\n\tfor (i=0; i<NUM_INPUTS; i++)\n\t{\n\t\t"+
    							   "ssSetInputPortDirectFeedThrough(S, i, 1);\n\t}\n");

    	C2Model.println("\tif (!ssSetNumOutputPorts(S,NUM_OUTPUTS)) return;\n");

    	for(int i=0, j=0; i<gs.GetVariableCount(); i++)
    		if(IsPort(gs.GetVariable(i), Scope.OUT) || IsPort(gs.GetVariable(i), Scope.IN_OUT)) 
    			C2Model.println("\tssSetOutputPortWidth(S, " + (j++) + ", " + gs.GetVariable(i).getWidth() + ");");

    	C2Model.println("\n\tssSetOptions(S, SS_OPTION_EXCEPTION_FREE_CODE);\n\n" +
    			
    						       "\tssSetNumRWork(S, 0);\n" + 
								   "\tssSetNumIWork(S, 0);\n" + 
								   "\tssSetNumPWork(S, 0);\n" + 
								   "\tssSetNumModes(S, 0);\n" + 
								   "\tssSetNumSampleTimes(S, 1);\n}\n");
    }
    
    protected void dumpMdlOutputs() {
    	Scope gs = SymtabManager.GetGlobalScope();
    	C2Model.println("/*@@@**************************************************************************\n" +
    							   "*******                   Configure Model Sample Time                   *******\n" + 
								   "******************************************************************************/\n\n" + 
								   
								   "static void mdlInitializeSampleTimes(SimStruct *S)\n" + 
								   "{\n" + 
								   "    ssSetSampleTime(S, 0, SAMPLE_TIME);\n" + 
								   "    ssSetOffsetTime(S, 0, 0.0);\n" + 
								   "}\n\n" + 
								   
								   "/*@@@**************************************************************************\n" + 
								   "*******                            Initialization                       *******\n" + 
								   "******************************************************************************/\n\n" + 
								   
								   "#define MDL_START\n" + 
								   "#if defined(MDL_START)\n" + 
								   "static void mdlStart(SimStruct *S)\n" + 
								   "{\n" + 
								   "}\n" + 
								   "#endif\n\n" + 
								   
								   "/*@@@**************************************************************************\n" + 
								   "*******                            model ouput calculation              *******\n" + 
								   "******************************************************************************/\n\n" + 
								   
								   "static void mdlOutputs(SimStruct *S, int_T tid)\n" + 
								   "{\n\n\t/* Outputs pointer*/");

    	int outPortCount = GetPortCount(Scope.OUT) + GetPortCount(Scope.IN_OUT);
    	for(int i=0; i<outPortCount; i++)
    		C2Model.println("\treal_T *Output" + i + " = ssGetOutputPortRealSignal(S," + i + ");");
    			
    	C2Model.println("\n\t/* Inputs Assignment*/");

    	for(int i=0, j=0; i<gs.GetVariableCount(); i++)
    		if(IsPort(gs.GetVariable(i))) 
    			C2Model.println("\t" + gs.GetVariable(i).getName() + "_inputs(S," + (j++) + ");");
    	    
    	C2Model.println("\n\t/* Function Call */\n\t" + scopeName + "();");

    	C2Model.println("\n\t/* Outputs Assignment*/");

    	for(int i=0, j=0; i<gs.GetVariableCount(); i++)
    		if(IsPort(gs.GetVariable(i), Scope.OUT) || IsPort(gs.GetVariable(i), Scope.IN_OUT)) 
    			C2Model.println("\t" + gs.GetVariable(i).getName() + "_outputs(Output" + (j++) + ");");

    	C2Model.println("}\n");
    }
    
    protected void dumpMdlTerminate() {
    	C2Model.println("/* Function: mdlTerminate =====================================================\n" + 
    							   " * Abstract:\n" +
								   " *    No termination needed, but we are required to have this routine.\n" +
								   " */\n" +
								   "/*@@@**************************************************************************\n" +
								   "*******                            Terminate                            *******\n" +
								   "******************************************************************************/\n\n" +
								   
								   "static void mdlTerminate(SimStruct *S)\n" +
								   "{\n" +
								   "}\n\n" +
								   
								   "#ifdef  MATLAB_MEX_FILE    /* Is this file being compiled as a MEX-file? */\n" +
								   "#include \"simulink.c\"      /* MEX-file interface mechanism */\n" +
								   "#else\n" +
								   "#include \"cg_sfun.h\"       /* Code generation registration function */\n" +
								   "#endif\n");
    }
        
    protected void dumpCustomizedFunctions() {
    	C2Model.println("/*@@@**************************************************************************\n" +
    			                   "*******                       Customized function                       *******\n" + 
								   "******************************************************************************/");
    	int in = 0, out = 0;
		for(Enumeration e = portTable.keys(); e.hasMoreElements();) {
			Variable p = (Variable)e.nextElement();
			//System.out.println("\t" + p + " --> " + portTable.get(p));
			Integer i = (Integer)portTable.get(p);
			if(i.equals(Scope.IN)) {
				in++;
				System.out.println("\t\tIN  -> " + p.getName());
				C2Model.println(p.getAsCustomizedInputFcn());
			}
			else if(i.equals(Scope.OUT)) {
				out++;
				System.out.println("\t\tOUT -> " + p.getName());
				C2Model.println(p.getAsCustomizedOutputFcn());
			}
			else if(i.equals(Scope.IN_OUT)) {
				in++; out++;
				System.out.println("\t\tIO  -> " + p.getName());
				C2Model.println(p.getAsCustomizedInputFcn());
				C2Model.println(p.getAsCustomizedOutputFcn());					
			}
			else
				System.out.println("\t\t__  -> " + p.getName());
		}
    	System.out.println("\t" + this.scopeName + " :: inputs=" + in + "; outputs=" + out);

    }

    public static final Integer IN 		= new Integer(1);
    public static final Integer OUT 	= new Integer(2);
    public static final Integer IN_OUT 	= new Integer(3);
    public static final Integer TEMP 	= new Integer(4);    
}
