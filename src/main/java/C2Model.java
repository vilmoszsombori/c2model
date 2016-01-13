import java.util.Enumeration;

/*
 * Created on Dec 8, 2004
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
public class C2Model {
	  public static final String vers = "0.1";
	  public static final String id = "C++ Parser";
	  public static final String PARSETREEFILE = "parsetree.txt";
	  private static String path = ".";

	  protected static java.io.PrintWriter output    = null;
	  protected static java.io.PrintWriter parsetree = null;

	  private static void msg(String s) {
	      System.out.println(id + " Version " + vers +": " + s);
	  }

	  public static void main(String args[]) {
	  	java.io.File file;
	    java.io.InputStream input;
	    String modelName = "default";
	    
	    if (args.length == 1) {
	      try {
	      	file = new java.io.File(args[0]);
	        input = new java.io.FileInputStream(file);
			msg("Reading from file " + file.getName() + " ...");
	        int i = (file.getName().lastIndexOf('.') == -1) ? file.getName().length() : file.getName().lastIndexOf('.');
			modelName = file.getName().substring(0, i);
			path = file.getParent();
			parsetree = new java.io.PrintWriter(new java.io.FileWriter(path + "/" + PARSETREEFILE, false));
	      } catch (java.io.FileNotFoundException e) {
	        msg("File " + args[0] + " not found.");
	        return;
	      } catch (java.io.IOException e) {
	        msg("File " + PARSETREEFILE + " could not be opened.");
	        return;
	      }
	    } else {
	      msg("Usage: java " + id + " {inputfile}");
	      return;
	    }

	    try {
	      CPPParser parser = new CPPParser(input);
	      SimpleNode root = CPPParser.translation_unit(modelName);
	      
	      root.dump("");
	      parsetree.close();
	      
	      switchOutput(modelName + ".m");
	      root.process(modelName);
	      closeOutput();
	      
	      //generateSFunctions();
	      
	      msg("Program parsed successfully.");
	    } catch (ParseException e) {
	      msg("Encountered errors during parse.");
	      e.printStackTrace();
	    } catch (Exception e) {
	      msg("Encountered unknown errors.");
	      e.printStackTrace();
	    }
	  }
	  
	  public static void switchOutput(String name) {
	  	try {
	  		output = new java.io.PrintWriter(new java.io.FileWriter(path + "/" + name, false));
		    msg("Writing to file " + name + " ...");
	    } catch (java.io.IOException e) {
	        msg("File " + name + " could not be opened.");
	        output = null;
	        return;
	    }    
	  }
	  
	  public static void closeOutput() {
	  	if(output != null)
		  	output.close();
	  }
	  
	  public static void println(String o) {
	  	if(output != null)
	  		output.println(o);
	  }

	  public static void print(String o) {
	  	if(output != null)
	  		output.print(o);
	  }	
	  
	  /**
	   * 
	   *
	   */
	  protected static void generateSFunctions() {
		for(Enumeration e = SymtabManager.GetGlobalScope().funcTable.elements(); e.hasMoreElements();) {
			Scope f = (Scope)e.nextElement();
			switchOutput("sfun_" + f.scopeName + ".c");
			System.out.println("\t" + f.scopeName);
			f.dumpIncludes();
			f.dumpMdlInitialization();
			f.dumpMdlOutputs();
			f.dumpMdlTerminate();
			f.dumpCustomizedFunctions();
			closeOutput();
		}
   	  }
}
