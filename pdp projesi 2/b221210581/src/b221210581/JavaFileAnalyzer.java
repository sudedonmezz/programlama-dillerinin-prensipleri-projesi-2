package b221210581;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
*
* @author sude dönmez 
* @since 2.04.2024
* <p>
* kod satır sayısı,javadoc satır sayısı, yorum satır sayısı, fonksiyon satır sayısı, total kod satır sayısı, ve yorum sapma yuzdesinin
* hesaplandığı class
* </p>
*/


public class JavaFileAnalyzer {

	public static void analyzeJavaFile(File file) {
        boolean classFound = false;

        
        int totalLines = countTotalLines(file);
        int codeLines = countCodeLines(file);
        int commentLines = commentLines(file);
        int javadocLines = countJavadocLines(file);
        int functionsCount = countFunctionsInFile(file);

        double yg = ((double)(javadocLines + commentLines) * 0.8) / functionsCount;
        double yh = ((double)codeLines / functionsCount) * 0.3;


        double yorumSapma = ((100 * yg) / yh) - 100;
        String formattedYorumSapma = String.format("%.2f", yorumSapma);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.contains("class ") && line.contains("{")) {
                    classFound = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (classFound) {
            System.out.println("Sınıf: " + file.getName());
            System.out.println("Javadoc Satır Sayısı: " + javadocLines);
            System.out.println("Yorum Satır Sayısı: " + commentLines);
            System.out.println("Kod Satır Sayısı: " + codeLines);
            System.out.println("LOC: " + totalLines);
            System.out.println("Fonksiyon Sayısı: " + functionsCount);
            System.out.println("Yorum Sapma Yüzdesi: % " + formattedYorumSapma);
            System.out.println("-----------------------------------------");
        }
    }
	
	
	
	 private static int countFunctionsInFile(File file) {
	        int functionCount = 0;
	        boolean inFunction = false;

	        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                line = line.trim();
	                // Fonksiyon veya arayüz tanımının başladığını belirten durumu kontrol eder
	                if (!inFunction && (line.startsWith("public") || line.startsWith("private") || line.startsWith("protected") || line.contains("implements"))) {
	                    functionCount++;
	                    inFunction = true;
	                } else if (inFunction && line.endsWith("}")) {
	                    inFunction = false;
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return functionCount;
	    }
	    
	    
	    
	    private static int countJavadocLines(File file)
	    {
	    	int javadocLines=0;
	    	boolean inJavadoc=false;
	    	
	    	 try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	             String line;
	             while ((line = reader.readLine()) != null) {
	                
	                 line = line.trim();
	                 // Javadoc yorumlarını kontrol eder
	                 if (line.startsWith("/**")) {
	                     inJavadoc = true;
	                     continue;
	                 }

	                 // Javadoc yorumu içinde mi kontrol eder
	                 if (inJavadoc) {
	                     if (line.endsWith("*/")) {
	                         // Javadoc yorumunun son satırı olduğunda içindeki satırları hesaba katmaz
	                         inJavadoc = false;
	                     } else {
	                         // Eğer javadoc yorumunun içindeyiz ve satır /** veya */ ile bitmiyorsa
	                         // bu satırı javadoc yorumu olarak kabul ederek satır sayısını arttırır
	                         javadocLines++;
	                     }
	                     continue;
	                 }

	                
	             }
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	    	 
	    	 return javadocLines;
	    }
	    
	    private static int commentLines(File file) {
	        int commentLines = 0;
	        boolean inBlockComment = false;

	        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                line = line.trim(); 

	                // Blok yorum içinde mi kontrol eder
	                if (inBlockComment) {
	                    if (line.contains("*/")) {
	                        inBlockComment = false;
	                        commentLines++;     
	                        line = line.substring(line.indexOf("*/") + 2).trim();
	                    }
	                    continue;
	                }

	                // Eğer satırın içinde yorum işaretleri varsa ancak bu yorum işaretleri
	                // bir string içinde yer alıyorsa bu satırı yorum satırı olarak saymaz
	                if (line.contains("//")) {
	                    int index = line.indexOf("//"); // // işaretinin pozisyonunu bulur
	                    if (index == 0 || (index > 0 && !line.substring(index - 1, index).equals("\""))) {
	                        commentLines++;
	                    }
	                    continue;
	                }
	                // Çoklu satır yorumları kontrol eder
	                if (line.startsWith("/*") && !line.startsWith("/**")) {
	                    if (line.contains("*/")) {
	                        commentLines++;
	                    } else {  
	                        inBlockComment = true;
	                    }
	                    continue;
	                }

	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return commentLines;
	    }
	    private static int countTotalLines(File file) {
	        int totalLines = 0;
	        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	            while (reader.readLine() != null) {
	                totalLines++;
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return totalLines;
	    }

	    private static int countCodeLines(File file) {
	        int codeLines = 0;
	        boolean inMultiLineComment = false;
	        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                line = line.trim();

	                // Çok satırlı yorum içerisinde olup olmadığımızı kontrol ediyoruz
	                if (inMultiLineComment) {
	                    
	                    if (line.contains("*/")) {
	                        inMultiLineComment = false;
	                       
	                        line = line.substring(line.indexOf("*/") + 2).trim();
	                    } else {
	      
	                        continue;
	                    }
	                }
	                // Eğer satır yorum satırı ile başlıyorsa bu bir tek satırlık yorumdur ve işlememeliyiz
	                if (line.startsWith("//")) {
	                    continue;
	                }

	                // Eğer satırın içerisinde /* varsa bu bir çok satırlı yorumun başlangıcı olabilir
	                if (line.contains("/*")) {
	                   
	                    if (line.contains("*/")) {
	                        line = line.substring(line.indexOf("*/") + 2).trim();
	                    } else {
	                        inMultiLineComment = true;
	                        continue;
	                    }
	                }           
	                if (!line.isEmpty()) {
	                    codeLines++;
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return codeLines;
	    }
}