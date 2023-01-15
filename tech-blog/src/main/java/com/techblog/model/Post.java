package com.techblog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;

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
    @Column(nullable = false,length = 100000)
    private String content;
    @Column(name="post_time")
    private Date postAddedTime;
    private String  postImageName;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> comments;
    @ManyToOne
    private Category category;
}
