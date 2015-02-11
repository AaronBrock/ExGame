package me.theminebench.exgame.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;

import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.NetworkManager;
import net.minecraft.server.v1_8_R1.PlayerConnection;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

public class PlayerInjector extends ChannelDuplexHandler {

	// cached fields
	private static Field CHANNEL_FIELD;

	private PlayerConnection playerConnection;
	private NetworkManager networkManager;

	private Player player;

	private Channel channel;

	private boolean isInjected;
	private boolean isClosed;

	public PlayerInjector(Player player) {
		Preconditions.checkNotNull(player, "Player cannot be NULL!");
		this.init(player);
		this.initChannel();
	}

	private void init(Player player) {
		this.player = player;

		EntityPlayer craftPlayer = ((CraftPlayer) player).getHandle();
		this.playerConnection = craftPlayer.playerConnection;
		this.networkManager = this.playerConnection.networkManager;
	}

	private void initChannel() {
		initializeFields();
		try {
			this.channel = (Channel) CHANNEL_FIELD.get(this.networkManager);
		} catch (Exception e) {
			throw new RuntimeException("Failed to get the channel for player: " + this.player.getName(), e);
		}
	}

	public void inject() {
		synchronized (this.networkManager) {
			if (this.isInjected)
				throw new IllegalStateException("Cannot inject twice!");

			if (this.channel == null)
				throw new IllegalStateException("Channel is NULL!");

			try {
				this.channel.pipeline().addBefore("packet_handler", "disguise_packet_handler", this);
			} catch (NoSuchElementException e){
				//ignore since it sometimes errors here
			}

			this.isInjected = true;
		}
	}

	public boolean isInjected() {
		return this.isInjected;
	}

	public boolean isOpen() {
		return !this.isClosed;
	}

	public void sendPacket(Object packet) {
		if (!this.isOpen())
			throw new IllegalStateException("PlayerInjector is closed!");
		this.channel.writeAndFlush(packet);
	}

	public void receivePacket(Object packet) {
		Bukkit.broadcastMessage(packet.toString());
		if (!this.isOpen())
			throw new IllegalStateException("PlayerInjector is closed!");
		this.channel.pipeline().context("encoder").fireChannelRead(packet);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
	}
	/*
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
	}
	*/
	private static void initializeFields() {
		if (CHANNEL_FIELD != null) 
			return;

		Class<NetworkManager> networkManagerClass = NetworkManager.class;
		for (Field field : networkManagerClass.getDeclaredFields()) {
			if (field.getType().equals(Channel.class)) {
				CHANNEL_FIELD = field;
				CHANNEL_FIELD.setAccessible(true);
				break; 
			}
		}
	}
}