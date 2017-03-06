# Approach to assets management
## Rationale

In Friendly Plans app pictures are used as icons and in connection with tasks or steps. Each task or step can (but don't have to) have a picture assigned. When child is using the app and is doing certain task or step, proper picture is being displayed. Sounds are used in a similar way - they can be connected with task or step. Assets come from the device storage (therapist can download them from the Internet earlier, make a photo or copy it from other device, etc.) and they have to be in one of image/sound formats supported by Android (JPEG, PNG, GIF, BMP, 3GP, MP3, MIDI, OGG, WAV). Link between asset and task, step, etc. is created during concrete element creation/edition process. Because life cycle of the Friendly Plans app cannot be directly connected with every linked asset's life cycle, the right approach to assets management is needed to avoid situations when after asset rename or deletion app can't display needed picture or play sound any more.

## Approach description

To ensure that every asset connected with the app will be always available and won't change its name, we make our safe copy of it. Making the copy is part of the concrete element creation/edition process - app make a copy right after user assign asset to the task/step. Copied asset gets its own unique file name (SHA-256 hash from original name and some random value) and unified extension (e.g. 'JPG' is going to be 'jpg'). Adding random part is necessary to avoid colision between two different assets with the same name (e.g. files from different locations or after rename). All safe copies are being stored in well defined path as app related files. All files names (with extension) are stored in asset table in db and link between tasks, steps, etc. and asset is stored as foreign key reference to that table. If some additional information (e.g. human readable asset name) will be needed, it will be stored in asset table with proper asset. After deletion of link between app's element and asset, copy of asset is going to be removed from the device and connected row from asset table too.

## Additional info

Random generator: java.util.Random class seeded with current timestamp
Hash algorithm: SHA-256
Input for hash pattern: {original name}{random value}

Copied filename pattern: {original name hash}.{mapped extension}

Path to copied files directory pattern: /{primary storage}/FriendlyPlans/{app version}/Assets
- primary storage -> Android's primary storage on concrete device
- app version -> Friendly Plans version

Full path to copied file example: /storage/sdcard0/FriendlyPlans/2/Assets/7d5081c15fbb3a697d9891f84038fc68.jpg

Extensions mappings (original -> mapped):
- In general mapped extension will have all letters in small case
- JPG -> jpg
- JPEG -> jpg
- jpg -> jpg
- jpeg -> jpg
- PNG -> png
- png -> png
- GIF -> gif
- gif -> gif
- BMP -> bmp
- bmp -> bmp
- 3GP -> 3gp
- 3gp -> 3gp
- MP3 -> mp3
- mp3 -> mp3

## Concepts to discuss
### Reuse saved assets

If we would like to decrease storage size needed for all copied assets, we can consider reuse of existing copies. For example, app can ask about using assets from "previously used" (already copied) or new ones. Of course it makes sense only when same asset is being used lot of times and when needed storage size became a problem.

### Link deletion behaviour

Alternative approach to deletion of link between app element (task, step) and asset can be removing only foreign key reference from db and not to remove copied asset from storage. Then user can easily restore it using "choose asset from previously used".
