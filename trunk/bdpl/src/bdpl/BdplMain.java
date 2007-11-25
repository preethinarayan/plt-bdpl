import java.io.*;
import antlr.CommonAST;
import antlr.collections.AST;
import antlr.debug.misc.ASTFrame;

public class BdplMain{
    
    /** @return non zero on error */
    public static int test_utils()
    {
       return 0; 
    }
    /** @return non zero on error */
    public static int test_cache()
    {
        return 0;
    }
    /** @return non zero on error */
    public static int test_DataNodeBit()
    {
        return 0;
    }
    /** @return non zero on error */
    public static int test_DataNodeByte()
    {
        return 0;
    }
    /** @return non zero on error */
    public static int test_DataNodeInt()
    {
        return 0;
    }
    /** @return non zero on error */
    public static int test_DataNodeArray() throws Exception
    {
        
        DataNodeArray a = new DataNodeArray ((DataNodeAbstract)new DataNodeInt ());
        DataNodeInt in_nod =new DataNodeInt (0x1234);
        a.append_element (in_nod);
        a.append_element (in_nod);
        a.append_element (in_nod);
        System.out.println (a.get_bitsequence_value ());
        return 0;
    }
    /** @return non zero on error */
    public static int test_DataNodeStruct() throws Exception
    {
        DataNodeStruct a=new DataNodeStruct();
        a.set_child_by_name ("c1",new DataNodeInt(0x1234));
        a.set_child_by_name ("c2",new DataNodeInt(0x1234));
        a.set_child_by_name ("c3",new DataNodeInt(0x1234));
        System.out.println (a.get_bitsequence_value ());
        return 0;
    }
    
    public static void main(String[] args){
	try{
            test_utils();
            test_DataNodeArray();
            test_DataNodeStruct();
		BdplLexer lexer = new BdplLexer(new FileInputStream("x:/test/decl_test1.bdl"));
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

