import java.io.*;

class BdplMain{
    public static void main(String[] args){
	try{
	    BdplLexer lexer = new BdplLexer(new FileInputStream("BdplTest.bdl"));
	    BdplParser parser = new BdplParser(lexer);
	    parser.file();
	}catch(Exception e){
	    System.err.println("Exception: "+e);
        }
    }
}