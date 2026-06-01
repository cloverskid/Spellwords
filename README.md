# SpellWords

Turn words and phrases into commands.

Create custom chat triggers that execute commands when players send specific messages.

## Features

* Trigger commands from chat messages
* Multiple trigger words per entry
* Per-trigger cooldowns
* Optional exact-match mode
* Optional partial-match mode
* Case-sensitive or case-insensitive matching
* Execute commands as the player
* Optionally cancel the original chat message
* Server-side only
* Supports any language, including non-Latin alphabets

## Configuration

### Trigger Properties

| Property        | Description                                       |
| --------------- | ------------------------------------------------- |
| words           | List of words or phrases that trigger the command |
| command         | Command to execute                                |
| exactMatch      | Require the entire message to match               |
| ignoreCase      | Ignore upper/lower case                           |
| cancelMessage   | Prevent the original chat message from being sent |
| cooldownSeconds | Cooldown in seconds                               |

### Example

```json
{
  "triggers": [
    {
      "words": ["fireball"],
      "command": "summon fireball ~ ~1 ~ {ExplosionPower:30}",
      "exactMatch": false,
      "ignoreCase": true,
      "cancelMessage": true,
      "cooldownSeconds": 10
    },
    {
      "words": [
        "badword",
        "anotherbadword",
        "плохое слово"
      ],
      "command": "tellraw @s \"Sorry, but that word is banned!\"",
      "exactMatch": true,
      "ignoreCase": false,
      "cancelMessage": true,
      "cooldownSeconds": 0
    },
    {
      "words": [
        "AvAdA kAdAbRa"
      ],
      "command": "kill @s",
      "exactMatch": true,
      "ignoreCase": true,
      "cancelMessage": false,
      "cooldownSeconds": 0
    }
  ]
}
```

## Note

This mod was originally created for my own server and is shared publicly for anyone who wants to use it.

Updates are primarily driven by the needs of my server.

Backports to older Minecraft versions are not planned.

Feel free to report bugs, issues, or suggestions.

Anyone is welcome to port this mod to other Minecraft versions.
