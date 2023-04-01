package com.dehidehidehi.twitchtagcarousel.producer;

import com.dehidehidehi.twitchtagcarousel.CarouselUi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
class UiProducer {
	
	@Produces
	CarouselUi produceCarouselUi() {
		return new com.dehidehidehi.twitchtagcarousel.swing.SwingCarouselUi();
	}
}
