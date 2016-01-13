/*
 * Created on Nov 25, 2004
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
public class Vector extends java.util.Vector {
	public Vector() {
		super();
	}
	
	public boolean equals(Object o) {
		if(o instanceof Vector && ((Vector)o).size() == this.size()) {
			for(int i=0; i<this.size(); i++)
				if (!this.get(i).equals(((Vector)o).get(i)))
					return false;
			return true;			
		}
		return false;
	}
	
	public String toString() {
		if(isEmpty())
			return "";
		else {
			String out = "";
			for(int i=0; i<size()-1; i++)
				out += get(i).toString() + ",";
			out += get(size()-1).toString();
			return out;
		}			
	}
}
