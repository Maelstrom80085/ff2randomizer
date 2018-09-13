import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Collections;
import java.util.ArrayList;

public class FF2Randomizer {
	
	public static void main (String[] args) throws Exception {
		// Load ROM
		File f = new File("Final Fantasy II (Japan) [En by Demiforce v1.03] [Title Fix by Parasyte v1.0].nes");
		byte[] rom = new byte[(int) f.length()];
		FileInputStream fis = new FileInputStream(f);
		fis.read(rom);
		fis.close();
		
		ArrayList<Byte> l = new ArrayList<>();
		
		// TODO: Apply randomizer bar 50% to 500%
		
		// TODO: Randomize character stats
		
		// TODO: Randomize character starting equipment

		// Randomize all shops
		// i.e. randomize odd bytes starting 3861D to 386FC
		for (int i = 0x3861D; i < 0x386FC; i += 2) {
			l.add(rom[i]);
		}
		Collections.shuffle(l);
		for (int i = 0x3861D; i < 0x386FC; i += 2) {
			rom[i] = l.remove(0);
		}
		
		// TODO: Adjust item sell values accordingly, otherwise infinite money :P
		
		// TODO: Randomize item codes, this will probably take care of the sell bug
		
		// TODO: Do not randomize item shop
		
		// TODO: Do not include Jade shop
		
		// TODO: Randomize shop items to be anything
		
		// Randomize all item chests
		// i.e. randomize 256 bytes starting C10
		for (int i = 0xC10; i < (0xC10 + 256); i++) {
			l.add(rom[i]);
		}
		Collections.shuffle(l);
		for (int i = 0xC10; i < (0xC10 + 256); i++) {
			rom[i] = l.remove(0);
		}
		
		// TODO: Include money chests in randomizer
		
		// TODO: Randomize the amount of cash found in the chests
		
		// TODO: Randomize shops and chests together
		
		// Sanitize progression items 
		// Method 1 - Exclude key items from randomization
		// i.e. item codes 00-0F should be present at offsets 00-0F respectively
		for (int code = 0; code <= 15; code++) {
			for (int i = 0xC10; i < (0xC10 + 256); i++) {
				if (rom[i] == code) {
					byte temp = rom[0xC10 + code];
					rom[0xC10 + code] = (byte) code;
					rom[i] = temp;
				}
			}
		}
		
		// Sanitize progression items 
		// TODO: Method 2 - Check if key item appears "before", otherwise guarantee it
		
		// Randomize monster AI
		// i.e. randomize every 1st byte starting 307D3 for 128 monsters
		for (int i = 0x307D3; i < (0x307D3 + 128*10); i += 10) {
			l.add(rom[i]);
		}
		Collections.shuffle(l);
		for (int i = 0x307D3; i < (0x307D3 + 128*10); i += 10) {
			rom[i] = l.remove(0);
		}
		
		// Give 50 MP to monsters that have none
		// i.e. set byte 30CD3 to 32
		rom[0x30CD3] = 0x32;
		
		// Randomize monster effect-on-attack
		// i.e. randomize lower nibble of every 5th byte starting 307D3 for 128 mons
		for (int i = 0x307D3; i < (0x307D3 + 128*10); i += 10) {
			l.add((byte)(rom[i + 4] & 0x0F));
		}
		Collections.shuffle(l);
		for (int i = 0x307D3; i < (0x307D3 + 128*10); i += 10) {
			rom[i + 4] = (byte)((rom[i + 4] & 0xF0) + l.remove(0));
		}
		
		// Randomize drop tables
		// i.e. randomize 128 bytes starting 17790		
		for (int i = 0x17790; i < (0x17790 + 128); i++) {
			l.add(rom[i]);
		}
		Collections.shuffle(l);
		for (int i = 0x17790; i < (0x17790 + 128); i++) {
			rom[i] = l.remove(0);
		}
		
		// TODO: Add constant and multiplier to drop Gil amounts
		
		// TODO: Randomize monster palettes
		
		// TODO: Randomize party palettes
		
		// TODO: Randomize ambush closets
		
		// ENHANCEMENTS
		
		// Dash
		rom[0x3D41E] = 2;
		rom[0x3D4F2] = 2;
		
		// TODO: Life is 100% accurate
		// i.e. Change 0x3067C from 32 (50%) to 64 (100%)
		rom[0x3067D] = (byte) 0x64;
		
		// Esuna is 100% accurate
		// i.e. Change 0x3068B from 32 (50%) to 64 (100%)
		rom[0x3068B] = (byte) 0x64;
		
		// Esuna cures all permanent status in battle
		// i.e. Change 0x3371E from 8 (check for 8 successes) to 1
		rom[0x3371E] = 1;
		
		// Esuna cures all permanent status out of battle
		// i.e. Change 0x3A760 from A5 (LDA zero page) to A9 (LDA immediate)
		// & change 0x3A761 from 87 (address) to 7E (all status)
		// & same changes for 0x0x3A754
		// & change 0x3A749 from 25 (AND zero page) to 29 (AND immediate)
		// & change 0x3A750 from 87 (address) to 7E (all status)
		rom[0x3A749] = 0x29;
		rom[0x3A74A] = 0x7E;
		rom[0x3A754] = (byte) 0xA9;
		rom[0x3A755] = 0x7E;
		rom[0x3A760] = (byte) 0xA9;
		rom[0x3A761] = 0x7E;
		
		// TODO: Shields have no magic penalty
		
		// Blink, Protect, Shell, Barrier, Wall have 100% accuracy
		rom[0x30692] = 0x64; // Barrier
		rom[0x30699] = 0x64; // Blink
		rom[0x306a0] = 0x64; // Protect
		rom[0x306A7] = 0x64; // Shell
		rom[0x306AE] = 0x64; // Wall
		
		// TODO: Stats have 1/16 chance of increment
		
		// TODO: Agility chances higher
		
		// TODO: Something about magic penalty
		
		// TODO: Blink raises eva%, Shell raises mdef% ?????
		
		// BUGFIXES
		
		// Bug: Stat and exp gains negated if afflicted with permanent status
		// Fix: Change 0x16562 from 0xFE (all permanent status) to 0xC0 (Only KO and Stone)
		rom[0x16562] = (byte) 0xC0;
		
		// Bug: Status afflictions remain after death
		// Fix: Change 0x33112 from 09 (ORA instruction) to A9 (LDA instruction)
		rom[0x33112] = (byte) 0xA9;
		
		// TODO: Bug: Protect has no effect except on the caster
		
		// TODO: Bug: Multi-target spells by enemies increase Firion's soul instead of everyone's MDef exp
		
		// TODO: Dispel doesn't work
		
		// Write processed ROM
		FileOutputStream fos = new FileOutputStream("ff2r.nes");
		fos.write(rom);
		fos.flush();
		fos.close();
	}

}
