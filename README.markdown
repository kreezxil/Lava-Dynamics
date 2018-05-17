# Lava Dynamics

Join me on Discord at https://discord.gg/BKqEewj

[![What it do!??](https://img.youtube.com/vi/lef94Sog708/0.jpg)](https://youtu.be/lef94Sog708?list=PLxL-reX9*y*4LaK5JKzcdNcd8JL0JStB6 "Lava Dynamics Demonstration")
 
**DO NOT ADD THIS MOD TO AN EXISTING SERVER WORLD OR SINGLE PLAYER WORLD IF YOU TREASURE YOUR BUILDS AND BASES**

## Description

 
The goal of this mod is to allow vulcanism to exist in your world. What it does is when lava begins flowing near the player it will check to see if the block being flowed over has a smeltable output and if it does it converts that block.

Flowing lava in the world now performs full in-world furnace smelting. If the furnace can make so can lava. However, when lava smelts it also ejects the smelted item with a force in the adjacent detected direction. Lava also has a chance to consume flammable blocks and if it does generate even more lava and even an explosion.

Configs have been added to allow you to manipulate everything in that has a value in the mod that I felt was needed to make it work for 1.12.2-2.0.7. You can tweak the values up and down as you see fit. If you find one that works really well for you, please share it in the comments so the rest of the community might benefit.

You can also control if Lava has access to furnace recipes. If you disallow it, then flowing lava can only consume flammables, generate more lava, and randomly explode if consumes flammables.

When added to large packs where blocks of many types can be smelted to other blocks of many types the possibilities are endless.

## Config Example

    # Configuration file

    ##########################################################################################################
    # conversions (mappings)
    #--------------------------------------------------------------------------------------------------------#
    # In World Smelting Controll for the lava
    ##########################################################################################################

    "conversions (mappings)" {
        # Enable/Disable using smelting recipes for Lava Dynamics
        B:furnaceRecipes=true

        # Enable/Disable consuming of partial blocks such as leaves, grass, crops, etc ...
        B:partialBlock=false
    }


    ##########################################################################################################
    # dimensions
    #--------------------------------------------------------------------------------------------------------#
    # True or False to control if LavaDynamics runs in said dimension
    ##########################################################################################################

    dimensions {
        # All that lava, probably you should allow it!
        B:Nether=false

        # It's honestly meant to run here
        B:OverWorld=true

        # I don't see why it can't run here
        B:"The End"=true
    }


    ##########################################################################################################
    # explosions
    #--------------------------------------------------------------------------------------------------------#
    # Volcanoes have explosive quailities, these settings are for that feature
    ##########################################################################################################

    explosions {
        # Integer value from 0 to 100 for determining the chance that lava causes an explosion.
        # Default: 5
        I:chanceExplosion=5

        # float value for determining strength of random explosions.
        # Default: 10.0
        D:maxExplosion=10.0

        # The minimum value for determining strenth of random explosions.
        # Default: 1.0
        D:minExplosion=1.0
    }


    ##########################################################################################################
    # general
    #--------------------------------------------------------------------------------------------------------#
    # General Settings
    ##########################################################################################################

    general {
        # If true it will print messages to the player based on what you are doing in the protected zone, useful for helping Kreezxil debug the mod
        B:debugMode=false
    }


    ##########################################################################################################
    # noise
    #--------------------------------------------------------------------------------------------------------#
    # Settings to help determine when walls and nodules get built
    ##########################################################################################################

    noise {
        # The Maximum value for determining how many blocks above the current block is also lava before generating a wall component.
        # Default: 2
        I:highNoise=2

        # The Minimum value for determining how many blocks above the current block is also lava before generating a wall component.
        # Default: 0
        I:lowNoise=0
    }


    ##########################################################################################################
    # oregen
    #--------------------------------------------------------------------------------------------------------#
    # The higher the value, the rarer all ores generating in the volcanic wall are. default: 500
    ##########################################################################################################

    oregen {
        # A standard would be 100, but that make ore too common, so 500 makes it all 5x rarer
        I:maxChance=500
    }


    ##########################################################################################################
    # plume height
    #--------------------------------------------------------------------------------------------------------#
    # the lower and higher extra amount to randomly add to a vent to set the size of a plume
    ##########################################################################################################

    "plume height" {
        # Extra amount of source blocks to add to the magma vent.
        # Default: 10
        I:extraHt=10

        # The minimum amount of source blocks to add to the magma vent.
        # Default: 3
        I:minHt=3
    }


    "volcano settings" {
        # The integer value from 0 to 100 for determining the chance that lava will spread.
        # Default: 10
        I:lavaSpread=10

        # The max y value to consider for turning lava into a magma vent.
        # Default: 10
        I:maxYlevel=10

        # 0 to 100 chance that part of the nodule is formed to protect the ore being generated
        I:nodulePartChance=10

        # if true any tile entity will prevent a volcano from erupting in a chunk. Signs are tile entities btw. Setting this to false enables hardcore volcanoes. Meaning a play must neutralize lava pools below them to be truly safe.
        B:protection=true

        # The y level for the approximate level of your surface.
        # Default: 69
        I:psuedoSurface=69

        # Percent chance a volcano or magma vent will occur.
        # Default: 20
        I:volcanoChance=20

        # You can play with this value, but it's not for you.
        # Default: seriously it will ignore you
        B:volcanoGen=false
    }

 

## Incompatibilities aka Mod Conflicts

**Storage Drawers** - one unexpected behavior is that a block will not enter the drawer but will instead place in front of it. Another is that it will enter the block and also place in front of it. Another is that when you harvest the storage drawer it will duplicate.

## Features

* We have volcanoes.
* We have magma vents.
* We have ore being generated in the cone walls.
* We have lots of lava.
* We have lots of config choices.
* In world smelting
* lots of explosions
* absolute hell on earth
## Todo

* add custom mapping conversions to the configs
* finish implement in-game commands for configuration purposes in real-time
* earthquakes
## Reviews

### English

[![Lava Dynamics English Review](http://img.youtube.com/vi/ZVEuEVeE3vU/0.jpg)](https://youtu.be/ZVEuEVeE3vU "Lava Dynamics Review by Dark_Moon67")

### Español

[![Lava Dynamics en Español](http://img.youtube.com/vi/LMUuHFK67Q0/0.jpg)](https://youtu.be/LMUuHFK67Q0 "Lava Dynamics en Español")
 

## Modpacks

Don't ask just add. A link back to this page would be appreciated but not required.

## Youtube - Twitch - Etc

Go ahead and please share the link so I can feature it.

## Contact

Other than the obvious methods provided by Curse. You can also chat with me live on Discord and Twitch at the following. The servers are public.

Discord: https://discord.gg/Nm9W3zw

## Donate

Help a Veteran today
I am Veteran of United States Army. I am not disabled. But I do love to make these mods and modpacks for you guys. Please help me to help you by Donating at [![Patreon](https://imgur.com/WIC5swq)](https://patreon.com/kreezxil "Donate to Kreezxil")

 

