package com.forumhub.Forum.Hub.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Table(name = "cursos")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String categoria;

    @OneToMany(mappedBy = "curso")
    private List<Topico> topicos = new ArrayList<>();

    public Curso(Long cursoId, String nome, String categoria) {
    }
}