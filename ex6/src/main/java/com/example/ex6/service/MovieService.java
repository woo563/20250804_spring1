package com.example.ex6.service;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.MovieImageDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.dto.PageResultDTO;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface MovieService {
  default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
    Map<String, Object> entityMap = new HashMap<>();
    Movie movie = Movie.builder()
            .mno(movieDTO.getMno()).title(movieDTO.getTitle())
            .build();
    entityMap.put("movie", movie);

    List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();
    if (imageDTOList != null && imageDTOList.size() > 0) {
      List<MovieImage> movieImageList = imageDTOList.stream().map(
              new Function<MovieImageDTO, MovieImage>() {
                @Override
                public MovieImage apply(MovieImageDTO movieImageDTO) {
                  return MovieImage.builder()
                          .path(movieImageDTO.getPath())
                          .imgName(movieImageDTO.getImgName())
                          .uuid(movieImageDTO.getUuid())
                          .movie(movie)
                          .build();
                }
              }
      ).collect(Collectors.toList());
      entityMap.put("imgList", movieImageList);
    }
    return entityMap;
  }

  default MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages, Double avg, Long reviewCnt) {
    MovieDTO movieDTO = MovieDTO.builder()
            .mno(movie.getMno())
            .title(movie.getTitle())
            .regDate(movie.getRegDate())
            .modDate(movie.getModDate())
            .build();
    List<MovieImageDTO> movieImageDTOList = movieImages.stream().map(mi -> {
      return MovieImageDTO.builder()
              .path(mi.getPath()).imgName(mi.getImgName())
              .uuid(mi.getUuid()).build();
    }).collect(Collectors.toList());
    movieDTO.setImageDTOList(movieImageDTOList);
    movieDTO.setAvg(avg);
    movieDTO.setReviewCnt(reviewCnt.intValue());
    return movieDTO;
  }

  Long register(MovieDTO movieDTO);

  PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

  MovieDTO get(Long mno);

  void modify(MovieDTO movieDTO);

  List<String> removeWithReviewsAndMovieImages(Long mno);

  void removeMovieImagebyUUID(String uuid);
}