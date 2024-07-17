package com.forumhub.Forum.Hub.service;

import com.forumhub.Forum.Hub.dto.CursoDTO;
import com.forumhub.Forum.Hub.dto.CursoIdDTO;
import com.forumhub.Forum.Hub.model.Curso;
import com.forumhub.Forum.Hub.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CursoService {
    @Autowired
    private CursoRepository repository;


    @Transactional
    public Long saveCurso(CursoDTO cursoDTO) {
        if (cursoDTO.nome() == null || cursoDTO.categoria() == null) {
            throw new IllegalArgumentException("Nome e categoria são obrigatórios!");
        }

        Curso curso = new Curso();
        curso.setNome(cursoDTO.nome());
        curso.setCategoria(cursoDTO.categoria());

        Curso savedCurso = repository.save(curso);
        return savedCurso.getId();
    }

    public Page<CursoIdDTO> getAllCursos(Pageable pageable) {
        Page<Curso> cursosPage = repository.findAll(pageable);
        return cursosPage.map(cursos -> new CursoIdDTO(
                cursos.getId(), cursos.getNome(),
                cursos.getCategoria()));
    }

    public void updateCurso(Long cursoId,
                            CursoIdDTO cursoIdDto) {
        Optional<Curso> optional = repository.findById(cursoId);
        if (optional.isEmpty()) {
            throw new IllegalStateException("Curso não encontrado!");
        }
        Curso existingCurso = optional.get();

        existingCurso.setNome(cursoIdDto.nome());
        existingCurso.setCategoria(cursoIdDto.categoria());

        repository.save(existingCurso);
    }

    public boolean existsById(Long cursoId) {
        return repository.existsById(cursoId);
    }
}
