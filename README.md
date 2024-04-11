# What is Fork?
This is a fork of the Random Loot (sometimes called Random Loot 2) mod for Minecraft. The Random Loot mod was created by TheMarstonConnell. 
I, treycool23, created this fork in the hopes of continuing the Random Loot mod for 1.20.1 in the absence of its creator, who has seemingly moved on to working on the blockchain.
As an amateur programmer, and someone who has never created a minecraft mod before, this is my first real project. Any help or comments would be greatly appreciated. 

The default Random Loot description is below. 

# Random Loot
![CF downloads](https://cf.way2muchnoise.eu/301631.svg) ![Modrinth Downloads](https://img.shields.io/modrinth/dt/bM2Gf75C?logo=modrinth&label=Modrinth)
 ![CF Version](https://cf.way2muchnoise.eu/versions/301631.svg) [![Gradle Build](https://github.com/TheMarstonConnell/randomloot/actions/workflows/gradle.yml/badge.svg)](https://github.com/TheMarstonConnell/randomloot/actions/workflows/gradle.yml) [![](https://dcbadge.vercel.app/api/server/w7FDgqB?style=flat)](https://discord.gg/w7FDgqB)

Introducing Looting like you've never seen it before! Have you ever felt that Minecraft didn't have enough tools and weapons to make you happy? Are you dissatisfied with the low amount of character each tool has? Ever wanted your tools to get better as you use them? Yes?!? Well then this is the mod for you!

**Random Loot 2** was rewritten from the ground up to support Minecraft 1.20.x with the goals of creating an easier to maintain code-base with an expandable modifier system.

## Items
This mod adds two items, the Loot Case and the Random Tool. You'll never see the Random Tool called the Random Tool since every tool is randomly generated and uniquely named.

### Loot Case
You can find cases in any chest that generated in a structure of some kind (ex: dungeons, mineshafts, buried treasure). There is a 25% chance that opening a chest yields a case. Right-clicking with this case generates a new tool and removes the case from your inventory.

![case in inventory](https://raw.githubusercontent.com/TheMarstonConnell/randomloot/main/.github/assets/case_in_inv.png)

### Random Tool
Random Tools look like a variety of tool types and can be one of: pickaxes, shovels, axes, swords. These tools all generate with random traits. For every tool you generate by opening a case, the tool you generate will come with better stats and more traits.

![tools in inventory](https://raw.githubusercontent.com/TheMarstonConnell/randomloot/main/.github/assets/tools.png)

![tools with information](https://raw.githubusercontent.com/TheMarstonConnell/randomloot/main/.github/assets/info.png)

Holding Shift while hovering over tools will give you an expanded view on details about the tool and current status of traits.
![tools with shift information](https://raw.githubusercontent.com/TheMarstonConnell/randomloot/main/.github/assets/shift_info.png)

Holding Control (command on a mac) while hovering over tools will give you a description of every trait currently applied to the tool.
![tools with control information](https://raw.githubusercontent.com/TheMarstonConnell/randomloot/main/.github/assets/expanded_info.png)

#### Tool Modifiers
For a complete list of modifiers check out the [modifier list](https://github.com/TheMarstonConnell/randomloot/blob/main/MODIFIERS.md).

### Trait Addition/Subtraction Template
Trait Addition/Subtraction Templates are items that allow you to add and remove traits from your tools. To do this, place either an addition template or subtraction template inside a Smithing Table, to add or remove a trait. Then place in your tool and the corresponding item listed in the [Modifiers](https://github.com/TheMarstonConnell/randomloot/blob/main/MODIFIERS.md) list. Then You can preview what will happen to your tool.

You can find Trait Addition Templates in dungeon chests similarly to loot cases. To get Subtraction Templates, right click with an addition template in your hand. They can be swapped back and forth as many times as you'd like but once you use them they're gone.

### Automation

You can place cases in dispensers to be opened automatically. Be aware that cases opened by dispensers will not keep the global progress they would follow if opened by a player, essentially they will always be the worst version of a tool.

## Another Rewrite?
The jump from 1.12 to 1.16 was one of the biggest changes to Forge & the Minecraft codebase making a complete rewrite of the mod very welcome. However, the 1.16 to 1.20 is again, a massive change and I'm overall dissatisfied with the 1.16 version of the mods codebase and sloppy planning. As such, 1.20 is a complete rewrite of Random Loot to make the mod feel more cohesive and less janky.

## Changelog
Check the changelog [here](https://github.com/TheMarstonConnell/randomloot/blob/main/CHANGELOG.md)

## Credits
Thank you to Sprucefence, Xiruen, and Zorbyn for donating tool textures.
