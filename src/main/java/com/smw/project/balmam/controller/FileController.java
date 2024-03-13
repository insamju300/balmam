package com.smw.project.balmam.controller;

import java.io.File;
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
import com.smw.project.balmam.enums.MediaType;
import com.smw.project.balmam.service.FileService;


@RestController
@RequestMapping("/files")
public class FileController {

	@Autowired
	private FileService fileService;

	@Value("${file.upload.realPath}")
	private String realPath;
	
	@Value("${file.upload.path}")
	private String path;
	
	@Value("${file.tools.ffmpegPath}")
	private String ffmpegPath;
	
	private static final List<String> imageExtensionList = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
	private static final List<String> videoExtensionList = Arrays.asList("mp4");

	@PostMapping("/upload")
	public ResultData<MediaFileDto>  uploadFile(@RequestParam("file") MultipartFile file) {
		
		try {
			String name = constructFilename(file.getOriginalFilename());
			Path savePath = Paths.get(realPath).resolve(name);
			Files.copy(file.getInputStream(), savePath);
			long size = file.getSize();
			MediaType type = null;

			// Extract the file extension

			int lastIndex = name.lastIndexOf('.');			
			String extention = name.substring(lastIndex + 1).toLowerCase();
			
			String thumbnailName = null;
			
			if(imageExtensionList.contains(extention)) {
				type=MediaType.image;
			}else if(videoExtensionList.contains(extention)) {
				type=MediaType.video;
				thumbnailName = generateAndSaveThumbnail(name, savePath); //todo 이미지 생성에 실패했을 경우 처리 생각하기.
			}
			
//			if (lastIndex != -1 && lastIndex < name.length() - 1) {
//				extention = name.substring(lastIndex + 1).toLowerCase();
//			}
			
			
			MediaFileEntity mediaFileEntity = new MediaFileEntity(name,thumbnailName, size, type);
			fileService.insertMediaFile(mediaFileEntity);
			
			MediaFileEntity findMediaFileEntity = fileService.findFileById(mediaFileEntity.getId()); //todo insert실패할 경우 에러 처리 생각할 것.
			MediaFileDto mediaFileDto = new MediaFileDto(findMediaFileEntity, path);
			
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
	
    public String generateAndSaveThumbnail(String videoFileName, Path videoFilePath) {
    	System.err.println(String.format("videoFilePath : %s, videoFileName : %s", videoFilePath , videoFileName));

    	String[] videoFileNameBits  = videoFileName.split("\\.");
    	String videoFileNameWithoutExtension = videoFileNameBits[0];
        String thumbnailName = videoFileNameWithoutExtension + "_thumbnail.jpg"; 
        Path thumbnailPath = Paths.get(realPath).resolve(thumbnailName);
        
        System.err.println(String.format("ffmpegPath : %s thumbnailPath", ffmpegPath ));

        try {
            // Execute FFmpeg command to extract first frame as thumbnail
        	ProcessBuilder processBuilder = new ProcessBuilder(
        			ffmpegPath, // ffmpeg 실행 파일의 전체 경로
        		    "-i", videoFilePath.toString(), // 입력 비디오 파일 경로
        		    "-vframes", "1", // 첫 번째 프레임만 추출
        		    "-an", // 오디오 무시
        		    "-s", "320x240", // 출력 이미지 크기 설정
        		    thumbnailPath.toString() // 썸네일 저장 경로
        		);
            Process process = processBuilder.start();
            process.waitFor(); // Wait for FFmpeg process to finish

            // Check if thumbnail was created successfully
            File thumbnailFile = new File(thumbnailPath.toString());
            if (thumbnailFile.exists()) {
                return thumbnailName; // Return thumbnail filename
            } else {
                throw new IOException("Thumbnail generation failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null; // Return null if thumbnail generation fails
        }
    }
}