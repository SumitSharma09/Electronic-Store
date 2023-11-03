package com.sumit.electronic.store.servicesImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sumit.electronic.store.exception.BadApiRequest;
import com.sumit.electronic.store.services.FileService;

@Service
public class FileServiceImpl implements FileService{
	
	
	private Logger logger = LoggerFactory.getLogger(FileService.class);

	@Override
	public String uploadFile(MultipartFile file, String path) throws IOException {
		// TODO Auto-generated method stub
		
		String originalfilename = file.getOriginalFilename();
		logger.info("file name is : {}", originalfilename);
		String filename = UUID.randomUUID().toString();
		String extension= originalfilename.substring(originalfilename.lastIndexOf("."));
		String filenameWithExtension=filename+extension;
		String fullPathWithfilename = path + filenameWithExtension;
		
		logger.info("full image path {}", fullPathWithfilename);
		if(extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png")) {
			// file save
			logger.info("file extension is ", extension);
			File folder=new File(path);
			if(!folder.exists()) {
				folder.mkdirs();
			}
			
			Files.copy(file.getInputStream(), Paths.get(fullPathWithfilename));	
			return filenameWithExtension;
			
		}else {
			throw new BadApiRequest("File with this "+extension+ "not allowed !!");
		}
	}

	@Override
	public InputStream getResources(String path, String name) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		String fullPath = path + File.separator + name;
		
		InputStream inputStream = new FileInputStream(fullPath);
		return inputStream;
		
	
	}
	
	
	

}
