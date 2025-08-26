package com.example.ex4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
// 공통된 매팽 정보를 상소받기 위한 클래스에 사용
@MappedSuperclass
// 엔티티의 객체가 생성 변경되는 것을 자동으로 감지 후 반영
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class BasicEntity {
  @CreatedDate
  @Column(name = "regDate", updatable = false)
  private LocalDateTime regDate;

  @LastModifiedDate
  @Column(name = "modDate", updatable = true)
  private LocalDateTime modDate;
}
