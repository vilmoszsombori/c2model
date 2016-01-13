/*
 * Created on Dec 13, 2004
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
public class BlockProperties {
    public static final int IN_X  = 100,  IN_Y  = 20; 
    public static final int OUT_X = 1230, OUT_Y = 20; 
    public static final int EXP_X = 800,  EXP_Y = 20; 
    public static final int ST_X  = 900,  ST_Y  = 20; 

    int inX  = IN_X,  inY  = IN_Y;
    int expX = EXP_X, expY = EXP_Y;
    int stX  = ST_X,  stY  = ST_Y;
    int outX = OUT_X, outY = OUT_Y;
    
    int count = 0;    

    public void resetPositions(boolean all) {
        inX  = IN_X;
        expX = EXP_X; 
        stX  = ST_X;  
        outX = OUT_X; 
        if (all) {
        	  inY  = IN_Y;
        	  expY = EXP_Y;
        	  stY  = ST_Y;
        	  outY = OUT_Y;
        }
        else {
          	int max1 = Math.max(inY, outY);
          	int max2 = Math.max(stY, expY);
          	int max3 = Math.max(max1, max2);
          	inY = outY = expY = stY = max3;
        }
    }    
}
