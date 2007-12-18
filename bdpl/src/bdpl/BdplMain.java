import java.io.*;
import antlr.CommonAST;
import antlr.collections.AST;
import antlr.debug.misc.ASTFrame;

public class BdplMain
{
    public static void main(String[] args){
	try
        {
            InputStream input = null;
            
            if(args.length > 0)
            {
                for(int argCount = 0; argCount < args.length; argCount++){
                    if(args[argCount].equals("-h")){
                        System.out.println("================================================");
                        System.out.println("Bdpl Usage:");
                        System.out.println("java BdplMain [options] <file>");
                        System.out.println("\t -f : File specified as next argument");
                        System.out.println("\t -h : To display this help text");
                        System.out.println("================================================");
                        System.exit(0);
                    }else if(args[argCount].equals("-f")){
                        assert(args.length > argCount+1);
                        input = new FileInputStream(args[argCount+1]);
                        argCount++;
                    }else{
                        System.out.println("================================================");
                        System.out.println("Bdpl Usage:");
                        System.out.println("java BdplMain [options] <file>");
                        System.out.println("\t -f : File specified as next argument");
                        System.out.println("\t -h : To display this help text");
                        System.out.println("================================================");
                        System.exit(0);
                    }
                }
            }
            else
            {
                input = new FileInputStream("x:/test/tutorial.bdl");
            }
 
            BdplLexer lexer = new BdplLexer(input);

 	    BdplParser parser = new BdplParser(lexer);
	    parser.program();
            
            
            CommonAST parseTree = (CommonAST)parser.getAST();
            //System.out.println(parseTree.toStringList());
            //ASTFrame frame = new ASTFrame("AST for the BDPL Parser", parseTree);
            //frame.setVisible(false);


	    BdplTreeParser treeParser = new BdplTreeParser();
            treeParser.program(parseTree);
	}
        catch(Exception e)
        {
            System.err.println ("aha : "+e.getMessage ());
            return ;
            //sSystem.exit(0);
	    //System.err.println("Exception: "+e);
        }
        
    }
}

