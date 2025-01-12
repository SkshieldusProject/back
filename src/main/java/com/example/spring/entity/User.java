package com.example.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Table(name="userTbl")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true, nullable = false) // name="컬럼명 변경"
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate registerDate;

    @ManyToMany(mappedBy = "recommendationUsers")
    @JsonIgnore
    private List<Review> recommendedReviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Post> userPosts;

    @Builder
    public User(long id, String userId, String password, String email, String phoneNumber,
                LocalDate registerDate, List<Review> reviews, List<Post> userPosts) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registerDate = registerDate;
        this.reviews = reviews;
        this.userPosts = userPosts;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("normal"));
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        // 사용자의 암호화된 암호 대체 처리
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}




/**
 {
     "reviews": [
         {
         "id": 7,
         "score": 2.0,
         "subject": "asdg",
         "content": "asdf",
         "createDate": "2025-01-06T00:00:00",
         "recommendationUsers": [],
         "movie": {
             "id": 1,
             "director": "윤제완",
             "title": "뽀로로 극장판 바닷속 대모험",
             "plot": "“대장님 같은 영웅이 되고 싶어요!”  바다의 수호자 ‘레드헌터스’를 이끄는 ‘머록’ 대장을 따라 바다를 지키는 영웅이 되기 위해 바닷속 모험을 떠난 뽀로로와 친구들. 하지만 거대한 괴물 ‘시터스’가 뽀로로와 친구들의 잠수함을 통째로 삼켜버리고, ‘머록’ 대장의 도움으로 가까스로 구출된 ‘뽀로로’와 ‘크롱’은 ‘시터스’에게 잡혀간 친구들을 구하기 위해 ‘최후의 시터스’ 사냥에 나선 ‘레드헌터스’의 마지막 작전에 함께하기로 한다. 그러나, 비밀스러운 소녀 ‘마린’이 나타나 그들의 작전을 방해하기 시작하고 바닷속에 숨겨진 진짜 비밀이 드러나게 되는데…  과연, 뽀로로와 친구들은 신비한 비밀을 풀고 진정한 ‘씨 가디언즈’가 될 수 있을까?",
             "releaseDate": "2025-01-01",
             "posterPath": null,
             "actors": [],
             "genres": [
                 {
                 "id": 8,
                 "name": "가족"
                 },
                 {
                 "id": 9,
                 "name": "애니메이션"
                 }
             ]
         },
         "user": null
         },
         {
             "id": 8,
             "score": 4.5,
             "subject": "Amazing movie experience!",
             "content": "The movie had a gripping story and fantastic visual effects. Highly recommend!",
             "createDate": "2025-01-08T12:34:56",
             "recommendationUsers": [],
             "movie": {
                 "id": 1,
                 "director": "윤제완",
                 "title": "뽀로로 극장판 바닷속 대모험",
                 "plot": "“대장님 같은 영웅이 되고 싶어요!”  바다의 수호자 ‘레드헌터스’를 이끄는 ‘머록’ 대장을 따라 바다를 지키는 영웅이 되기 위해 바닷속 모험을 떠난 뽀로로와 친구들. 하지만 거대한 괴물 ‘시터스’가 뽀로로와 친구들의 잠수함을 통째로 삼켜버리고, ‘머록’ 대장의 도움으로 가까스로 구출된 ‘뽀로로’와 ‘크롱’은 ‘시터스’에게 잡혀간 친구들을 구하기 위해 ‘최후의 시터스’ 사냥에 나선 ‘레드헌터스’의 마지막 작전에 함께하기로 한다. 그러나, 비밀스러운 소녀 ‘마린’이 나타나 그들의 작전을 방해하기 시작하고 바닷속에 숨겨진 진짜 비밀이 드러나게 되는데…  과연, 뽀로로와 친구들은 신비한 비밀을 풀고 진정한 ‘씨 가디언즈’가 될 수 있을까?",
                 "releaseDate": "2025-01-01",
                 "posterPath": null,
                 "actors": [],
                 "genres": [
                     {
                     "id": 8,
                     "name": "가족"
                     },
                     {
                     "id": 9,
                     "name": "애니메이션"
                     }
                 ]
             },
             "user": null
         },
     ]
 }
 */
