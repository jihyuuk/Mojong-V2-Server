package com.jihyuk.mojong_v2.service;

import com.jihyuk.mojong_v2.model.entity.User;
import com.jihyuk.mojong_v2.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public List<User> findAllMembers(){
        return userRepository.findAll();
    }

    @Transactional
    public void setUserEnabled(Long id, boolean value){
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 직원입니다."));
        user.setEnabled(value);
    }

    @Transactional
    public void approval(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 직원입니다."));
        user.setEnabled(true);
        user.setApproved(true);
        user.setCreatedDate(LocalDateTime.now());
    }

    @Transactional
    public void disApproval(Long id){
        userRepository.deleteById(id);
    }

}
