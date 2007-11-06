import java.io.*;

public class BdplMain{
    public static void main(String[] args){
	try{
	    BdplLexer lexer = new BdplLexer(new FileInputStream("../../Users/akshay/Documents/school/plt/trunk/bdpl/test/prog3.bdl"));
	    BdplParser parser = new BdplParser(lexer);
	    parser.file();
	}catch(Exception e){
	    System.err.println("Exception: "+e);
        }
    }
}

