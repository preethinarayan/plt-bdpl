import java.io.*;
import antlr.CommonAST;
import antlr.collections.AST;
import antlr.debug.misc.ASTFrame;

public class BdplMain{
    public static void main(String[] args){
	try{
            DataNodeArray a = new DataNodeArray((DataNodeAbstract)new DataNodeInt());
            DataNodeInt in_nod =new DataNodeInt(0x1234);
            a.append_element (in_nod);
            a.append_element (in_nod);
            a.append_element (in_nod);
            
            System.out.println (a.get_bitsequence_value ());
		BdplLexer lexer = new BdplLexer(new FileInputStream("x:/test/prog3.bdl"));
	   // BdplLexer lexer = new BdplLexer(new FileInputStream("C:/Users/akshay/Documents/school/plt/trunk/bdpl/test/prog3.bdl"));
	    BdplParser parser = new BdplParser(lexer);
	    parser.program();
            
            CommonAST parseTree = (CommonAST)parser.getAST();
            System.out.println(parseTree.toStringList());
            
            ASTFrame frame = new ASTFrame("AST for the BDPL Parser", parseTree);
            frame.setVisible(true);

	    BdplTreeParser treeParser = new BdplTreeParser();
            treeParser.program(parseTree);
	}catch(Exception e){
	    System.err.println("Exception: "+e);
        }
    }
}

