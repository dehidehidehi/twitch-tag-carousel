package com.dehidehidehi.twitchtagcarousel.service.twitch.heliximpl;
import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.*;
import com.dehidehidehi.twitchtagcarousel.service.TwitchAuthService;
import com.dehidehidehi.twitchtagcarousel.service.twitch.TwitchApiService;
import com.dehidehidehi.twitchtagcarousel.service.twitch.basicimpl.BasicTwitchApiServiceImpl;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.helix.domain.ChannelInformation;
import com.github.twitch4j.helix.domain.UserList;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;


@Typed(TwitchApiService.class)
@ApplicationScoped
public class HelixTwitchApiServiceImpl extends BasicTwitchApiServiceImpl implements TwitchApiService {

    public static final String PROPERTY_KEY_CHANNEL_NAME = "twitch-app.channel-name";
    private static final Logger LOGGER = LoggerFactory.getLogger(HelixTwitchApiServiceImpl.class);
    private final UserPropertiesDao userPropertiesDao;

    private final TwitchAuthService twitchAuthService;
    private final TwitchHelix twitchHelix;

    @Inject
    HelixTwitchApiServiceImpl(final TwitchAuthService twitchAuthService,
                              final UserPropertiesDao userPropertiesDao) {
        this.twitchAuthService = twitchAuthService;
        this.userPropertiesDao = userPropertiesDao;
        twitchHelix = TwitchHelixBuilder.builder().build();
    }

    private void validateUserAccessToken(final String userAccessToken) {
        final String hiddenToken = StringUtils.abbreviateMiddle(userAccessToken, "******", 12);
        LOGGER.info("Validating userAccessToken: {}", hiddenToken);
        final boolean isValid = isUserAccessTokenValid(userAccessToken);
        if (!isValid) {
            LOGGER.error("Your user access token is invalid : {}", hiddenToken);
            throw new AuthTokenQueryException("Your user access token is invalid : %s".formatted(hiddenToken));
        }
    }

    @Override
    public boolean isUserAccessTokenValid(final String userAccessToken) throws AuthTokenQueryException {
        // Twitch4J implementation is otherwise available in the Auth module, TwitchIdentityProvider#getAdditionalCredentialInformation
        return super.isUserAccessTokenValid(userAccessToken);
    }

    /**
     * Requests the channel's id using the channel's name as a parameter.
     */
    @Override
    public String getBroadcasterIdOf() throws TwitchChannelIdException, MissingAuthTokenException {
        LOGGER.debug("{} entered getChannelIdFrom method.", HelixTwitchApiServiceImpl.class.getSimpleName());
        final UserList userList;
        try {
            final String userAccessToken = userPropertiesDao.getUserAccessToken();
            userList = twitchHelix
                    .getUsers(userAccessToken, null, null)
                    .execute();
        } catch (HystrixRuntimeException e) {
            throw new TwitchChannelIdException(e);
        }
        if (userList.getUsers().isEmpty()) {
            throw new TwitchChannelIdException("Unhandled error: Could not find find your channel name.");
        }
        final String channelId = userList.getUsers().get(0).getId();
        LOGGER.debug("Found channelId {}: {}", channelId);
        return channelId;
    }

    /**
     * <a href="https://dev.twitch.tv/docs/api/reference/#replace-stream-tags">Channel Information Twitch API
     * documentation</a><br>
     * Requires a user access token that includes the channel:manage:broadcast scope.<br>
     * channel%3Amanage%3Abroadcast<br>
     * https://id.twitch.tv/oauth2/authorize?client_id=6k3qz1pdf1wko4xec9cjbfh3fbla24&redirect_uri=http://localhost&response_type=token&scope=channel%3Amanage%3Abroadcast
     */
    @Override
    public void updateTags(final TwitchTagBatch tags)
    throws MissingUserProvidedTagsException, TwitchTagUpdateException, MissingAuthTokenException {
        LOGGER.debug("{} entered update tags method with params {}.", HelixTwitchApiServiceImpl.class.getSimpleName(), tags);
        if (tags.get().isEmpty()) {
            throw new MissingUserProvidedTagsException("No tags found. You must provide at least one tag to use the tag carousel.");
        }
        final List<String> tagsAsList = tags.get().stream().map(TwitchTag::toString).toList();
        final ChannelInformation channelInformation = new ChannelInformation().withTags(tagsAsList);
        try {
            final String userAccessToken = userPropertiesDao.getUserAccessToken();
            final String broadcasterId = getBroadcasterIdOf();
            twitchHelix
                    .updateChannelInformation(userAccessToken, broadcasterId, channelInformation)
                    .execute();
        } catch (HystrixRuntimeException e) {
            final String actualErrorFromTwitchApi = e.getCause().getMessage();
            if (actualErrorFromTwitchApi.contains("400") || actualErrorFromTwitchApi.contains("tags were not applied")) {
                final String msg = """
                   One of your tags failed a validation check with twitch.
                   Twitch does not supply details which would tell us which tag caused the error.
                   Try to look for tags which are too short, have syntax errors, or odd (invisible) white space characters).
                   You can try removing all formatting from your tags by copy/pasting back the tags into a basic notepad text editor.
                   
                   Your tags were :
                   [[TAGS]]""".replace("[[TAGS]]", String.join(",%n".formatted(), tagsAsList));
                throw new TwitchTagValidationException(msg);
            } else {
                throw new TwitchTagUpdateException(actualErrorFromTwitchApi);
            }
        }
    }

    @Override
    public void queryUserAccessToken() {
        try {
            twitchAuthService.asyncQueryAccessTokenWithImplicitGrantFlow(userPropertiesDao::setUserAccessToken).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
