/*
 * BdplFile.java
 *
 */

/**
 *
 * @author Preethi Narayan(pn2156)
 */
import java.io.*;
import java.util.BitSet;
public class BdplFile {
    private RandomAccessFile _file;
    private BitSet _file_data;
    private byte _byte_read;
    private int _bit_set_pointer;
    /**
     * Creates a new instance of BdplFile
     */
    public BdplFile(String _filename) throws BdplException {
       _bit_set_pointer=0; 
       _file_data=new BitSet();
       try{
        _file=new RandomAccessFile(_filename,"r");
       do{
          _byte_read=_file.readByte();
          int l=0;
          while(l<8){
             int bool=_byte_read%2;
             _byte_read/=2;
             if(bool==0)
                _file_data.set(_bit_set_pointer,false);
             else
                _file_data.set(_bit_set_pointer,true);
             _bit_set_pointer++;
             l++;
         }            
         }while(_byte_read!=-1);
       }
       catch(IOException e){
           throw new BdplException(e.getMessage());
       }
       
        
    }
    
    
    /**
     *method to read n bits from the bit set
     */
    public BitSet readnbits(int n) throws BdplException{
        BitSet _bits_read=new BitSet();
        for(int i=0;i<n;i++){
            if(_bit_set_pointer<_file_data.size())
            {
               boolean bool=_file_data.get(_bit_set_pointer);
               if(bool){
                   _bits_read.set(i,1);
               }
               else{
                   _bits_read.set(i,0);
               }
               _bit_set_pointer++;
            }
            else
                throw new BdplException("Reached end of file");
        }
        return _bits_read;
    }
    
    /**
     *method to read n bytes from the bit set
     */
    public byte[] readnbytes(int n){
        byte[] _bytes_read=new byte[n];
        boolean[] bools=new boolean[8];
        for(int i=0;i<n;i++){
            if(_bit_set_pointer<_file_data.size())
            {
               for(int k=0;k<8;k++){
                   bools[i]=_file_data.get(_bit_set_pointer);
                  _bit_set_pointer++;
               }
               for(int j=7;j>0;j--){
                   /*if(bool[j]){
                  _bytes_read[i]=1;
                  */
                   }
               }
            }
            else
                throw new BdplException("Reached end of file");
        }
        return _bytes_read;
                
    }
    
    /**
     *method to check for the remaining number of bits in the bitset
     */
    public int checkremainingbits(){
        
        
    }
    /**
     *method to check for the remaining number of bytes
     */
    public int checkremainingbytes(){
        
    }
    /**
     *method to check for the end of the bitset
     */
    public boolean endoffile(){
        if(_bit_set_pointer==-1)
            return true;
        else return false;
        
    }
}
