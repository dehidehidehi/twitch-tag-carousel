package com.dehidehidehi.twitchtagcarousel.service.twitchclient;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;

public interface TwitchClient {
    
    void updateTags(TwitchTagBatch tags) throws TwitchTagUpdateException;
    
}
