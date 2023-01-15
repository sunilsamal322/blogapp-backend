package com.techblog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer categoryId;
    private String categoryTitle;
    private String categoryDescription;
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Post> posts;
}
