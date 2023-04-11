# What does the Tag Carousel do?

Introducing the Twitch Tag Carousel - the tool that helps you reach a wider audience by
using all the popular tags available.

Did you know? Twitch channels can only have 10 tags at a time, but with this tool, you can rotate through as many
tags as you'd like.  
The application automatically rotates in popular tags at your preferred interval.  
Maximize your viewership by getting exposure from as many tags as you'd like thanks to the Twitch Tag Carousel.

# How to start the Twitch Tag Carousel

1. Download the Java 20 Runtime environment:
	- Windows: <https://download.oracle.com/java/20/latest/jdk-20_windows-x64_bin.msi>
	- Linux: <https://www.oracle.com/java/technologies/downloads/#jdk20-linux>
2. [Download the `twitch-tag-carousel` application](https://github.com/dehidehidehi/twitch-tag-Carousel/releases/download/v1.0.0/twitch-tag-carousel-jar-with-dependencies.jar).
3. Start the application.
4. That's it!

# Adding popular tags to the rotating tags section

You can find the current 100 top tags here:  
https://okayeg.com/stats  

Then you can copy pasta comma separated list of all (or only those which interest you) into the app.  

## Example of popular tags, comma separated

> 18plus,
action,
adhd,
ally,
ama,
anime,
anxiety,
apex,
argentina,
art,
asian,
asmr,
australia,
australian,
battleroyale,
black,
callofduty,
canada,
casual,
casualplaythrough,
chat,
chatting,
chatty,
chile,
chill,
chilled,
cod,
colombia,
comedy,
comfy,
competitive,
controller,
cozy,
depression,
drops,
dropsactivados,
dropsenabled,
envtuber,
filipino,
firstplaythrough,
fortnite,
fps,
friendly,
fun,
funny,
furry,
gamer,
gamergirl,
gaming,
girl,
gta,
horror,
interactive,
irl,
justchatting,
latam,
latino,
letsplay,
lgbt,
lgbtq,
lgbtqia,
lgbtqiaplus,
lol,
mentalhealth,
mexico,
minecraft,
multiplayer,
music,
nobackseating,
pc,
playingwithviewers,
playstation,
pngtuber,
ps5,
pve,
pvp,
ranked,
retro,
roleplay,
rp,
rpg,
safespace,
shooter,
solo,
spanish,
speedrun,
survival,
usa,
valorant,
variety,
vibesber,
warzone,
woman,
xbox

# Additional information

## Kudos

[chatty/chatty](https://github.com/chatty/chatty) - for coming up with the clever idea on how to handle the twitch
auth implicit grand flow using Swing.

## Some information on the libraries used:

- Jakarta EE 10 Core specifications, notably : CDI, WS RS, Annotation.
- Swing for the GUI.
- Twitch4J (Helix)
