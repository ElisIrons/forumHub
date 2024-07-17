package com.forumhub.Forum.Hub.service;

import com.forumhub.Forum.Hub.dto.UsuarioDTO;
import com.forumhub.Forum.Hub.dto.UsuarioDetalhamentoDTO;
import com.forumhub.Forum.Hub.dto.UsuarioIdDTO;
import com.forumhub.Forum.Hub.model.Usuario;
import com.forumhub.Forum.Hub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long saveUser(UsuarioDTO userDTO) {
        if (userDTO.email() == null || userDTO.senha() == null) {
            throw new IllegalArgumentException("Os campos login e senha são obrigatórios!");
        }
        if (usuarioRepository.existsByEmail(userDTO.email())) {
            throw new IllegalStateException("User with this login already exists.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(userDTO.nome());
        usuario.setEmail(userDTO.email());
        usuario.setSenha(passwordEncoder.encode(userDTO.senha()));
        usuario.setStatus(true);

        Usuario savedUser = usuarioRepository.save(usuario);
        return savedUser.getId();
    }

    public Page<UsuarioIdDTO> getAllUsers(Pageable pageable) {
        Page<Usuario> usersPage = usuarioRepository.findAll(pageable);
        return usersPage.map(user -> new UsuarioIdDTO(user.getId(), user.getNome(), user.getEmail(), user.isStatus()));
    }

    @Transactional
    public void updateUser(Long userId, UsuarioIdDTO detalhamentoUserDTO) {
        Optional<Usuario> optionalUser = usuarioRepository
                .findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("Usuário não encontrado!");
        }
        Usuario existingUser = optionalUser.get();

        existingUser.setNome(detalhamentoUserDTO.nome());
        existingUser.setEmail(detalhamentoUserDTO.email());

        existingUser.setStatus(detalhamentoUserDTO.status() ? detalhamentoUserDTO.status() : existingUser.isStatus());

        usuarioRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        Optional<Usuario> optionalUser = usuarioRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("A ID do usuário não é válida!");
        }

        Usuario existingUser = optionalUser.get();
        existingUser.setStatus(false);
        usuarioRepository.save(existingUser);
    }

    public Usuario findByEmail(String email) {
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        return usuario;
    }

    public Optional<UsuarioDetalhamentoDTO> detalharUsuario(Long id) {
        Optional<Usuario> optionalUser = usuarioRepository.findById(id)
                .filter(Usuario::isStatus);
        return optionalUser.map(this::mapToDetalhamentoDTO);
    }

    private UsuarioDetalhamentoDTO mapToDetalhamentoDTO(Usuario user) {
        return new UsuarioDetalhamentoDTO(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.isStatus()
        );
    }
}
