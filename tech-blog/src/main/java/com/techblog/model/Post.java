package com.techblog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 500,nullable = false)
    private String title;
    @Lob
    @Column(nullable = false)
    private String content;
    @Column(name="post_time")
    private Instant postAddedTime;
    @Column(name = "image_name")
    private String postImageName;
    @ManyToOne
    private User user;
}
