package com.dehidehidehi.twitchtagcarousel.service.twitchclient;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;

public interface TwitchClient {
    
    void updateTags(TwitchTagBatch tags);
    
}
