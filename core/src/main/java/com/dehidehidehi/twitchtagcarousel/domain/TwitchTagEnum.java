package com.dehidehidehi.twitchtagcarousel.domain;

/**
 * Top twitch tags by popularity.
 */
public enum TwitchTagEnum {

    chill(5_300, 4.3F),
    vtuber(4_034, 3.27F),
    fps(2_479, 2.01F),
    anime(2_340, 1.9F),
    gaming(2_167, 1.76F),
    adhd(2_031, 1.65F),
    pvp(1_928, 1.56F),
    valorant(1_753, 1.42F),
    multiplayer(1_699, 1.8F),
    playingwithviewers(1_62, 1.35F),
    ranked(1_633, 1.32F),
    chatty(1_613, 1.31F),
    dropsactivados(1_605, 1.3F),
    dropsaktiviert(1_601, 1.3F),
    ama(1_578, 1.28F),
    fun(1_566, 1.27F),
    dropsenabled(1_557, 126F),
    fortnite(1_543, 1.25F),
    competitive(1_474, 1.9F),
    lgbtqiaplus(1_462, 1.9F),
    pc(1_394, 1.13F),
    safespace(1_377, 1.12F),
    roleplay(1_184, 0.96F),
    funny(1_179, 0.96F),
    dropsativados(1_157, .94F),
    warzone(1_156, 0.94F),
    cozy(1_124, 0.91F),
    firstplaythrough(1_10, 0.89F),
    shooter(1_089, 0.88F),
    casual(1_040, 0.84F),
    rpg(992, 0.8F),
    music(960, 0.78F),
    horror(885, 0.72F),
    chilled(879, 0.71F),
    gamer(865, 0.7F),
    rp(856, 0.69F),
    furry(791, 0.64F),
    ps5(785, 0.64F),
    mentalhealth(771, 0.6F),
    lgbt(767, 0.62F),
    lgbtq(765, 0.62F),
    minecraft(763, 0.62F),
    lgbtqia(761, 0.62F),
    france(760, 0.62F),
    cod(755, 0.61F),
    nobackseating(746, 0.F),
    pve(746, 0.6F),
    argentina(725, 0.59F),
    girl(680, 0.55F),
    pngtuber(679, 0.55F),
    gta(676, 0.55F),
    xbox(669, 0.54F),
    survival(656, 0.53F),
    variety(656, 0.53F),
    uk(635, 0.51F),
    fr(632, 0.51F),
    black(625, 0.51F),
    brasil(615, 0.5F),
    callofduty(605, 0.49F),
    chatting(603, 0.49F),
    drops(601, 0.49F),
    retro(601, 0.49F),
    gameplay(598, 0.48F),
    lol(596, 0.48F),
    csgo(594, 0.48F),
    playstation(589, 0.48F),
    nederlands(585, 0.47F),
    community(582, 0.47F),
    envtuber(582, 0.47F),
    apex(582, 0.47F),
    friendly(575, 0.47F),
    competitivo(561, 0.45F),
    chat(546, 0.44F),
    letsplay(545, 0.44F),
    irl(542, 0.44F),
    woman(540, 0.44F),
    action(539, 0.44F),
    francais(537, 0.44F),
    chile(535, 0.43F),
    speedrun(520, 0.42F),
    usa(516, 0.42F),
    british(515, 0.42F),
    justchatting(505, 0.4F),
    gtarp(500, 0.41F),
    asmr(497, 0.4F),
    latino(489, 0.4F),
    canada(487, 0.39F),
    comfy(487, 0.39F),
    anxiety(483, 0.39F),
    game(483, 0.39F),
    arab(477, 0.39F),
    dropabilitati(476, 0.9F),
    leagueoflegends(470, .38F),
    nospoilers(454, 0.37F),
    solo(446, 0.36F),
    gamergirl(440, 0.36F);

    private final long numberChannels;
    private final float proportionChannels;

    TwitchTagEnum(final long numberChannels, final float proportionChannels) {
        this.numberChannels = numberChannels;
        this.proportionChannels = proportionChannels;
    }

    public long getNumberChannels() {
        return numberChannels;
    }

    public float getProportionChannels() {
        return proportionChannels;
    }
}
