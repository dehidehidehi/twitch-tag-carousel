package com.dehidehidehi.twitchtagcarousel;
import com.dehidehidehi.twitchtagcarousel.service.TagRotatorService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagRotatorApplicationTest {

    @Mock
    TagRotatorService tagRotatorService;

    @Inject
    @InjectMocks
    @Spy
    TagRotatorApplication tagRotatorApplication;

    @Test
    void shouldExecute() {
        tagRotatorApplication.start();
        verify(tagRotatorService, times(1)).updateTags(anySet());
    }
}
