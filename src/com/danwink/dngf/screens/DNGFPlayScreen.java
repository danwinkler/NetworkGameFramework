package com.danwink.dngf.screens;

import com.danwink.dngf.DNGFClient;
import com.danwink.dngf.DNGFInternalMessage;
import com.danwink.dngf.DNGFMessage;
import com.phyloa.dlib.network.DMessage;
import com.phyloa.dlib.renderer.DScreen;
import com.phyloa.dlib.renderer.DScreenHandler;
import com.phyloa.dlib.renderer.Graphics2DRenderer;

public class DNGFPlayScreen extends DScreen<DNGFClient, Graphics2DRenderer>
{

	public void update( DNGFClient gc, int delta ) 
	{
		if( gc.client.hasClientMessages() )
		{
			DMessage m = gc.client.getNextClientMessage();
			if( m.message instanceof DNGFInternalMessage )
			{
				
			}
			else
			{
				DNGFMessage message = (DNGFMessage)m.message;
				gc.handleMessage( message.type, message.message );
			}
		}
		
		gc.update( delta / 1000.f );
	}

	public void render( DNGFClient gc, Graphics2DRenderer g )
	{
		
	}

	public void onActivate( DNGFClient gc, DScreenHandler<DNGFClient, Graphics2DRenderer> dsh )
	{
		gc.clientStart();
	}

	public void onExit() 
	{
		gc.reset();
	}

	public void message( Object o )
	{
		
	}
}
