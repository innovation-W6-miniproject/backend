package com.example.miniproject.controller;


import com.example.miniproject.controller.response.ImageResponseDto;
import com.example.miniproject.controller.response.ResponseDto;
import com.example.miniproject.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    //@RequestParam("image") MultipartFile multipartFile
    //@RequestPart(value = "file") MultipartFile multipartFile
    @PostMapping("/api/auth/image")
    public ResponseDto<?> imageUpload(@RequestPart(value = "file")MultipartFile multipartFile) {

        if(multipartFile.isEmpty()) {
            return ResponseDto.fail("INVALID_FILE","파일이 유효하지 않습니다.");
        }try{
            return ResponseDto.success(new ImageResponseDto(fileUploadService.uploadFile(multipartFile,"image")) );
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.fail("INVALID_FILE","파일이 유효하지 않습니다.");
        }
    }
}
