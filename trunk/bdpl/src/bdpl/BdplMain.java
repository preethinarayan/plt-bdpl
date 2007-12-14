import java.io.*;
import antlr.CommonAST;
import antlr.collections.AST;
import antlr.debug.misc.ASTFrame;

public class BdplMain
{
    /** @return non zero on error */
    public static int test_utils()
    {
       return 0; 
    }
    /** @return non zero on error */
    public static int test_cache()
    {  
        Cache _b_cache =new Cache(64);
        Cache _bobj_cache=new Cache(_b_cache);
        System.out.println("***Cache Testing***");
         if(_bobj_cache.is_valid ())
        {
            System.out.println( _b_cache.as_string());
        }
    
        return 0;
    }
    /** @return non zero on error */
    public static int test_DataNodeBit()
    {  
        DataNodeInt in_nod =new DataNodeInt (0x1234);
        
        DataNodeBit b=new DataNodeBit(1);
        System.out.println("***DataNodeBit Testing***");
        System.out.println(b.get_type_name());
        System.out.println(b.get_bitsequence_value());
        System.out.println(b.get_int_value());
        System.out.println(b._fieldsize);
        System.out.println(b._bitsize);
        return 0;
    }
    /** @return non zero on error */
    public static int test_DataNodeByte()throws Exception
    {
        DataNodeArray a1=new DataNodeArray((DataNodeAbstract)new DataNodeByte ());
        DataNodeArray a2=new DataNodeArray((DataNodeAbstract)new DataNodeBit ());
        DataNodeByte b_node = new DataNodeByte(0X64);
        DataNodeBit b1=new DataNodeBit(1);
        DataNodeBit b2=new DataNodeBit(0);
        DataNodeBit b3=new DataNodeBit(1);
       
       a1.append_element(b_node);
       a1.append_element(b_node);
       a2.append_element(b1);
       a2.append_element(b2);
       a2.append_element(b3);
       
       
       System.out.println("***DataNodeByte Testing***");
       System.out.println(b_node.get_type_name());
       System.out.println(b_node.get_bitsequence_value());//????
       System.out.println(b_node.get_int_value());
       System.out.println(b_node._fieldsize);
       System.out.println(b_node._bitsize);
        
      System.out.println("***DataNodeByte Testing with Array***");
       System.out.println("!!!Array 1 Size!!!"+a1.get_size());
       System.out.println("Array 1 Element Type"+a1.get_type_name());
       System.out.println("!!!!Array 1 Bit Sequence!!!!"+a1.get_bitsequence_value());
       System.out.println("!!!Array 2 Size!!!"+a2.get_size());
       System.out.println("!!!!Array 2 Bit Sequence!!!!"+a2.get_bitsequence_value());//not showing the sequence
       System.out.println("Array 2 Elements"+a2.get_element(0).get_type_name());
       //System.out.println("Array 2 elements"+(a2.get_element(1)));
       //System.out.println("Array 1 Elements"+a1.get_element(2));
       return 0;
    }
    /** @return non zero on error */
    public static int test_DataNodeInt()
    {
        DataNodeInt d_nodeint=new DataNodeInt(18);
        DataNodeInt d_nodeint1=new DataNodeInt(d_nodeint);
        System.out.println("***DataNodeInt Testing***");
        System.out.println(d_nodeint.get_bitsequence_value());
        System.out.println(d_nodeint.get_type_name());
        System.out.println(d_nodeint.get_int_value());
        System.out.println(d_nodeint._bitsize);
        System.out.println(d_nodeint._fieldsize);
                
        
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
        System.out.println("***DataNodeArray Testing***");
        System.out.println (a.get_bitsequence_value ());
        System.out.println(a.get_bit_size());
        System.out.println(a.get_int_value());
        System.out.println(a.get_type_name());
        System.out.println(a.get_size());
        System.out.println(a.get_offset());
     
        System.out.println(a.get_element(2));//Is it correct???DataNodeInt@1efa1f8
        System.out.println(a.get_element(1));//Is it correct???DataNodeInt@16729a9
        a.set_offset(2);
        System.out.println(a.get_int_value());


        return 0;
    }
    /** @return non zero on error */
    public static int test_DataNodeStruct() throws Exception
    {
        DataNodeStruct str=new DataNodeStruct();
        DataNodeArray arr = new DataNodeArray ((DataNodeAbstract)new DataNodeInt ());
        DataNodeInt in_nod =new DataNodeInt (0x1234);
        
        arr.append_element (in_nod);
        arr.append_element (in_nod);
        arr.append_element (in_nod);
        str.set_child_by_name ("c1",new DataNodeByte(0x1234));
        str.set_child_by_name ("c2",new DataNodeInt(0x1234));
        str.set_child_by_name ("c3",new DataNodeInt(0x1234));
        str.set_child_by_name ("arr1",arr);
        DataNodeStruct str1=new DataNodeStruct();
        str1.set_child_by_name ("str1",str);
        
        return 0;
    }
    
    public static void main(String[] args){
	try
        {
            InputStream input;
            if(args.length == 1)
            {
                input = new FileInputStream(args[0]);
            }
            else
            {
                input = new FileInputStream("x:/test/unl_arr_test1.bdl");
            }
            //test_utils();
            //test_DataNodeArray();

            BdplLexer lexer = new BdplLexer(input);


            //test_DataNodeStruct();
            //BdplLexer lexer = new BdplLexer(new FileInputStream("x:/test/decl_test1.bdl"));

	   // BdplLexer lexer = new BdplLexer(new FileInputStream("C:/Users/akshay/Documents/school/plt/trunk/bdpl/test/prog3.bdl"));
	    BdplParser parser = new BdplParser(lexer);
	    parser.program();
            
            CommonAST parseTree = (CommonAST)parser.getAST();
            //System.out.println(parseTree.toStringList());
            
            ASTFrame frame = new ASTFrame("AST for the BDPL Parser", parseTree);
            frame.setVisible(true);

	    BdplTreeParser treeParser = new BdplTreeParser();
            treeParser.program(parseTree);           
            
	}
        catch(Exception e)
        {
            e.printStackTrace ();
	    System.err.println("Exception: "+e);
        }
    }
}

