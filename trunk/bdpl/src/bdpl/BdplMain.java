import java.io.*;
import antlr.CommonAST;
import antlr.collections.AST;
import antlr.debug.misc.ASTFrame;

public class BdplMain{
    public static void main(String[] args){
	try{
	    BdplLexer lexer = new BdplLexer(new FileInputStream("x:/test/prog3.bdl"));
	    BdplParser parser = new BdplParser(lexer);
	    parser.file();
            
            CommonAST parseTree = (CommonAST)parser.getAST();
            System.out.println(parseTree.toStringList());
            
            ASTFrame frame = new ASTFrame("AST for the BDPL Parser", parseTree);
            frame.setVisible(true);
	}catch(Exception e){
	    System.err.println("Exception: "+e);
        }
    }
}

