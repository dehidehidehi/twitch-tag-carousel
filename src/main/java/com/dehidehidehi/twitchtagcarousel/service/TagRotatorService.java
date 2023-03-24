package com.dehidehidehi.twitchtagcarousel.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TagRotatorService {

    public List<String> getTagRotation() {
        return List.of("hello there");
    }
}
