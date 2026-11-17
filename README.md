# Armor Hide

NeoForge **1.21.1** mod that adds an inventory toggle to hide your worn armor.
The hide flag is stored on the server (data attachment) and synced to every client
that can see you, so other players render you without armor when it is hidden.

## Requirements

- Java **21**
- NeoForge for Minecraft **1.21.1** (MDK pulls this via Gradle)

## Build

```bash
./gradlew build
```

The mod jar is written to `build/libs/armorhide-1.0.0.jar`.

## Run client (dev)

```bash
./gradlew runClient
```

Open the survival or creative inventory and use **Hide Armor** / **Show Armor**.

## Run dedicated server (dev)

```bash
./gradlew runServer
```

## How it works (short)

1. **Inventory button** — client adds a toggle on the inventory / creative screens and sends a C2S packet.
2. **Server attachment** — server flips a persisted `armor_hidden` player attachment and broadcasts a S2C sync to the player and trackers.
3. **Render hook** — a client mixin skips `HumanoidArmorLayer` when the synced flag is true.

See `docs` in the Project Context store for a slightly longer architecture note if present.
