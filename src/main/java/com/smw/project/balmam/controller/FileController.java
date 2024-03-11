package com.smw.project.balmam.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smw.project.balmam.dto.MediaFileDto;
import com.smw.project.balmam.dto.ResultData;
import com.smw.project.balmam.entity.MediaFileEntity;
import com.smw.project.balmam.service.FileService;

@RestController
@RequestMapping("/files")
public class FileController {

	@Autowired
	private FileService fileService;

	@Value("${file.upload.realPath}")
	private String uploadRealPath;
	
	@Value("${file.upload.path}")
	private String uploadPath;
	
	private static final List<String> imageExtensionList = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");

	@PostMapping("/upload")
	public ResultData<MediaFileDto>  uploadFile(@RequestParam("file") MultipartFile file) {
		
		try {
			String name = constructFilename(file.getOriginalFilename());
			Path savePath = Paths.get(uploadRealPath).resolve(name);
			Files.copy(file.getInputStream(), savePath);
			long size = file.getSize();
			String type = null;

			// Extract the file extension

			int lastIndex = name.lastIndexOf('.');
			String extention = name.substring(lastIndex + 1).toLowerCase();
			if(imageExtensionList.contains(extention)) {
				type="image";
			}
//			if (lastIndex != -1 && lastIndex < name.length() - 1) {
//				extention = name.substring(lastIndex + 1).toLowerCase();
//			}
			
			MediaFileEntity mediaFileEntity = new MediaFileEntity(name, uploadPath, uploadRealPath, size, type);
			fileService.insertMediaFile(mediaFileEntity);
			MediaFileDto mediaFileDto = new MediaFileDto(mediaFileEntity);
			
			ResultData<MediaFileDto> resultDaata = ResultData.ofData("S-1", "파일 등록이 완료되었습니다.", "미디어 파일", mediaFileDto);

			return resultDaata;
		} catch (IOException e) {
			e.printStackTrace();
			return ResultData.ofMessage("F-1", "파일 등록에 실패하였습니다.");
		}
	}

	private String constructFilename(String originalFilename) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String uuid = UUID.randomUUID().toString().substring(0, 7);
		String extension = StringUtils.getFilenameExtension(originalFilename);
		return timestamp + "_" + uuid + (extension != null ? "." + extension : "");
	}
}