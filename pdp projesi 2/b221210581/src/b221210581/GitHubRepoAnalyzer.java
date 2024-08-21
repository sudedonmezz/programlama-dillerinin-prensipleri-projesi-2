package b221210581;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
*
* @author sude dönmez
* @since 01.04.2024
* <p>
* github dosyalarının klonlandığı,listelendiği ve javafileanalyzer sınıfını çağıran ana class.
* </p>
*/

public class GitHubRepoAnalyzer {

	 

	    public static void main(String[] args) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	        try {
	            System.out.println("GitHub depo linkini girin:");
	            String repoUrl = reader.readLine().trim();

	            File clonedRepoDir = cloneRepository(repoUrl);

	            if (clonedRepoDir != null) {
	                analyzeRepository(clonedRepoDir);
	            } else {
	                System.out.println("Depo klonlanamadı. Lütfen geçerli bir GitHub depo linki girin.");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                reader.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    private static File cloneRepository(String repoUrl) {
	        try {
	            File clonedRepoDir = new File(repoUrl.substring(repoUrl.lastIndexOf('/') + 1));
	            if (clonedRepoDir.exists() && clonedRepoDir.isDirectory() && containsJavaFiles(clonedRepoDir)) {
	                System.out.println("Depo zaten klonlandı ve içinde *.java dosyaları bulunuyor.");
	                System.out.println("Klonlanan depo dizini: " + clonedRepoDir.getAbsolutePath());
	                return clonedRepoDir;
	            } else {
	                String cloneCommand = "git clone " + repoUrl;
	                Process cloneProcess = Runtime.getRuntime().exec(cloneCommand);
	                int cloneResult = cloneProcess.waitFor();

	                if (cloneResult == 0) {
	                    System.out.println("Depo başarıyla klonlandı.");
	                    return clonedRepoDir;
	                }
	            }
	        } catch (IOException | InterruptedException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    private static void analyzeRepository(File clonedRepoDir) {
	        System.out.println("Java dosyaları:");
	        listJavaFiles(clonedRepoDir);
	    }

	    private static void listJavaFiles(File directory) {
	        File[] files = directory.listFiles();
	        if (files != null) {
	            for (File file : files) {
	                if (file.isDirectory()) {
	                    listJavaFiles(file);
	                } else if (file.isFile() && file.getName().endsWith(".java")) {
	                    JavaFileAnalyzer.analyzeJavaFile(file); // JavaFileAnalyzer sınıfından analiz yapar
	                }
	            }
	        }
	    }
	    private static boolean containsJavaFiles(File directory) {
	        File[] files = directory.listFiles();
	        if (files != null) {
	            for (File file : files) {
	                if (file.isDirectory()) {
	                    if (containsJavaFiles(file)) {
	                        return true;
	                    }
	                } else if (file.isFile() && file.getName().endsWith(".java")) {
	                    return true;
	                }
	            }
	        }
	        return false;
	    }
	}