package com.olaaref.mintshop.setting;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.olaaref.mintshop.helper.FileUploadUtils;

public class TestCreateFiles {

	public static void main(String[] args) throws IOException {
		
		saveFile();

	}

	private static void saveFile() throws IOException {
		
		String filename = "test2.png";
		String uploadDirectory = "src/main/resources/static/img/site-logo/";
		Path uploadPath = Paths.get(uploadDirectory);
		
		byte[] content = null;
		try {
		    content = Files.readAllBytes(uploadPath);
		} catch (final IOException e) {
		}
		
		MultipartFile multipart = new MockMultipartFile(filename, content);
		
		FileUploadUtils.cleanDir(uploadDirectory);
		FileUploadUtils.saveFile(uploadDirectory, filename, multipart);
	}

}
