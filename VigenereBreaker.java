import java.util.*;
import edu.duke.*;
import java.io.*;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        //REPLACE WITH YOUR CODE
        StringBuilder ans = new StringBuilder();
        for(int i=whichSlice;i<message.length();i+=totalSlices){
            ans.append(message.charAt(i));
        }
        return ans.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        //WRITE YOUR CODE HERE
        for(int i=0;i<klength;i++){
           String s=sliceString(encrypted,i,klength);
           CaesarCracker cc = new CaesarCracker(mostCommon);
           key[i]=cc.getKey(s);
        }
        return key;
    }

    public void breakVigenere () {
        //WRITE YOUR CODE HERE
       FileResource fr = new FileResource();
       String encrypted = fr.asString();
        //int[] key = tryKeyLength(emessage,5,'e');
       HashMap<String,HashSet<String>> languages = new HashMap<String,HashSet<String>>();
       DirectoryResource dr = new DirectoryResource();
       for(File F:dr.selectedFiles()){
         FileResource f = new FileResource(F);
         HashSet<String>Dictionary=readDictionary(f);
         String lang = F.getName();
         languages.put(lang,Dictionary);
         System.out.println(lang+" dictionary is created with "+Dictionary.size()+" words");;
       }        
       // VigenereCipher vc = new Vigener   eCipher(key);
        String s = breakForAllLangs(encrypted,languages);
        System.out.println(s+"\n");
    }
    
    public HashSet<String> readDictionary(FileResource fr){
       HashSet<String> Dictionary = new HashSet<String>();
       for(String word : fr.lines()){
           if(!Dictionary.contains(word.toLowerCase()))
                 Dictionary.add(word.toLowerCase());
        }
       return Dictionary; 
    }
    
    public int countWords(String message,HashSet<String> Dictionary){
       int count=0;
       for(String word : message.split("\\W+")){
         if(Dictionary.contains(word.toLowerCase()))
          count++;
        }
       return count; 
    }  
    
    public String breakForLanguage(String encypted,HashSet<String> Dictionary){
      int max=0;
      int kmax=1;
      char ch=mostCommonCharIn(Dictionary);
      for(int k=1;k<=100;k++){
        int[] key=tryKeyLength(encypted,k,ch);
        VigenereCipher vc = new VigenereCipher(key);
        String s = vc.decrypt(encypted);
        int c=countWords(s,Dictionary);
        if(max<c)
          { max=c;
            kmax=k;
            }
        }      
      int[] key=tryKeyLength(encypted,kmax,ch);
      //System.out.print("{ ");
      //for(int i=0;i<key.length;i++)
      //     System.out.print(key[i]+" , ");
      //System.out.println(" } :"+kmax);     
      //System.out.println("Maximum Words Found : "+max);
      VigenereCipher vc = new VigenereCipher(key);
      String s = vc.decrypt(encypted);
      return s;
    }
    
    public char mostCommonCharIn(HashSet<String> Dictionary){
       int max=0;
       String alpha="abcdefghijklmnopqrstuvwxyz";
       int[] count = new int[26];
       for(String word:Dictionary){
          String w = word.toLowerCase();
          for(int i=0;i<word.length();i++){
             int idx = alpha.indexOf(w.charAt(i));
             if(idx!=-1)
                count[idx]++;
            }
            
        }
       
       int im=0;
       for(int i=0;i<26;i++){
          if(max<count[i])
            { max=count[i];
              im=i;
            }
        } 
       
       return alpha.charAt(im);
    }
    
    public String breakForAllLangs(String encrypted,HashMap<String,HashSet<String>>languages){
       int max=0;
       String lmax="English";
       for(String lang:languages.keySet()){
         String message = breakForLanguage(encrypted,languages.get(lang));
         int count = countWords(message,languages.get(lang));
         if(max<count)
         { max = count;
           lmax = lang; 
            }
        }
       String message = breakForLanguage(encrypted,languages.get(lmax));
       int count = max; 
       System.out.println("Language :"+lmax+" Counts :"+count);
       //System.out.println(message+"\n");
       return message;
    }
    
}
