package com.danwink.dngf;

public class DNGFInternalMessage 
{
	public DNGFInternalMessageType type;
	public Object o;
	
	public DNGFInternalMessage()
	{
		
	}
	
	public DNGFInternalMessage( DNGFInternalMessageType type, Object o )
	{
		this.type = type;
		this.o = o;
	}
	
	public enum DNGFInternalMessageType
	{
		LOBBY,
		PLAY
	}
}
