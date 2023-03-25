package com.dehidehidehi.twitchtagcarousel.service;
import com.dehidehidehi.twitchtagcarousel.util.CDIExtension;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(CDIExtension.class)
class TagRotatorServiceTest {
    
    @Inject
    private TagRotatorService tagRotatorService;
    
    @Test
    void selectTagsShouldReturnTenTags() {
        assertThat(tagRotatorService.selectTags()).hasSize(10);
    }

}
