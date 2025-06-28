package com.example.springuserservice.service;

import com.example.springuserservice.dto.UserDto;
import com.example.springuserservice.entity.User;
import com.example.springuserservice.kafka.Producer;
import com.example.springuserservice.mapper.UserMapper;
import com.example.springuserservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;
    private final Producer producer;

    public UserService(UserRepository repository, Producer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    public UserDto getById(Long id) {
        return repository.findById(id)
                .map(UserMapper::toDto)
                .orElse(null);
    }

    public List<UserDto> getAll() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto create(UserDto dto) {
        User user = UserMapper.toEntity(dto);
        User saved = repository.save(user);
        producer.sendUserEvent(saved.getEmail(), "CREATE");
        return UserMapper.toDto(saved);
    }

    public UserDto update(Long id, UserDto dto) {
        User user = repository.findById(id).orElseThrow();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return UserMapper.toDto(repository.save(user));
    }

    public void delete(Long id) {
        User user = repository.findById(id).orElseThrow();
        repository.deleteById(id);
        producer.sendUserEvent(user.getEmail(), "DELETE");
    }
}