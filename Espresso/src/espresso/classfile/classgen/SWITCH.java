package  espresso.classfile.classgen;




import  java.io.*;




/** 
 * SWITCH - Branch depending on int value, generates either LOOKUPSWITCH or
 * TABLESWITCH instruction, depending on whether the match values (int[]) can be
 * sorted with no gaps between the numbers.
 *
 * @version 980112
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public final class SWITCH
    implements CompoundInstruction
{

    private int[] match;
    private InstructionHandle[] targets;
    private Select instruction;
    private int match_length;


    /**
   * Template for switch() constructs, if the match array can be sorted
   * in ascending order with no gaps between the numbers a TABLESWITCH
   * instruction is generated, a LOOKUPSWITCH otherwise.
   *
   * @param match array of match values (case 2: ... case 7: ..., etc.)
   * @param targets the instructions to be performed for each case
   * @param target the default target
   */
    public SWITCH (int[] match, InstructionHandle[] targets, InstructionHandle target) {
        this.match =  match;
        this.targets = targets;	// this line was not here
        match_length =  match.length;
        sort();
        if (matchIsOrdered()) instruction =  new TABLESWITCH(match, targets, target); 
        else instruction =  new LOOKUPSWITCH(match, targets, target);
    }


// not as fancy a sort, but at least it works

	void sort () {		
		int x = 0;
		int j = 0;
	    InstructionHandle h2;	
		for (int i =1; i < match.length; i++ ) {
			x = match[i];
			h2 = targets[i];
			j = i-1;
		  	while ( j >= 0 && x < match[j] ) {
				match[j+1] = match[j];
				targets[j+1] = targets[j];
				j--;
		  	}
		  	match[j+1] = x;
		  	targets[j+1] = h2;
		} 	
	}



    /**
   * @return match is sorted in ascending order?
   */
    private final boolean matchIsOrdered () {
        for (int i =  1; i < match_length; i++) if (match[i] != match[i - 1] + 1) return  false;
        return  true;
    }


    public final InstructionList getInstructionList () {
        return  new InstructionList(instruction);
    }


    public final Instruction getInstruction () {
        return  instruction;
    }

}

