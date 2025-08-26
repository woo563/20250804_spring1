package com.example.ex6.repository;

import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
class MovieRepositoryTests {

  @Autowired
  MovieRepository movieRepository;

  @Autowired
  MovieImageRepository movieImageRepository;

  @Test
  @Transactional
  @Commit
  public void insertMovies() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      Movie movie = Movie.builder().title("Movie..."+i).build();
      movieRepository.save(movie);

      int count = (int) (Math.random()*5) + 1;
      for (int j = 0; j < count; j++) {
        MovieImage movieImage = MovieImage.builder()
            .uuid(UUID.randomUUID().toString())
            .imgName("test"+j+".jpg")
            .movie(movie)
            .build();
        movieImageRepository.save(movieImage);
      }
    });
  }

  @Test
  public void testGetListPage() {
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
    Page<Object[]> result = movieRepository.getListPage(pageRequest);
    for (Object[] objects : result.getContent()) {
      System.out.println(Arrays.toString(objects));
    }
  }
  @Test
  public void testGetListPageMi() {
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
    Page<Object[]> result = movieRepository.getListPageMi(pageRequest);
    for (Object[] objects : result.getContent()) {
      System.out.println(Arrays.toString(objects));
    }
  }
  @Test
  public void testGetListPageMaxMi() {
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
    Page<Object[]> result = movieRepository.getListPageMaxMi(pageRequest);
    for (Object[] objects : result.getContent()) {
      System.out.println(Arrays.toString(objects));
    }
  }

  @Test
  public void testGetMovieWithAllMi() {
    List<Object[]> result = movieRepository.getMovieWithAll(5L);
    for (Object[] objects : result) {
      System.out.println(Arrays.toString(objects));
    }
  }

}