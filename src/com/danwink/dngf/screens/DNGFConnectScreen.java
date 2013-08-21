package com.danwink.dngf.screens;

import java.awt.event.KeyEvent;
import java.io.IOException;

import com.danwink.dngf.DNGFClient;
import com.danwink.dngf.DNGFInternalMessage;
import com.danwink.dngf.DNGFInternalMessage.DNGFInternalMessageType;
import com.phyloa.dlib.dui.AWTComponentEventMapper;
import com.phyloa.dlib.dui.DButton;
import com.phyloa.dlib.dui.DTextBox;
import com.phyloa.dlib.dui.DUI;
import com.phyloa.dlib.dui.DUIElement;
import com.phyloa.dlib.dui.DUIEvent;
import com.phyloa.dlib.dui.DUIListener;
import com.phyloa.dlib.network.DClient;
import com.phyloa.dlib.network.DMessage;
import com.phyloa.dlib.network.DMessageType;
import com.phyloa.dlib.renderer.DScreen;
import com.phyloa.dlib.renderer.DScreenHandler;
import com.phyloa.dlib.renderer.Graphics2DRenderer;

public class DNGFConnectScreen extends DScreen<DNGFClient, Graphics2DRenderer> implements DUIListener
{
	DUI dui;
	DTextBox address;
	DButton enter;
	DButton back;
	
	public void onActivate( DNGFClient e, DScreenHandler<DNGFClient, Graphics2DRenderer> dsh )
	{
		if( dui == null )
		{
			AWTComponentEventMapper em = new AWTComponentEventMapper();
			em.register( e.canvas );
			dui = new DUI( em );
			address = new DTextBox( e.getWidth() / 2 - 200, e.getHeight()/2 - 120, 400, 100 );
			back = new DButton( "Back", e.getWidth() / 2 - 200, e.getHeight()/2 + 20, 200, 100 );
			enter = new DButton( "Join", e.getWidth() / 2, e.getHeight()/2 + 20, 200, 100 );
			
			dui.add( address );
			dui.add( enter );
			dui.add( back );
			
			dui.setFocus( address );
			
			dui.addDUIListener( this );
		}
		dui.setEnabled( true );
		
		if( e.server != null )
		{
			address.setText( "localhost" );
		}
	}
	
	public void update( DNGFClient gc, int delta )
	{
		dui.update();
	}

	public void render( DNGFClient gc, Graphics2DRenderer g )
	{
		dui.render( g );
	}

	public void onExit()
	{
		dui.setEnabled( false );
	}
	
	public void event( DUIEvent event )
	{
		DUIElement e = event.getElement();
		if( e instanceof DButton && event.getType() == DButton.MOUSE_UP )
		{
			if( e == enter )
			{
				doConnect();
			} 
			else if( e == back )
			{
				dsh.activate( "home", gc );
			}
		} else if( e instanceof DTextBox )
		{
			if( event.getType() == KeyEvent.VK_ENTER )
			{
				doConnect();
			}
		}
	}
	
	void doConnect()
	{
		String addr = address.getText().trim();
		try 
		{
			gc.startClient( addr );
			while( true )
			{
				if( gc.client.hasClientMessages() )
				{
					DMessage m = gc.client.getNextClientMessage();
					
					if( m.message instanceof DNGFInternalMessage )
					{
						DNGFInternalMessage im = (DNGFInternalMessage)m.message;
						if( im.type == DNGFInternalMessageType.LOBBY )
						{
							dsh.activate( "lobby", gc );
							break;
						}
						else
						{
							dsh.activate( "play", gc );
							break;
						}
					}
				}
			}
			
		} catch( IOException e )
		{
			
		}
	}

	@Override
	public void message( Object o )
	{
		
	} 
}
