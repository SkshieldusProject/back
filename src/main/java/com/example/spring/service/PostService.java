package com.example.spring.service;

import com.example.spring.dto.PostDto;
import com.example.spring.entity.Post;
import com.example.spring.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<PostDto> getRecentPosts() {
        List<Post> posts = postRepository.findAllByOrderByCreateDateDesc();
        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            postDtos.add(PostDto.builder()
                            .id(post.getId())
                            .subject(post.getSubject())
                            .content(post.getContent())
                            .createDate(post.getCreateDate())
                            .user(post.getUser())
                            .movie(post.getMovie())
                    .build());
        }
        return postDtos;
    }

    public void create(PostDto postDto) {
        postRepository.save(Post.builder()
                        .subject(postDto.getSubject())
                        .content(postDto.getContent())
                        .createDate(postDto.getCreateDate())
                        .user(postDto.getUser())
                        .movie(postDto.getMovie())
                        .build());
        }
}
