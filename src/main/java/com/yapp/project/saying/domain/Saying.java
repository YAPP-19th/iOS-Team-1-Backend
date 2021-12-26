package com.yapp.project.saying.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Saying {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(columnDefinition = "VARCHAR(255) default '작자미상'")
    private String author;

    @Builder
    public Saying(Long id, String content, String author){
        Assert.hasText(content,"내용은 반드시 있어야 합니다.");
        this.id = id;
        this.author = author;
        this.content = content;
    }

    @PrePersist
    public void prePersist(){
        this.author = this.author == null ? "작자미상" : this.author;
    }

}
