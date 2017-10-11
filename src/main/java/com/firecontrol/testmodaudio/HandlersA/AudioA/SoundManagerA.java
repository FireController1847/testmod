package com.firecontrol.testmodaudio.HandlersA.AudioA;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import com.firecontrol.testmodaudio.TestModA;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.SoundSystemLogger;
import paulscode.sound.Source;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;
import scala.concurrent.forkjoin.ThreadLocalRandom;

public class SoundManagerA {

	/** The marker used for logging */
	private static final Marker LOG_MARKER = MarkerManager.getMarker("SOUNDS");
	private static final Set<ResourceLocation> UNABLE_TO_PLAY = Sets.<ResourceLocation>newHashSet();
	/** A reference to the sound handler. */
	public final SoundHandlerA sndHandler;
	/** Reference to the GameSettings object. */
	private final GameSettings options;
	/** A reference to the sound system. */
	private SoundManagerA.SoundSystemStarterThread sndSystem;
	/** Set to true when the SoundManager has been initialised. */
	private boolean loaded;
	/** A counter for how long the sound manager has been running */
	private int playTime;
	/**
	 * Identifiers of all currently playing sounds. Type: HashBiMap<String, ISound>
	 */
	private final Map<String, ISoundA> playingSounds = HashBiMap.<String, ISoundA>create();
	/**
	 * Inverse map of currently playing sounds, automatically mirroring changes in
	 * original map
	 */
	private final Map<ISoundA, String> invPlayingSounds;
	private final Multimap<SoundCategory, String> categorySounds;
	/** A subset of playingSounds, this contains only ITickableSoundAs */
	private final List<ITickableSoundA> tickableSounds;
	/** Contains sounds to play in n ticks. Type: HashMap<ISound, Integer> */
	private final Map<ISoundA, Integer> delayedSounds;
	/**
	 * The future time in which to stop this sound. Type: HashMap<String, Integer>
	 */
	private final Map<String, Integer> playingSoundsStopTime;
	private final List<ISoundAEventListener> listeners;
	private final List<String> pausedChannels;

	public boolean isNewSoundManager = true;

