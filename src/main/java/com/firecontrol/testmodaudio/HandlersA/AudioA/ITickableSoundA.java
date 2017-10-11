package com.firecontrol.testmodaudio.HandlersA.AudioA;

import net.minecraft.util.ITickable;

public interface ITickableSoundA extends ISoundA, ITickable {
	boolean isDonePlaying();
}
