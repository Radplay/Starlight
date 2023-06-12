Starlight Legacy (Fabric)
==
A backport of Starlight for 1.14.x and 1.15.x with updates from the upstream repository.

Credits:

[1.15.x backport by dariasc](https://github.com/dariasc/Starlight)

[1.14.x backport by KingContaria](https://github.com/KingContaria/Starlight-1.14)

Special thanks to [KingContaria](https://github.com/KingContaria) for providing updated source code for Fabric 1.14.x - 1.16.x

## Known Issues
At this point in Starlight's development, I expect problems with mod conflicts.

## Download
**Starlight Legacy download**<br>
[![modrinth](https://img.shields.io/badge/dynamic/json?color=158000&label=downloads&prefix=+%20&query=downloads&url=https://api.modrinth.com/v2/project/N6SNVyVO?style=social&logo=modrinth)](https://modrinth.com/mod/starlight-legacy)
[![github](https://img.shields.io/github/downloads/Radplay/Starlight-Legacy/total?style=social)](https://github.com/Radplay/Starlight-Legacy/releases)<br>
**Starlight download**<br>
[CurseForge (Fabric)](https://www.curseforge.com/minecraft/mc-mods/starlight)<br>
[Modrinth (Fabric)](https://modrinth.com/mod/starlight)

## Contact
[Discord](https://discord.gg/tuinity)

## Results
Please note that I do not expect this mod to improve FPS client side. This is mostly going to affect
chunk generation speed and FPS drops when people are building on large platforms in the sky. Starlight
also fixes [MC-162253](https://bugs.mojang.com/browse/MC-162253), which only affects
lag spikes specifically from lighting.
An increase in chunk generation speed can also negatively affect FPS by making the client 
render more chunks per frame. However, I only noticed this effect, even on non-optimal hardware,
when generating amplified worlds which see a very significant chunk generation speed uplift.

The graph below shows how much time the light engine was active while generating 10404 chunks:
![Light engine time chunk generation Graph](https://i.imgur.com/5aI8Eaf.png)
- Tested via [LightBench](https://github.com/Spottedleaf/lightbench)
- World seed: vanillakekw
- CPU: Ryzen 9 5950X

Below is a graph detailing how long light updates took for breaking/placing
a block on a large platform at y = 254 down to a large platform at y = 0:
![Block update at height graph](https://i.imgur.com/kKtbe9y.png)
- Tested via [LightBench](https://github.com/Spottedleaf/lightbench)
- World is just a flat world with bedrock at y = 0 and grass at y = 254
- CPU: Ryzen 9 5950X

Below is a graph detailing light update times for a simple glowstone
place/break:
![Simple glowstone block update](https://i.imgur.com/yCNK602.png)
- Tested via [LightBench](https://github.com/Spottedleaf/lightbench)
- World is just a flat world with bedrock at y = 0 and grass at y = 254
- CPU: Ryzen 9 5950X
- Tested breaking and placing the glowstone on the bedrock platform,
  where skylight could not interfere with the test.

The results are pretty clear. Starlight is the fastest, by 
an _unbelievable_ margin. 

If you want more details on these graphs (and how Starlight achieves these numbers), 
you can check out [TECHNICAL_DETAILS.md](TECHNICAL_DETAILS.md), but that document is
mostly for people who understand programming.

## Purpose
The performance of the Vanilla engine is just awful. Existing modifications like 
Phosphor or Paper's light engine changes (some of Paper's changes include Phosphor's) 
fail to fix this issue, even though they improve it.

Phosphor's changes to the existing light engine are very good, but there's only 
so much that can be done to Vanilla's code to improve performance.
Paper also suffers from the same issue as Phosphor, as they also opt for 
changing Vanilla code.

On inspection of the light engine code to see what was causing such
regressions in performance, it became obvious that there were
performance problems were everywhere in how Vanilla processed updates, 
It seemed like Vanilla was optimised to try and reduce light updates, 
but it came at the cost of slowing down each light update. So even 
when it did _fewer_ updates, it took more time.

So, If I wanted to fix them I would effectively have to end 
up rewriting most of the light engine, and at that point it's 
easier to write a new one than rework the one old. The 
Vanilla light engine simply had too many problems to fix.
