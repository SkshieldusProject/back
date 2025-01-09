package com.example.spring.service;

import com.example.spring.entity.Poster;
import com.example.spring.repository.PosterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PosterService {
    private final PosterRepository posterRepository;

    public PosterService(PosterRepository posterRepository) {
        this.posterRepository = posterRepository;
    }

    // 포스터 저장
    public Poster savePoster(Poster poster) {
        return posterRepository.save(poster);
    }

    // ID로 포스터 조회
    public Poster getPosterById(Long id) {
        return posterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Poster not found with ID: " + id));
    }

    // 포스터 파일 저장
    public String savePosterFile(MultipartFile posterFile) throws IOException {
        String uploadDir = "src/main/resources/static/images"; // 파일 저장 경로
        File uploadFolder = new File(uploadDir);

        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String fileName = posterFile.getOriginalFilename();
        String filePath = uploadDir + File.separator + fileName;

        posterFile.transferTo(new File(filePath)); // 파일 저장

        return "/images/" + fileName; // 클라이언트에서 접근 가능한 경로 반환
    }
}