	public SoundManagerA(SoundHandlerA handler, GameSettings options) {
		this.invPlayingSounds = ((BiMap) this.playingSounds).inverse();
		this.categorySounds = HashMultimap.<SoundCategory, String>create();
		this.tickableSounds = Lists.<ITickableSoundA>newArrayList();
		this.delayedSounds = Maps.<ISoundA, Integer>newHashMap();
		this.playingSoundsStopTime = Maps.<String, Integer>newHashMap();
		this.listeners = Lists.<ISoundAEventListener>newArrayList();
		this.pausedChannels = Lists.<String>newArrayList();
		this.sndHandler = handler;
		this.options = options;
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			// MinecraftForge.EVENT_BUS.post(new SoundSetupEvent(this));
		} catch (SoundSystemException e) {
			TestModA.logger.error(LOG_MARKER, "Error linking with the LibraryJavaSound plug-in", (Throwable) e);
		}
	}

	public void reloadSoundSystem() {
		UNABLE_TO_PLAY.clear();

		for (SoundEvent soundevent : SoundEvent.REGISTRY) {
			ResourceLocation resourcelocation = soundevent.getSoundName();

			if (this.sndHandler.getAccessor(resourcelocation) == null) {
				TestModA.logger.warn("Missing sound for event: {}", SoundEvent.REGISTRY.getNameForObject(soundevent));
				UNABLE_TO_PLAY.add(resourcelocation);
			}
		}

		this.unloadSoundSystem();
		this.loadSoundSystem();
	}

	private synchronized void loadSoundSystem() {
		if (!this.loaded) {
			try {
				(new Thread(new Runnable() {
					public void run() {
						SoundSystemConfig.setLogger(new SoundSystemLogger() {
							public void message(String p_message_1_, int p_message_2_) {
								if (!p_message_1_.isEmpty()) {
									TestModA.logger.info(p_message_1_);
								}
							}

							public void importantMessage(String p_importantMessage_1_, int p_importantMessage_2_) {
								if (!p_importantMessage_1_.isEmpty()) {
									TestModA.logger.warn(p_importantMessage_1_);
								}
							}

							public void errorMessage(String p_errorMessage_1_, String p_errorMessage_2_,
									int p_errorMessage_3_) {
								if (!p_errorMessage_2_.isEmpty()) {
									TestModA.logger.error("Error in class '{}'", (Object) p_errorMessage_1_);
									TestModA.logger.error(p_errorMessage_2_);
								}
							}
						});
						SoundManagerA.this.sndSystem = SoundManagerA.this.new SoundSystemStarterThread();
						SoundManagerA.this.loaded = true;
						SoundManagerA.this.sndSystem
								.setMasterVolume(SoundManagerA.this.options.getSoundLevel(SoundCategory.MASTER));
						TestModA.logger.info(SoundManagerA.LOG_MARKER, "Sound engine started");
					}
				}, "Sound Library Loader")).start();
			} catch (RuntimeException e) {
				TestModA.logger.error(LOG_MARKER, "Error starting SoundSystem. Turning off sounds & music",
						(Throwable) e);
				this.options.setSoundLevel(SoundCategory.MASTER, 0.0F);
				this.options.saveOptions();
			}
		}
	}

	private float getVolume(SoundCategory category) {
		return category != null && category != SoundCategory.MASTER ? this.options.getSoundLevel(category) : 1.0F;
	}

	public void setVolume(SoundCategory cat, float vol) {
		if (!this.loaded)
			return;
		if (cat == SoundCategory.MASTER) {
			this.sndSystem.setMasterVolume(vol);
		} else {
			for (String s : this.categorySounds.get(cat)) {
				ISoundA isound = this.playingSounds.get(s);
				float f = this.getClampedVolume(isound);
				if (f <= 0.0F) {
					this.stopSound(isound);
				} else {
					this.sndSystem.setVolume(s, f);
				}
			}
		}
	}

	public void unloadSoundSystem() {
		if (!this.loaded)
			return;
		this.stopAllSounds();
		this.sndSystem.cleanup();
		this.loaded = false;
	}

	public void stopAllSounds() {
		if (!this.loaded)
			return;
		for (String s : this.playingSounds.keySet()) {
			this.sndSystem.stop(s);
		}

		this.pausedChannels.clear();
		this.playingSounds.clear();
		this.delayedSounds.clear();
		this.tickableSounds.clear();
		this.categorySounds.clear();
		this.playingSoundsStopTime.clear();
	}

	public void addListener(ISoundAEventListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(ISoundAEventListener listener) {
		this.listeners.remove(listener);
	}

	public void updateAllSounds() {
		this.playTime++;
		for (ITickableSoundA its : this.tickableSounds) {
			its.update();
			if (its.isDonePlaying()) {
				this.stopSound(its);
			} else {
				String s = this.invPlayingSounds.get(its);
				this.sndSystem.setVolume(s, this.getClampedVolume(its));
				this.sndSystem.setPitch(s, this.getClampedPitch(its));
				this.sndSystem.setPosition(s, its.getXPosF(), its.getYPosF(), its.getZPosF());
			}
		}

		Iterator<Entry<String, ISoundA>> iterator = this.playingSounds.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, ISoundA> entry = (Entry) iterator.next();
			String s1 = entry.getKey();
			ISoundA isound = entry.getValue();

			if (!this.sndSystem.playing(s1)) {
				int i = ((Integer) this.playingSoundsStopTime.get(s1)).intValue();

				if (i <= this.playTime) {
					int j = isound.getRepeatDelay();

					if (isound.canRepeat() && j > 0) {
						this.delayedSounds.put(isound, Integer.valueOf(this.playTime + j));
					}

					iterator.remove();
					TestModA.logger.debug(LOG_MARKER, "Removed channel {} because it's not playing anymore",
							(Object) s1);
					this.sndSystem.removeSource(s1);
					this.playingSoundsStopTime.remove(s1);

					try {
						this.categorySounds.remove(isound.getCategory(), s1);
					} catch (RuntimeException var8) {
						// Do Nothing
					}

					if (isound instanceof ITickableSoundA) {
						this.tickableSounds.remove(isound);
					}
				}
			}
		}

		Iterator<Entry<ISoundA, Integer>> iterator1 = this.delayedSounds.entrySet().iterator();

		while (iterator1.hasNext()) {
			Entry<ISoundA, Integer> entry1 = (Entry) iterator1.next();

			if (this.playTime >= ((Integer) entry1.getValue()).intValue()) {
				ISoundA isound1 = entry1.getKey();

				if (isound1 instanceof ITickableSoundA) {
					((ITickableSoundA) isound1).update();
				}

				this.playSound(isound1);
				iterator1.remove();
			}
		}
	}

	public boolean isSoundPlaying(ISoundA sound) {
		if (!this.loaded) {
			return false;
		} else {
			String s = this.invPlayingSounds.get(sound);
			if (s == null) {
				return false;
			} else {
				return this.sndSystem.playing(s) || this.playingSoundsStopTime.containsKey(s)
						&& ((Integer) this.playingSoundsStopTime.get(s)).intValue() <= this.playTime;
			}
		}
	}

	public void stopSound(ISoundA sound) {
		if (this.loaded) {
			String s = this.invPlayingSounds.get(sound);
			if (s != null) {
				this.sndSystem.stop(s);
			}
		}
	}

	public void playSound(ISoundA p_sound) {
		if (this.loaded) {
			if (p_sound == null)
				return;
			SoundEventAccessor sea = p_sound.createAccessor(this.sndHandler);
			ResourceLocation rl = p_sound.getSoundLocation();
			if (sea == null) {
				if (UNABLE_TO_PLAY.add(rl)) {
					TestModA.logger.warn(LOG_MARKER, "Unable to play unknown soundEvent: {}", (Object) rl);
				}
			} else {
				if (!this.listeners.isEmpty()) {
					for (ISoundAEventListener isel : this.listeners) {
						isel.soundPlay(p_sound, sea);
					}
				}
				if (this.sndSystem.getMasterVolume() <= 0.0F) {
					TestModA.logger.debug(LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero",
							(Object) rl);
				} else {
					Sound sound = p_sound.getSound();
					if (sound == SoundHandler.MISSING_SOUND) {
						if (UNABLE_TO_PLAY.add(rl)) {
							TestModA.logger.warn(LOG_MARKER, "Unable to play empty soundEvent: {}", (Object) rl);
						}
					} else {
						float f3 = p_sound.getVolume();
						float f = 16.0F;
						if (f3 > 1.0F) {
							f *= f3;
						}

						SoundCategory sc = p_sound.getCategory();
						float f1 = this.getClampedVolume(p_sound);
						float f2 = this.getClampedPitch(p_sound);

						if (f1 == 0.0F) {
							TestModA.logger.debug(LOG_MARKER, "Skipped playing sound {}, volume was zero.",
									(Object) sound.getSoundLocation());
						} else {
							boolean flag = p_sound.canRepeat() && p_sound.getRepeatDelay() == 0;
							String s = MathHelper.getRandomUUID(ThreadLocalRandom.current()).toString();
							ResourceLocation rl1 = sound.getSoundAsOggLocation();

							if (sound.isStreaming()) {
								this.sndSystem.newStreamingSource(false, s, getURLForSoundResource(rl1), rl1.toString(),
										flag, p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF(),
										p_sound.getAttenuationType().getTypeInt(), f);
							} else {
								this.sndSystem.newSource(false, s, getURLForSoundResource(rl1), rl1.toString(), flag,
										p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF(),
										p_sound.getAttenuationType().getTypeInt(), f);
							}

							TestModA.logger.debug(LOG_MARKER, "Playing sound {} for event {} as channel {}",
									sound.getSoundLocation(), rl, s);
							this.sndSystem.setPitch(s, f2);
							this.sndSystem.setVolume(s, f1);
							this.sndSystem.play(s);
							this.playingSoundsStopTime.put(s, Integer.valueOf(this.playTime + 20));
							this.playingSounds.put(s, p_sound);
							this.categorySounds.put(sc, s);

							if (p_sound instanceof ITickableSoundA) {
								this.tickableSounds.add((ITickableSoundA) p_sound);
							}
						}
					}
				}
			}
		}
	}

	private float getClampedPitch(ISoundA soundIn) {
		return MathHelper.clamp(soundIn.getPitch(), 0.5F, 2.0F);
	}

	private float getClampedVolume(ISoundA soundIn) {
		return MathHelper.clamp(soundIn.getVolume() * this.getVolume(soundIn.getCategory()), 0.0F, 1.0F);
	}

	public void pauseAllSounds() {
		for (Entry<String, ISoundA> entry : this.playingSounds.entrySet()) {
			String s = entry.getKey();
			boolean flag = this.isSoundPlaying(entry.getValue());

			if (flag) {
				TestModA.logger.debug(LOG_MARKER, "Pausing channel {}", (Object) s);
				this.sndSystem.pause(s);
				this.pausedChannels.add(s);
			}
		}
	}

	public void resumeAllSounds() {
		for (String s : this.pausedChannels) {
			TestModA.logger.debug(LOG_MARKER, "Resuming channel {}", (Object) s);
			this.sndSystem.play(s);
		}

		this.pausedChannels.clear();
	}

	public void playDelayedSound(ISoundA sound, int delay) {
		this.delayedSounds.put(sound, Integer.valueOf(this.playTime + delay));
	}

	private static URL getURLForSoundResource(final ResourceLocation p_148612_0_) {
		String s = String.format("%s:%s:%s", "mcsounddomain", p_148612_0_.getResourceDomain(),
				p_148612_0_.getResourcePath());
		URLStreamHandler urlstreamhandler = new URLStreamHandler() {
			protected URLConnection openConnection(URL p_openConnection_1_) {
				return new URLConnection(p_openConnection_1_) {
					public void connect() throws IOException {
					}

					public InputStream getInputStream() throws IOException {
						return Minecraft.getMinecraft().getResourceManager().getResource(p_148612_0_).getInputStream();
					}
				};
			}
		};

		try {
			return new URL((URL) null, s, urlstreamhandler);
		} catch (MalformedURLException var4) {
			throw new Error("TODO: Sanely handle url exception! :D");
		}
	}

	public void setListener(EntityPlayer player, float p_148615_2_) {
		if (this.loaded && player != null) {
			float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * p_148615_2_;
			float f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * p_148615_2_;
			double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) p_148615_2_;
			double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) p_148615_2_
					+ (double) player.getEyeHeight();
			double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) p_148615_2_;
			float f2 = MathHelper.cos((f1 + 90.0F) * 0.017453292F);
			float f3 = MathHelper.sin((f1 + 90.0F) * 0.017453292F);
			float f4 = MathHelper.cos(-f * 0.017453292F);
			float f5 = MathHelper.sin(-f * 0.017453292F);
			float f6 = MathHelper.cos((-f + 90.0F) * 0.017453292F);
			float f7 = MathHelper.sin((-f + 90.0F) * 0.017453292F);
			float f8 = f2 * f4;
			float f9 = f3 * f4;
			float f10 = f2 * f6;
			float f11 = f3 * f6;
			this.sndSystem.setListenerPosition((float) d0, (float) d1, (float) d2);
			this.sndSystem.setListenerOrientation(f8, f5, f9, f10, f7, f11);
		}
	}

	public void stop(String p_189567_1_, SoundCategory p_189567_2_) {
		if (p_189567_2_ != null) {
			for (String s : this.categorySounds.get(p_189567_2_)) {
				ISoundA isound = this.playingSounds.get(s);

				if (p_189567_1_.isEmpty()) {
					this.stopSound(isound);
				} else if (isound.getSoundLocation().equals(new ResourceLocation(p_189567_1_))) {
					this.stopSound(isound);
				}
			}
		} else if (p_189567_1_.isEmpty()) {
			this.stopAllSounds();
		} else {
			for (ISoundA isound1 : this.playingSounds.values()) {
				if (isound1.getSoundLocation().equals(new ResourceLocation(p_189567_1_))) {
					this.stopSound(isound1);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	class SoundSystemStarterThread extends SoundSystem {
		private SoundSystemStarterThread() {
		}

		public boolean playing(String p_playing_1_) {
			synchronized (SoundSystemConfig.THREAD_SYNC) {
				if (this.soundLibrary == null) {
					return false;
				} else {
					Source source = (Source) this.soundLibrary.getSources().get(p_playing_1_);

					if (source == null) {
						return false;
					} else {
						return source.playing() || source.paused() || source.preLoad;
					}
				}
			}
		}
	}

}
