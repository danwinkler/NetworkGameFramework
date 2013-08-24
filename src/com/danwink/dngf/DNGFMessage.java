package com.danwink.dngf;

public class DNGFMessage<E extends Enum>
{
	public E type;
	public Object message;
	
	public DNGFMessage()
	{
		
	}
	
	public DNGFMessage( E type, Object message )
	{
		this.type = type;
		this.message = message;
	}
}
