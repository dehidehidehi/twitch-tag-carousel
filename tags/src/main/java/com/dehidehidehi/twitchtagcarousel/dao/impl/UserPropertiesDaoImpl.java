package com.dehidehidehi.twitchtagcarousel.dao.impl;

import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Typed(UserPropertiesDao.class)
@ApplicationScoped
class UserPropertiesDaoImpl implements UserPropertiesDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPropertiesDaoImpl.class);

	@Override
	public void saveAllTags(final List<TwitchTag> tags) {
		throw new UnsupportedOperationException("UserPropertiesDaoImpl.saveAllTags not implemented.");
	}

	@Override
	public List<TwitchTag> getAllTags() {
		throw new UnsupportedOperationException("UserPropertiesDaoImpl.getAllTags not implemented.");
	}
}
