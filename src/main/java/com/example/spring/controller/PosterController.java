package com.example.spring.controller;

import com.example.spring.dto.MovieDto;
import com.example.spring.service.MovieService;
import com.example.spring.service.PosterService;
import com.example.spring.service.ReviewService;
import com.example.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class PosterController {
    private final PosterService posterService;
    private final MovieService movieService;

    @GetMapping("")
    public ResponseEntity<?> getAllMovies() {
        try {
            List<MovieDto> movieList = movieService.getAllMovies();
            return ResponseEntity.ok(Map.of("movies", movieList));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Movie not found") ;
        }
    }




    //영화 상세정보 url 수정
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getMovieDetails(@PathVariable long id) {
        try {
            return ResponseEntity.ok(movieService.getOneMovieReviews(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Movie not found with ID: " + id);
        }
    }



    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(@RequestParam("keyword") String keyword) {
        List<MovieDto> movieDtos = movieService.getSearchMovies(keyword);

        return ResponseEntity.ok(Map.of("movies", movieDtos));
    }
}


/**

 {
     "movies": [
         {
             "id": 1,
             "director": "윤제완",
             "title": "뽀로로 극장판 바닷속 대모험",
             "plot": "“대장님 같은 영웅이 되고 싶어요!”  바다의 수호자 ‘레드헌터스’를 이끄는 ‘머록’ 대장을 따라 바다를 지키는 영웅이 되기 위해 바닷속 모험을 떠난 뽀로로와 친구들. 하지만 거대한 괴물 ‘시터스’가 뽀로로와 친구들의 잠수함을 통째로 삼켜버리고, ‘머록’ 대장의 도움으로 가까스로 구출된 ‘뽀로로’와 ‘크롱’은 ‘시터스’에게 잡혀간 친구들을 구하기 위해 ‘최후의 시터스’ 사냥에 나선 ‘레드헌터스’의 마지막 작전에 함께하기로 한다. 그러나, 비밀스러운 소녀 ‘마린’이 나타나 그들의 작전을 방해하기 시작하고 바닷속에 숨겨진 진짜 비밀이 드러나게 되는데…  과연, 뽀로로와 친구들은 신비한 비밀을 풀고 진정한 ‘씨 가디언즈’가 될 수 있을까?",
             "releaseDate": "2025-01-01",
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
             ],
             "movieReviews": null,
             "moviePosts": null,
             "posterPath": null
         },
         {
             "id": 2,
             "director": "마이클 만",
             "title": "페라리",
             "plot": "1957년, 전세계를 뒤흔든 '페라리'의 충격 실화가 드러난다!  파산 위기에 놓인 '엔초 페라리'. 회사 존폐의 기로에서 사사건건 충돌하는 아내 라우라. 아들 피에로를 페라리 가로 인정하라고 압박하는 또다른 여인 리나. 평생 쌓아온 모든 것이 무너지기 직전인 1957년 여름, 이탈리아 전역 공도를 가로지르는 광기의 1,000마일 레이스 '밀레 밀리아'에서 엔초 페라리는 판도를 뒤집을 마지막 승부수를 던지는데...",
             "releaseDate": "2025-01-08",
             "actors": [
                 {
                 "id": 15,
                 "name": "아담 드라이버"
                 },
                 {
                 "id": 16,
                 "name": "페넬로페 크루즈"
                 }
             ],
             "genres": [
                 {
                 "id": 4,
                 "name": "액션"
                 },
                 {
                 "id": 6,
                 "name": "드라마"
                 }
             ],
             "movieReviews": null,
             "moviePosts": null,
             "posterPath": null
         },
         ...
     ]
 }

 */