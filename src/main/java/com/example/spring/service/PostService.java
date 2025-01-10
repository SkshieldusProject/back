package com.example.spring.service;

import com.example.spring.dto.PostDto;
import com.example.spring.dto.UserDto;
import com.example.spring.entity.Post;
import com.example.spring.entity.Review;
import com.example.spring.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public PostDto getOnePost(Long id) {
        Optional<Post> oPost = postRepository.findById(id);
        if(oPost.isPresent()) {
            Post post = oPost.get();
            return PostDto.fromEntity(post);
        }
        throw new NoSuchElementException("User not found with postId: " + id);
    }
// 추가
    public List<PostDto> getPostsByUser(UserDto user) {
        List<Post> posts = postRepository.findByUserId(user.getId());
        if (posts == null || posts.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }
        return posts.stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }


    public void deletePost(PostDto postDto) {
        postRepository.delete(postDto.toEntity());
    }

    public void modify(PostDto postDto) {
        postRepository.save(postDto.toEntity());
    }
}
