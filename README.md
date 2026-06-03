
# SpellWords

Turn words and phrases into commands.

Create custom chat triggers that execute commands when players send specific messages.

## Features

* Trigger commands from chat messages
* Multiple trigger words per entry
* Per-trigger cooldown system
* Permission-based triggers ([LuckPerms](https://modrinth.com/plugin/luckperms) supported)
* Optional canceling of original chat message
* Cooldown and permission fallback commands
* Fully server-side
* Works with any language (including non-Latin scripts)

## Configuration

### Trigger Properties

| Property        | Description                                                         | Default |
| --------------- | ------------------------------------------------------------------- | ------- |
| words           | List of words or phrases that activate the trigger                  | `[]`    |
| command         | Command executed when triggered (`{player}` supported)              | `""`    |
| cooldownCommand | Command executed if trigger is on cooldown (`{cooldown}` supported) | `""`    |
| denyCommand     | Command executed if player lacks permission                         | `""`    |
| exactMatch      | Require full message match                                          | `false` |
| ignoreCase      | Case-insensitive matching                                           | `true`  |
| cancelMessage   | Cancel original chat message                                        | `false` |
| cooldownSeconds | Cooldown time in seconds                                            | `0`     |
| runAsConsole    | Execute command as server/console                                   | `false` |
| permission      | Optional permission node                                            | `""`    |

## Example

```json
{
  "triggers": [
    {
      "words": ["fireball"],
      "command": "execute as {player} at @s run summon fireball ~ ~1 ~ {ExplosionPower:30}",
      "cooldownCommand": "tellraw {player} \"Wait {cooldown} seconds before using this spell again.\"",
      "denyCommand": "tellraw {player} \"You are not a magician!\"",
      "exactMatch": true,
      "ignoreCase": true,
      "cancelMessage": true,
      "cooldownSeconds": 10,
      "runAsConsole": true,
      "permission": "spellwords.fireball"
    },
    {
      "words": ["badword", "anotherbadword", "плохое слово"],
      "command": "tellraw {player} \"Sorry, but that word is banned!\"",
      "cooldownCommand": "",
      "denyCommand": "",
      "exactMatch": false,
      "ignoreCase": true,
      "cancelMessage": true,
      "cooldownSeconds": 0,
      "runAsConsole": true,
      "permission": ""
    },
    {
      "words": ["AvAdA kAdAbRa"],
      "command": "kill {player}",
      "cooldownCommand": "",
      "denyCommand": "",
      "exactMatch": true,
      "ignoreCase": true,
      "cancelMessage": false,
      "cooldownSeconds": 0,
      "runAsConsole": true,
      "permission": "spellwords.awadakadabra"
    }
  ]
}
```

## Note

This mod was originally created for a personal server and is shared publicly for anyone who wants to use it.

Updates are primarily driven by server needs.

Backports to older Minecraft versions are not planned, but may happen occasionally.

Feel free to report bugs, issues, or suggestions.

Porting to other Minecraft versions is allowed and welcome.
