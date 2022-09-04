package com.olaaref.mintshop.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtils {
	
	public static void saveFile(String uploadDirectory, String filename, MultipartFile multipart) throws IOException {
		
		Path uploadPath = Paths.get(uploadDirectory);
		
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try(InputStream inputStream = multipart.getInputStream()){
			Path filePath = uploadPath.resolve(filename);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		}
		catch(IOException e) {
			throw new IOException("Could not save file : " + filename, e);
		}
	}
	
	public static void cleanDir(String directory){
		
		Path dirPath = Paths.get(directory);
		
		try {
			Files.list(dirPath).forEach(file -> {
				if(!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					}
					catch(IOException e) {
						System.out.println("Could not delete file : " + file);
					}
				}
			});
		}
		catch(IOException e) {
			System.out.println("Could not list directory : " + dirPath);
		}
		
		
	}

}
